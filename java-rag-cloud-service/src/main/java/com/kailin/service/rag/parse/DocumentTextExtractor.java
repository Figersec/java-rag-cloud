package com.kailin.service.rag.parse;

import com.kailin.api.KBException;
import com.kailin.api.RagKRMessage;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocumentTextExtractor {

    public String extract(Path file, String fileType) {
        try {
            return switch (fileType) {
                case "pdf" -> extractPdf(file);
                case "docx" -> extractDocx(file);
                case "txt", "md" -> Files.readString(file, StandardCharsets.UTF_8);
                default -> throw new KBException(RagKRMessage.FILE_TYPE_UNSUPPORTED);
            };
        } catch (KBException e) {
            throw e;
        } catch (Exception e) {
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, e.getMessage());
        }
    }

    private String extractPdf(Path file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.toFile())) {
            return new PDFTextStripper().getText(document);
        }
    }

    private String extractDocx(Path file) throws IOException {
        try (InputStream in = Files.newInputStream(file);
             XWPFDocument document = new XWPFDocument(in);
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }
}
