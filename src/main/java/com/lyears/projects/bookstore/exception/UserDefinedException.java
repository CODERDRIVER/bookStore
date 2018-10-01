package com.lyears.projects.bookstore.exception;

import com.lyears.projects.bookstore.util.ResultEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author fzm
 * @date 2018/9/28
 **/
public class UserDefinedException extends RuntimeException {
    @Getter
    @Setter
    private Integer code;

    public UserDefinedException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }
}
