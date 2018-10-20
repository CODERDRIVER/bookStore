package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.util.CookieUtil;
import com.lyears.projects.bookstore.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Controller
public class ViewHandler {

    @Autowired
    private HttpServletResponse response;

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
    public String index(@CookieValue(value = "token",required = false)Cookie token) {
        if (token == null)
        {
            return "index";
        }else{
            String type = JwtToken.verifyToken(token.getValue()).get("type").asString();
            return type;
        }
    }

    /**
     * 管理员页面跳转
     */
    @GetMapping("/librarian/pages/{page}")
    public String librarianPage(@PathVariable("page") String page,@RequestParam(value = "readerId",required = false) String readerId)
    {
//        if (readerId!=null)
//        {
//            response.setHeader("readerId",readerId);
//        }
        CookieUtil.createCookie("readerId",readerId,60*60*12,"/",response);
        return page;
    }


}
