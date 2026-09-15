package com.kailin.controller.chatlog;

import com.kailin.request.common.BaseIdRequest;
import com.kailin.response.chatlog.ChatLogRes;
import com.kailin.service.chatlog.IChatLogService;
import com.kailin.api.KpResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 杨松
 */
@RestController
@RequestMapping(value = "/chatlog")
@Tag(name = "1.0 聊天记录-API ")
@RequiredArgsConstructor
public class ChatLogController {

    private final IChatLogService iChatLogService;

    @PostMapping("/getChatLogByChatId")
    @Operation(summary = "根据chatId获取聊天记录")
    public KpResponse<List<ChatLogRes>> getChatLogByChatId(@RequestBody BaseIdRequest baseIdRequest) {
        List<ChatLogRes> chatLogResList = iChatLogService.getChatLogByChatId(baseIdRequest.getId());
        return KpResponse.data(chatLogResList);
    }
}
