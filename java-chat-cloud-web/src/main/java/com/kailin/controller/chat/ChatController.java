package com.kailin.controller.chat;

import com.kailin.request.chat.ChatReq;
import com.kailin.response.chat.ChatRes;
import com.kailin.service.chat.IChatService;
import com.kailinjt.middleware.kp.common.api.entity.KpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天Controller
 *
 * @author hujia
 */
@RestController
@RequestMapping(value = "/chat")
@Api(tags = "1.0 聊天-API ")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private IChatService iChatService;

    @PostMapping("/openChat")
    @ApiOperation(value = "创建聊天室")
    public KpResponse<ChatRes> openChat(@RequestBody ChatReq chatRe) {
        ChatRes chat = iChatService.openChat(chatRe);
        return KpResponse.data(chat);
    }

}
