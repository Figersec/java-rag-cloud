package com.kailin.service.rag;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.kailin.api.KBException;
import com.kailin.api.RagKRMessage;
import com.kailin.config.rag.RagProperties;
import com.kailin.dao.rag.entity.RagChunk;
import com.kailin.dao.rag.entity.RagDocument;
import com.kailin.dao.rag.entity.RagKb;
import com.kailin.dao.rag.mapper.RagChunkMapper;
import com.kailin.dao.rag.mapper.RagDocumentMapper;
import com.kailin.dao.rag.mapper.RagKbMapper;
import com.kailin.request.rag.KbCreateReq;
import com.kailin.response.rag.KbRes;
import com.kailin.response.rag.RagAskRes;
import com.kailin.response.rag.RagDocumentRes;
import com.kailin.response.rag.RagRetrieveRes;
import com.kailin.service.rag.llm.OpenAiCompatibleClient;
import com.kailin.service.rag.parse.DocumentTextExtractor;
import com.kailin.service.rag.parse.TextChunker;
import com.kailin.service.rag.vector.MilvusVectorStore;
import com.kailin.service.rag.vector.VectorHit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class RagService {

    private static final Set<String> SUPPORTED_TYPES = Set.of("pdf", "docx", "txt", "md");

    private final RagKbMapper ragKbMapper;
    private final RagDocumentMapper ragDocumentMapper;
    private final RagChunkMapper ragChunkMapper;
    private final RagProperties ragProperties;
    private final DocumentTextExtractor documentTextExtractor;
    private final TextChunker textChunker;
    private final OpenAiCompatibleClient openAiCompatibleClient;
    private final MilvusVectorStore milvusVectorStore;

    public KbRes createKb(KbCreateReq req) {
        RagKb kb = new RagKb();
        kb.setName(req.getName().trim());
        kb.setDescription(req.getDescription());
        ragKbMapper.insert(kb);
        return toKbRes(kb);
    }

    public List<KbRes> listKb() {
        return ragKbMapper.selectList(Wrappers.lambdaQuery(RagKb.class)
                        .orderByDesc(RagKb::getCreateTime))
                .stream()
                .map(this::toKbRes)
                .collect(Collectors.toList());
    }

    public List<RagDocumentRes> listDocuments(String kbId) {
        requireKb(kbId);
        return ragDocumentMapper.selectList(Wrappers.lambdaQuery(RagDocument.class)
                        .eq(RagDocument::getKbId, kbId)
                        .orderByDesc(RagDocument::getCreateTime))
                .stream()
                .map(this::toDocumentRes)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteKb(String kbId) {
        requireKb(kbId);
        List<RagDocument> documents = ragDocumentMapper.selectList(Wrappers.lambdaQuery(RagDocument.class)
                .eq(RagDocument::getKbId, kbId)
                .select(RagDocument::getId, RagDocument::getFilePath));
        milvusVectorStore.deleteByKbId(kbId);
        ragChunkMapper.delete(Wrappers.lambdaQuery(RagChunk.class).eq(RagChunk::getKbId, kbId));
        ragDocumentMapper.delete(Wrappers.lambdaQuery(RagDocument.class).eq(RagDocument::getKbId, kbId));
        ragKbMapper.deleteById(kbId);
        documents.forEach(item -> deleteLocalFile(item.getFilePath()));
        deleteKbDirectory(kbId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteDocument(String docId) {
        RagDocument document = requireDocument(docId);
        purgeIndexedChunks(document.getId());
        ragDocumentMapper.deleteById(document.getId());
        deleteLocalFile(document.getFilePath());
    }

    @Transactional(rollbackFor = Exception.class)
    public RagDocumentRes reindex(String docId) {
        RagDocument document = requireDocument(docId);
        Path stored = Path.of(StringUtils.defaultString(document.getFilePath()));
        if (StringUtils.isBlank(document.getFilePath()) || !Files.isRegularFile(stored)) {
            throw new KBException(RagKRMessage.FILE_MISSING);
        }
        document.setStatus("PROCESSING");
        document.setErrorMsg(null);
        ragDocumentMapper.updateById(document);
        try {
            ingest(document, stored, document.getFileType());
            document.setStatus("READY");
            document.setErrorMsg(null);
            ragDocumentMapper.updateById(document);
        } catch (Exception e) {
            log.error("重新入库失败 docId={}", document.getId(), e);
            document.setStatus("FAILED");
            document.setErrorMsg(StringUtils.abbreviate(e.getMessage(), 1000));
            ragDocumentMapper.updateById(document);
            if (e instanceof KBException kbException) {
                throw kbException;
            }
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, e.getMessage());
        }
        return toDocumentRes(document);
    }

    @Transactional(rollbackFor = Exception.class)
    public RagDocumentRes upload(String kbId, MultipartFile file) {
        requireKb(kbId);
        if (file == null || file.isEmpty()) {
            throw new KBException(RagKRMessage.EMPTY_CONTENT, "请上传文件");
        }
        String originalName = StringUtils.defaultIfBlank(file.getOriginalFilename(), "unnamed");
        String fileType = fileType(originalName);
        if (!SUPPORTED_TYPES.contains(fileType)) {
            throw new KBException(RagKRMessage.FILE_TYPE_UNSUPPORTED);
        }

        RagDocument document = new RagDocument();
        document.setKbId(kbId);
        document.setFileName(originalName);
        document.setFileType(fileType);
        document.setStatus("PROCESSING");
        document.setChunkCount(0);
        document.setFilePath("");
        ragDocumentMapper.insert(document);

        Path stored = storeFile(kbId, document.getId(), originalName, file);
        document.setFilePath(stored.toString());
        ragDocumentMapper.updateById(document);

        try {
            ingest(document, stored, fileType);
            document.setStatus("READY");
            document.setErrorMsg(null);
            ragDocumentMapper.updateById(document);
        } catch (Exception e) {
            log.error("文档入库失败 docId={}", document.getId(), e);
            document.setStatus("FAILED");
            document.setErrorMsg(StringUtils.abbreviate(e.getMessage(), 1000));
            ragDocumentMapper.updateById(document);
            if (e instanceof KBException kbException) {
                throw kbException;
            }
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, e.getMessage());
        }
        return toDocumentRes(document);
    }

    public RagAskRes ask(String kbId, String question, Integer topK) {
        AskContext ctx = retrieve(kbId, question, topK);
        if (ctx.empty()) {
            RagAskRes empty = new RagAskRes();
            empty.setAnswer("知识库中没有检索到相关内容。");
            return empty;
        }
        String answer = openAiCompatibleClient.chat(SYSTEM_PROMPT, userPrompt(ctx.evidence(), question));
        ctx.res().setAnswer(answer);
        return ctx.res();
    }

    public void askStream(String kbId, String question, Integer topK, java.util.function.Consumer<Map<String, Object>> emit) {
        emit.accept(event("status", "正在检索知识库…"));
        AskContext ctx = retrieve(kbId, question, topK);
        if (ctx.empty()) {
            emit.accept(event("status", "未检索到相关资料"));
            emit.accept(event("delta", "知识库中没有检索到相关内容。"));
            emit.accept(Map.of("type", "done"));
            return;
        }
        emit.accept(event("status", "正在结合资料生成回答…"));
        StringBuilder answer = new StringBuilder();
        openAiCompatibleClient.chatStream(SYSTEM_PROMPT, userPrompt(ctx.evidence(), question),
                reasoning -> {
                    if (answer.isEmpty()) {
                        emit.accept(event("reasoning", reasoning));
                    }
                },
                piece -> {
                    answer.append(piece);
                    emit.accept(event("delta", piece));
                });
        if (answer.isEmpty()) {
            emit.accept(event("delta", "模型未返回内容"));
        }
        emit.accept(Map.of("type", "done"));
    }

    public RagRetrieveRes retrievePreview(String kbId, String question, Integer topK) {
        AskContext ctx = retrieve(kbId, question, topK);
        RagRetrieveRes res = new RagRetrieveRes();
        res.setQuestion(question);
        res.setTopK(topK == null || topK <= 0 ? 5 : Math.min(topK, 20));
        res.setHits(ctx.res().getCitations());
        res.setHitCount(ctx.res().getCitations() == null ? 0 : ctx.res().getCitations().size());
        return res;
    }

    private Map<String, Object> event(String type, String content) {
        Map<String, Object> event = new HashMap<>();
        event.put("type", type);
        event.put("content", content);
        return event;
    }

    private static final String SYSTEM_PROMPT = """
            你是企业知识库助手。只根据提供的资料回答用户的问题。
            要求：直接给出简洁、结构化的结论，不要原文粘贴资料，不要罗列参考来源或切片全文。
            资料中没有的内容明确说不知道，不要编造。""";

    private String userPrompt(String evidence, String question) {
        return "【资料】\n" + evidence + "【问题】\n" + question;
    }

    private AskContext retrieve(String kbId, String question, Integer topK) {
        requireKb(kbId);
        int k = topK == null || topK <= 0 ? 5 : Math.min(topK, 20);
        List<Float> queryVector = openAiCompatibleClient.embed(List.of(question)).get(0);
        List<VectorHit> hits = milvusVectorStore.search(kbId, queryVector, k);
        RagAskRes res = new RagAskRes();
        if (hits.isEmpty()) {
            return new AskContext(res, "");
        }
        List<String> chunkIds = hits.stream().map(VectorHit::getChunkId).filter(StringUtils::isNotBlank).toList();
        if (chunkIds.isEmpty()) {
            return new AskContext(res, "");
        }
        Map<String, RagChunk> chunkMap = ragChunkMapper.selectBatchIds(chunkIds).stream()
                .collect(Collectors.toMap(RagChunk::getId, item -> item, (a, b) -> a));
        List<String> docIds = hits.stream().map(VectorHit::getDocId).filter(StringUtils::isNotBlank).distinct().toList();
        Map<String, RagDocument> documentMap = docIds.isEmpty()
                ? Map.of()
                : ragDocumentMapper.selectBatchIds(docIds).stream()
                .collect(Collectors.toMap(RagDocument::getId, item -> item, (a, b) -> a));

        StringBuilder evidence = new StringBuilder();
        int index = 1;
        for (VectorHit hit : hits) {
            RagChunk chunk = chunkMap.get(hit.getChunkId());
            if (chunk == null || StringUtils.isBlank(chunk.getContent())) {
                continue;
            }
            RagDocument document = documentMap.get(hit.getDocId());
            String fileName = document == null ? "" : document.getFileName();
            evidence.append(index).append(". (来自文件 ").append(fileName).append(")\n")
                    .append(chunk.getContent()).append("\n\n");
            RagAskRes.Citation citation = new RagAskRes.Citation();
            citation.setChunkId(chunk.getId());
            citation.setDocId(chunk.getDocId());
            citation.setFileName(fileName);
            citation.setContent(chunk.getContent());
            citation.setScore(hit.getScore());
            res.getCitations().add(citation);
            index++;
        }
        return new AskContext(res, evidence.toString());
    }

    private record AskContext(RagAskRes res, String evidence) {
        boolean empty() {
            return res.getCitations() == null || res.getCitations().isEmpty();
        }
    }

    private void ingest(RagDocument document, Path stored, String fileType) {
        String text = documentTextExtractor.extract(stored, fileType);
        List<String> chunks = textChunker.chunk(text, ragProperties.getChunk().getSize(), ragProperties.getChunk().getOverlap());
        if (chunks.isEmpty()) {
            throw new KBException(RagKRMessage.EMPTY_CONTENT);
        }
        milvusVectorStore.deleteByDocId(document.getId());
        ragChunkMapper.delete(Wrappers.lambdaQuery(RagChunk.class).eq(RagChunk::getDocId, document.getId()));

        List<RagChunk> entities = new ArrayList<>(chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            RagChunk chunk = new RagChunk();
            chunk.setKbId(document.getKbId());
            chunk.setDocId(document.getId());
            chunk.setChunkIndex(i);
            chunk.setContent(chunks.get(i));
            entities.add(chunk);
        }
        Db.saveBatch(entities, 200);
        List<String> chunkIds = entities.stream().map(RagChunk::getId).toList();
        List<List<Float>> vectors = openAiCompatibleClient.embed(chunks);
        milvusVectorStore.insert(document.getKbId(), document.getId(), chunkIds, vectors);
        document.setChunkCount(entities.size());
    }

    private void purgeIndexedChunks(String docId) {
        milvusVectorStore.deleteByDocId(docId);
        ragChunkMapper.delete(Wrappers.lambdaQuery(RagChunk.class).eq(RagChunk::getDocId, docId));
    }

    private void deleteLocalFile(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return;
        }
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (Exception e) {
            log.warn("删除本地文件失败 path={}", filePath, e);
        }
    }

    private void deleteKbDirectory(String kbId) {
        Path dir = Path.of(ragProperties.getStorage().getLocalDir(), kbId);
        if (!Files.exists(dir)) {
            return;
        }
        try (Stream<Path> walk = Files.walk(dir)) {
            walk.sorted(Comparator.reverseOrder()).forEach(path -> {
                try {
                    Files.deleteIfExists(path);
                } catch (Exception e) {
                    log.warn("删除知识库目录失败 path={}", path, e);
                }
            });
        } catch (Exception e) {
            log.warn("删除知识库目录失败 kbId={}", kbId, e);
        }
    }

    private Path storeFile(String kbId, String docId, String originalName, MultipartFile file) {
        try {
            Path dir = Path.of(ragProperties.getStorage().getLocalDir(), kbId);
            Files.createDirectories(dir);
            String safeName = originalName.replaceAll("[\\\\/:*?\"<>|]", "_");
            Path target = dir.resolve(docId + "_" + safeName);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            if (Files.size(target) <= 0) {
                throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, "保存后的文件为空");
            }
            return target.toAbsolutePath();
        } catch (Exception e) {
            throw new KBException(RagKRMessage.DOCUMENT_PARSE_FAILED, "保存文件失败: " + e.getMessage());
        }
    }

    private RagDocument requireDocument(String docId) {
        RagDocument document = ragDocumentMapper.selectById(docId);
        if (document == null) {
            throw new KBException(RagKRMessage.DOCUMENT_NOT_FOUND);
        }
        return document;
    }

    private RagKb requireKb(String kbId) {
        RagKb kb = ragKbMapper.selectById(kbId);
        if (kb == null) {
            throw new KBException(RagKRMessage.KB_NOT_FOUND);
        }
        return kb;
    }

    private String fileType(String fileName) {
        int idx = fileName.lastIndexOf('.');
        if (idx < 0 || idx == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(idx + 1).toLowerCase(Locale.ROOT);
    }

    private KbRes toKbRes(RagKb kb) {
        KbRes res = new KbRes();
        BeanUtils.copyProperties(kb, res);
        return res;
    }

    private RagDocumentRes toDocumentRes(RagDocument document) {
        RagDocumentRes res = new RagDocumentRes();
        BeanUtils.copyProperties(document, res);
        return res;
    }
}
