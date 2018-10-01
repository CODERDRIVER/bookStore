package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author fzm
 * @date 2018/9/27
 **/
@Controller
public class LoginHandler {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login/admin")
    @ResponseBody
    public ResponseMessage login(@RequestBody Administrator administrator, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String email = administrator.getEmail();
        String password = administrator.getPassword();

        Administrator one = adminService.findByEmail(email);
        if (one != null) {
            if (one.getPassword().equals(password)) {
                String jwtToken = JwtToken.createToken(email, one.getUserName());
                Cookie jwtTokenCookie = new Cookie("token", jwtToken);
                jwtTokenCookie.setMaxAge(60 * 60 * 12);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
                return ResultUtil.success(one, request.getRequestURL().toString());
            } else {
                throw new UserDefinedException(ResultEnum.PASSWORD_ERROR);
            }
        } else {
            throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
        }
    }
}
