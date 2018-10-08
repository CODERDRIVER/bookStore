package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.LibrarianService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Controller
public class LibrarianHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LibrarianService librarianService;

    /**
     * 注册图书管理员
     *
     * @param librarian 传入的图书管理员信息
     * @return 返回结果
     */
    @PostMapping(value = "/add/librarian")
    @ResponseBody
    public ResponseMessage saveReader(@RequestBody Librarian librarian, @CookieValue(value = "token") Cookie token) {
        String type = "admin";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new UserDefinedException(ResultEnum.NO_RIGHT);
        }
        librarianService.save(librarian);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

}
