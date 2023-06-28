package com.kailin.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yangsong
 */
@Getter
@AllArgsConstructor
public enum CommonEnum implements IEnum<Integer> {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    @EnumValue
    private final Integer value;
    private final String name;

    @Override
    public Integer getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
