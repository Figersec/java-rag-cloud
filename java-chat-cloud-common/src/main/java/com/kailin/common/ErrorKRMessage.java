package com.kailin.common;

import com.kailinjt.middleware.kp.common.api.entity.KRMessage;

/**
 * 错误码及错误信息定义
 * @Author chengpuhui
 * @Date 2022/1/12
 */
public enum ErrorKRMessage implements KRMessage {
    // 建议错误码以10000起，低于10000为集团公共错误码
    USER_NOT_EXIST(10001, "用户不存在！"),
    ;

    private final int code;
    private final String message;
    private final String systemName;

    ErrorKRMessage(int code, String message){
        this.code = code;
        this.message = message;
        this.systemName = "java-chat-cloud";
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getSystemName() {
        return this.systemName;
    }
}
