package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@ControllerAdvice
public class DefinedExceptionHandler {

    @Autowired
    HttpServletRequest request;

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public ResponseMessage handle(RuntimeException e) {
        if (e instanceof UserDefinedException) {
            UserDefinedException userDefinedException = (UserDefinedException) e;
            return ResultUtil.error(userDefinedException.getCode(), userDefinedException.getMessage(), request.getRequestURL().toString());
        } else {
            e.printStackTrace();
            return ResultUtil.error(ResultEnum.SYSTEM_ERROR, request.getRequestURL().toString());
        }
    }

    @ExceptionHandler(value = ErrorPageException.class)
    public ModelAndView handle(ErrorPageException e) {
        ModelAndView mav = new ModelAndView("errorPage");
        if (e.getCode() == -1) {
            mav.addObject("errorCode", -1);
        }
        return mav;
    }
}
