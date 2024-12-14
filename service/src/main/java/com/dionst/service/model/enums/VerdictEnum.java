package com.dionst.service.model.enums;


import lombok.Data;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum VerdictEnum {

    Accepted(0, "accepted"),
    OutOfMemoryError(1, "OutOfMemoryError"),
    StackOverflowError(2, "StackOverflowError"),
    OutOfTimeError(3, "OutOfTimeError"),
    SegmentError(4, "SegmentError"),
    RunError(5, "运行错误"),
    WrongAnswer(6, "答案错误"),
    SystemError(7, "系统错误"),

    InQueue(8, "队列中"),
    Pending(9, "等待中"), Running(10, "判题中");


    private final int value;
    private final String text;

    VerdictEnum(int value, String text) {
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
        for (VerdictEnum anEnum : VerdictEnum.values()) {
            if (anEnum.text.equals(text)) {
                return anEnum.getValue();
            }
        }
        return null;
    }

}
