package com.kailin.controller.chatlog;

import com.kailin.request.common.BaseIdRequest;
import com.kailin.response.chatlog.ChatLogRes;
import com.kailin.service.chatlog.IChatLogService;
import com.kailinjt.middleware.kp.common.api.entity.KpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 杨松
 */
@RestController
@RequestMapping(value = "/chatlog")
@Api(tags = "1.0 聊天记录-API ")
@RequiredArgsConstructor
public class ChatLogController {

    private final IChatLogService iChatLogService;

    @PostMapping("/getChatLogByChatId")
    @ApiOperation(value = "根据chatId获取聊天记录")
    public KpResponse<List<ChatLogRes>> openChat(@RequestBody BaseIdRequest baseIdRequest) {
        List<ChatLogRes> chatLogResList = iChatLogService.getChatLogByChatId(baseIdRequest.getId());
        return KpResponse.data(chatLogResList);
    }
}
