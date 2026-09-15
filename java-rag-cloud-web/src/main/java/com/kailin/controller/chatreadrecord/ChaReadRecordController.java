package com.kailin.controller.chatreadrecord;

import com.kailin.request.chatreadrecord.ChatReadRecordBatchReq;
import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.request.chatreadrecord.UpdateChatRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;
import com.kailin.service.chatreadrecord.IChatReadRecordService;
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
@RequestMapping(value = "/chatreadrecord")
@Tag(name = "1.0 聊天记录-API ")
@RequiredArgsConstructor
public class ChaReadRecordController {

    private final IChatReadRecordService iChatReadRecordService;

    @PostMapping("/getChatReadRecordListByCondition")
    @Operation(summary = "获取会话组每个用户阅读情况")
    public KpResponse<List<ChatReadRecordRes>> getChatReadRecordListByCondition(@RequestBody ChatReadRecordReq chatReadRecordReq) {
        List<ChatReadRecordRes> chatLogResList = iChatReadRecordService.getChatReadRecordListByCondition(chatReadRecordReq);
        return KpResponse.data(chatLogResList);
    }


    @PostMapping("/updateRead")
    @Operation(summary = "更新为已读")
    public KpResponse<Boolean> updateReadRecord(@RequestBody UpdateChatRecordReq updateChatRecordReq) {
        Boolean success =  iChatReadRecordService.updateReadRecord(updateChatRecordReq);
        return KpResponse.data(success);
    }
}
