package com.dionst.service.model.enums;

import io.swagger.models.auth.In;
import lombok.Getter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 */
@Getter
public enum UserRoleEnum {

    USER("用户", 0),
    ADMIN("管理员", 1),
    BAN("被封号", 2);

    private final String text;

    private final int value;

    UserRoleEnum(String text, int value) {
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
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.text.equals(text)) {
                return anEnum.getValue();
            }
        }
        return null;
    }

    public static UserRoleEnum getEnumByValue(Integer userRole) {
        if (userRole == null) {
            return null;
        }
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (userRole == anEnum.getValue()) {
                return anEnum;
            }
        }
        return null;
    }
}
