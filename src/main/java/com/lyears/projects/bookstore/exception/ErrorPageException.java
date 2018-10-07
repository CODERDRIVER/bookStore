package com.lyears.projects.bookstore.exception;

import com.lyears.projects.bookstore.util.ResultEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fzm
 * @date 2018/10/7
 **/
public class ErrorPageException extends RuntimeException {
    @Getter
    @Setter
    private Integer code;

    public ErrorPageException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
