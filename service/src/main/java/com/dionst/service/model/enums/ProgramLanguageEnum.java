package com.dionst.service.model.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 */
@Getter
public enum ProgramLanguageEnum {

    JAVA("Java", 1),
    CPP("cpp", 2);

    private final String text;

    private final int value;

    ProgramLanguageEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param text
     * @return
     */
    public static Integer getEnumByText(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        for (ProgramLanguageEnum anEnum : ProgramLanguageEnum.values()) {
            if (anEnum.text.equals(text)) {
                return anEnum.getValue();
            }
        }
        return null;
    }
    public static ProgramLanguageEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ProgramLanguageEnum anEnum : ProgramLanguageEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

}
