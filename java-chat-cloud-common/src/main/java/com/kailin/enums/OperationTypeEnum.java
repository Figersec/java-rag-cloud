package com.kailin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yangsong
 */
@Getter
@AllArgsConstructor
public enum OperationTypeEnum implements IEnum<String> {


    /**
     * 发送
     */
    SEND("send", "发送消息"),

    /**
     * 撤回
     */
    RECALL("recall", "撤回消息"),

    /**
     * 检测心跳
     */
    CHECK_HEART("checkHeart", "检测心跳");

    @EnumValue
    private final String value;
    private final String name;

    public static final Map<String, OperationTypeEnum> VALUE_MAP = Arrays.stream(values()).collect(Collectors.toMap(OperationTypeEnum::getValue, e -> e, (v1, v2) -> v2));

    @Override
    public String getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static OperationTypeEnum getEnumByValue(String value){
        return VALUE_MAP.get(value);
    }
}
