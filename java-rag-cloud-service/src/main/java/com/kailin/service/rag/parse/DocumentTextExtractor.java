package com.kailin.service.rag.parse;

import com.kailin.api.KBException;
import com.kailin.api.RagKRMessage;
import com.kailin.config.rag.RagProperties;
import com.kailin.service.rag.llm.OpenAiCompatibleClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class DocumentTextExtractor {

    private final RagProperties ragProperties;
    private final OpenAiCompatibleClient openAiCompatibleClient;

    public String extract(Path file, String fileType) {
        try {
            String text = switch (fileType) {
                case "pdf" -> extractPdf(file);
                case "docx" -> extractDocx(file);
                case "txt", "md" -> readPlainText(file);
                default -> throw new KBException(RagKRMessage.FILE_TYPE_UNSUPPORTED);
            };
            if (StringUtils.isBlank(text)) {
                if ("pdf".equals(fileType)) {
                    throw new KBException(RagKRMessage.EMPTY_CONTENT,
                            "该 PDF 没有可提取文字。若为扫描件，请确认 rag.ocr 已开启且 OCR 模型可用");
                }
                throw new KBException(RagKRMessage.EMPTY_CONTENT);
            }
            return text;
        } catch (KBException e) {
            throw e;
        } catch (InvalidPasswordException e) {
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, "PDF 已加密，无法解析");
        } catch (Exception e) {
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, e.getMessage());
        }
    }

    private String extractPdf(Path file) throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        try (PDDocument document = Loader.loadPDF(bytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setSortByPosition(true);
            stripper.setShouldSeparateByBeads(false);
            stripper.setAddMoreFormatting(true);
            stripper.setLineSeparator("\n");
            stripper.setStartPage(1);
            stripper.setEndPage(Math.max(1, document.getNumberOfPages()));
            StringBuilder text = new StringBuilder(StringUtils.defaultString(stripper.getText(document)));
            appendFormText(document, text);
            String extracted = text.toString();
            if (StringUtils.isNotBlank(extracted)) {
                return extracted;
            }
            log.info("PDFBox 未抽出文字，pages={} size={}，尝试 OCR", document.getNumberOfPages(), bytes.length);
            return ocrPdfPages(document);
        }
    }

    private String ocrPdfPages(PDDocument document) throws IOException {
        RagProperties.Ocr ocr = ragProperties.getOcr();
        if (ocr == null || !ocr.isEnabled()) {
            return "";
        }
        int pages = Math.min(document.getNumberOfPages(), Math.max(1, ocr.getMaxPages()));
        PDFRenderer renderer = new PDFRenderer(document);
        StringBuilder all = new StringBuilder();
        for (int i = 0; i < pages; i++) {
            BufferedImage image = renderer.renderImageWithDPI(i, Math.max(96, ocr.getDpi()), ImageType.RGB);
            image = scaleDown(image, 1600);
            byte[] jpeg = toJpeg(image, 0.82f);
            log.info("OCR PDF 第 {}/{} 页, imageBytes={}", i + 1, pages, jpeg.length);
            String pageText = openAiCompatibleClient.ocrImage(jpeg);
            if (StringUtils.isNotBlank(pageText)) {
                all.append(pageText.trim()).append("\n\n");
            }
        }
        return all.toString();
    }

    private BufferedImage scaleDown(BufferedImage image, int maxWidth) {
        if (image.getWidth() <= maxWidth) {
            return image;
        }
        int height = (int) Math.round(image.getHeight() * (maxWidth / (double) image.getWidth()));
        BufferedImage scaled = new BufferedImage(maxWidth, Math.max(1, height), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaled.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(image, 0, 0, maxWidth, height, null);
        g.dispose();
        return scaled;
    }

    private byte[] toJpeg(BufferedImage image, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             MemoryCacheImageOutputStream ios = new MemoryCacheImageOutputStream(out)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
            return out.toByteArray();
        }
    }

    private void appendFormText(PDDocument document, StringBuilder text) throws IOException {
        PDAcroForm form = document.getDocumentCatalog().getAcroForm();
        if (form == null) {
            return;
        }
        for (PDField field : form.getFieldTree()) {
            String value = field.getValueAsString();
            if (StringUtils.isNotBlank(value)) {
                text.append('\n').append(value);
            }
        }
    }

    private String extractDocx(Path file) throws IOException {
        try (InputStream in = Files.newInputStream(file);
             XWPFDocument document = new XWPFDocument(in);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    private String readPlainText(Path file) throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        Charset charset = detectCharset(bytes);
        return new String(bytes, charset);
    }

    private Charset detectCharset(byte[] bytes) {
        if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
            return StandardCharsets.UTF_8;
        }
        if (bytes.length >= 2 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xFE) {
            return StandardCharsets.UTF_16LE;
        }
        if (bytes.length >= 2 && bytes[0] == (byte) 0xFE && bytes[1] == (byte) 0xFF) {
            return StandardCharsets.UTF_16BE;
        }
        return StandardCharsets.UTF_8;
    }
}
