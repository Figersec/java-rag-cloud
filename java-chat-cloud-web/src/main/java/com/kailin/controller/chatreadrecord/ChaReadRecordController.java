package com.kailin.controller.chatreadrecord;

import com.kailin.request.chatreadrecord.ChatReadRecordReq;
import com.kailin.response.chatreadrecord.ChatReadRecordRes;
import com.kailin.service.chatreadrecord.IChatReadRecordService;
import com.kailinjt.middleware.kp.common.api.entity.KpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "1.0 聊天记录-API ")
@RequiredArgsConstructor
public class ChaReadRecordController {

    private final IChatReadRecordService iChatReadRecordService;

    @PostMapping("/getUserReadRecordList")
    @ApiOperation(value = "获取会话组每个用户阅读情况")
    public KpResponse<List<ChatReadRecordRes>> getUserReadRecordList(@RequestBody ChatReadRecordReq chatReadRecordReq) {
        List<ChatReadRecordRes> chatLogResList = iChatReadRecordService.getUserReadRecordList(chatReadRecordReq);
        return KpResponse.data(chatLogResList);
    }
}
