package com.kailin.controller.rag;

import com.kailin.api.KpResponse;
import com.kailin.request.rag.KbCreateReq;
import com.kailin.request.rag.KbIdReq;
import com.kailin.request.rag.RagAskReq;
import com.kailin.response.rag.KbRes;
import com.kailin.response.rag.RagAskRes;
import com.kailin.response.rag.RagDocumentRes;
import com.kailin.service.rag.RagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.List;

@RestController
@RequestMapping("/rag")
@Tag(name = "2.0 知识库 RAG")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

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

    @PostMapping("/ask")
    @Operation(summary = "知识库问答")
    public KpResponse<RagAskRes> ask(@Valid @RequestBody RagAskReq req) {
        return KpResponse.data(ragService.ask(req.getKbId(), req.getQuestion(), req.getTopK()));
    }
}
