package com.kailin.controller.chat;

import com.kailin.request.chat.ChatReq;
import com.kailin.response.chat.ChatRes;
import com.kailin.service.chat.IChatService;
import com.kailin.api.KpResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@Tag(name = "1.0 聊天-API ")
@RequiredArgsConstructor
public class ChatController {

    @Autowired
    private IChatService iChatService;

    @PostMapping("/openChat")
    @Operation(summary = "创建聊天室")
    public KpResponse<ChatRes> openChat(@RequestBody ChatReq chatRe) {
        ChatRes chat = iChatService.openChat(chatRe);
        return KpResponse.data(chat);
    }

    @PostMapping("/updateChat")
    @Operation(summary = "编辑聊天室")
    public KpResponse<Boolean> updateChat(@RequestBody @Validated({ChatReq.Update.class}) ChatReq chatReq) {
        return KpResponse.data(iChatService.updateChat(chatReq));
    }

}
