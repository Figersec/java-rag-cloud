package com.kailin.controller.rag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kailin.api.KpResponse;
import com.kailin.request.rag.DocIdReq;
import com.kailin.request.rag.KbCreateReq;
import com.kailin.request.rag.KbIdReq;
import com.kailin.request.rag.RagAskReq;
import com.kailin.response.rag.KbRes;
import com.kailin.response.rag.RagAskRes;
import com.kailin.response.rag.RagDocumentRes;
import com.kailin.response.rag.RagRetrieveRes;
import com.kailin.service.rag.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rag")
@Tag(name = "2.0 知识库 RAG")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;
    private final ObjectMapper objectMapper;

    @PostMapping("/kb/create")
    @Operation(summary = "创建知识库")
    public KpResponse<KbRes> createKb(@Valid @RequestBody KbCreateReq req) {
        return KpResponse.data(ragService.createKb(req));
    }

    @PostMapping("/kb/list")
    @Operation(summary = "知识库列表")
    public KpResponse<List<KbRes>> listKb() {
        return KpResponse.data(ragService.listKb());
    }

    @PostMapping("/kb/delete")
    @Operation(summary = "删除知识库")
    public KpResponse<Void> deleteKb(@Valid @RequestBody KbIdReq req) {
        ragService.deleteKb(req.getKbId());
        return KpResponse.success();
    }

    @PostMapping(value = "/document/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文档并入库")
    public KpResponse<RagDocumentRes> upload(@RequestParam("kbId") String kbId,
                                             @RequestPart("file") MultipartFile file) {
        return KpResponse.data(ragService.upload(kbId, file));
    }

    @PostMapping("/document/list")
    @Operation(summary = "知识库文档列表")
    public KpResponse<List<RagDocumentRes>> listDocuments(@Valid @RequestBody KbIdReq req) {
        return KpResponse.data(ragService.listDocuments(req.getKbId()));
    }

    @PostMapping("/document/delete")
    @Operation(summary = "删除文档")
    public KpResponse<Void> deleteDocument(@Valid @RequestBody DocIdReq req) {
        ragService.deleteDocument(req.getDocId());
        return KpResponse.success();
    }

    @PostMapping("/document/reindex")
    @Operation(summary = "重新入库")
    public KpResponse<RagDocumentRes> reindex(@Valid @RequestBody DocIdReq req) {
        return KpResponse.data(ragService.reindex(req.getDocId()));
    }

    @PostMapping("/retrieve")
    @Operation(summary = "检索预览（不调用大模型）")
    public KpResponse<RagRetrieveRes> retrieve(@Valid @RequestBody RagAskReq req) {
        return KpResponse.data(ragService.retrievePreview(req.getKbId(), req.getQuestion(), req.getTopK()));
    }

    @PostMapping("/ask")
    @Operation(summary = "知识库问答")
    public KpResponse<RagAskRes> ask(@Valid @RequestBody RagAskReq req) {
        return KpResponse.data(ragService.ask(req.getKbId(), req.getQuestion(), req.getTopK()));
    }

    @PostMapping(value = "/ask/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "知识库问答（流式）")
    public void askStream(@Valid @RequestBody RagAskReq req, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-transform");
        response.setHeader("X-Accel-Buffering", "no");
        response.setHeader("Connection", "keep-alive");
        response.flushBuffer();
        PrintWriter out = response.getWriter();
        try {
            ragService.askStream(req.getKbId(), req.getQuestion(), req.getTopK(), event -> writeSse(out, event));
        } catch (Exception e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("type", "error");
            error.put("message", "问答失败");
            writeSse(out, error);
        }
    }

    private void writeSse(PrintWriter out, Map<String, Object> event) {
        try {
            synchronized (out) {
                out.write("data:" + objectMapper.writeValueAsString(event) + "\n\n");
                out.flush();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
