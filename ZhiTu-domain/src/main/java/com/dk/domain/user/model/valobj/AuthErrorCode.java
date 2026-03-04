package com.dk.domain.user.model.valobj;

/**
 * 用户认证相关错误码值对象。
 */
public enum AuthErrorCode {

    /** 账号或密码错误（登录失败） */
    LOGIN_FAIL("A001", "账号或密码错误"),
    /** 用户不存在 */
    USER_NOT_FOUND("A002", "用户不存在"),
    /** 手机号或邮箱已被注册 */
    USER_ALREADY_EXISTS("A003", "手机号或邮箱已被注册"),
    /** 账号格式不合法或未提供手机号/邮箱 */
    INVALID_ACCOUNT("A004", "请提供有效的手机号或邮箱"),
    ;

    private final String code;
    private final String info;

    AuthErrorCode(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
