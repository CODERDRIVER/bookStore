package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.util.ResultEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Controller
public class ViewHandler {

    @GetMapping("/admin")
    public String adminIndex(@CookieValue(value = "token", required = false) Cookie token) {
        String type = "admin";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new ErrorPageException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new ErrorPageException(ResultEnum.NO_RIGHT);
        }
        return "admin";
    }
    @GetMapping("/librarian")
    public String librarianIndex(@CookieValue(value = "token", required = false) Cookie token) {
        String type = "librarian";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new ErrorPageException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new ErrorPageException(ResultEnum.NO_RIGHT);
        }
        return "librarian";
    }

    @GetMapping("/404")
    public String notFoundPage() {
        return "admin-404";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


}
