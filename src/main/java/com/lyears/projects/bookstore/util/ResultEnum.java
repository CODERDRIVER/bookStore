package com.lyears.projects.bookstore.util;

/**
 * @author fzm
 * @date 2018/9/28
 **/
public enum ResultEnum {
    /**
     * 未知错误
     */
    UNKNOWN_ERROR(-100, "unknown error!"),
    NEED_LOGIN(-1, "need login!"),
    REPEAT_REGISTER(-2, "repeat register!"),
    USER_NOT_EXIST(-3, "user not exist!"),
    PASSWORD_ERROR(-4, "password error!"),
    EMPTY_USERNAME(-5, "empty username!"),
    EMPTY_PASSWORD(-6, "empty password!"),
    ERROR_NUMBER(-7, "error number!"),
    NO_RIGHT(-8,"no right!"),
    SUCCESS(0, "success"),
    SYSTEM_ERROR(-1000001, "system error!");

    private Integer code;

    private String msg;

    private ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
