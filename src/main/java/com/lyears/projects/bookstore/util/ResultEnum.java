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
    NO_BOOK_STORE(-8,"no book store!"),
    NO_RIGHT(-9,"no right!"),
    BORROW_NOT_EXIST(-10,"borrow not exist!"),
    NO_ANNOUNCEMENT(-11,"no announcement"),
    SUCCESS(0, "success"),
    NO_BORROWNUM_LEFT(-12,"borrow numbers is max"),
    NO_LIBRARIAN(-13,"no librarian"),
    EMAIL_EXITS(-14,"email exits"),
    PHONENUMBER_EXITS(-15,"phone number exits"),
    FAILURE(-16,"failure"),
    CAN_NOT_DELETE(-17,"can not be deleted"),
    BOOK_NO_BORROW(-18,"book has not borrowd"),
    BARCODE_GENERATE_FAILURE(-19,"barcode generate failure"),
    NO_IMAGE_FILE(-20,"image does not esits"),
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
