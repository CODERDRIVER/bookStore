package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@ControllerAdvice
public class DefinedExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseMessage handle(HttpServletRequest request, RuntimeException e) {
        if (e instanceof UserDefinedException) {
            UserDefinedException userDefinedException = (UserDefinedException) e;
            return ResultUtil.error(userDefinedException.getCode(), userDefinedException.getMessage(), request.getRequestURL().toString());
        } else {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.SYSTEM_ERROR, request.getRequestURL().toString());
        }
    }
}
