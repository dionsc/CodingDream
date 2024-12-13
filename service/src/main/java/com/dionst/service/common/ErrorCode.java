package com.dionst.service.common;

/**
 * 自定义错误码
 */
public enum ErrorCode {

    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    PHONE_PATTERN_ERROR(50002, "手机号格式错误"),
    PHNOE_CODE_ERROE(50003, "验证码错误"),
    FREQUENT_GET_CODE(50004, "频繁获取验证码"),
    QUESTION_NOT_HIDE(50005, "题目没隐藏"),
    PARTICIPANT_NAME_REPEAT(50006, "参赛者名字已存在"),
    UN_LOG_IN(50007, "未登录"), TRY_LOCK_FAILURE(50008, "获取锁失败"),
    CLOSE_TO_START_TIME(50009,"必须在比赛开始的一天前创建比赛" ),
    CONTEST_DURATION_TOO_SHORT(50010,"比赛时长过短" );

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}