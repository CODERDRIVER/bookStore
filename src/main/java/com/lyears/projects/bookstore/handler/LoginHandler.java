package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ReaderService readerService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/login/admin")
    @ResponseBody
    public ResponseMessage login(@RequestBody Administrator administrator, HttpServletResponse response) throws UnsupportedEncodingException {
        String email = administrator.getEmail();
        String password = administrator.getPassword();

        Administrator one = adminService.findByEmail(email);
        if (one != null) {
            if (one.getPassword().equals(password)) {
                String jwtToken = JwtToken.createToken(email, one.getUserName(), "admin");
                Cookie jwtTokenCookie = new Cookie("token", jwtToken);
                jwtTokenCookie.setMaxAge(60 * 60 * 12);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
                return ResultUtil.successNoData(request.getRequestURL().toString());
            } else {
                throw new UserDefinedException(ResultEnum.PASSWORD_ERROR);
            }
        } else {
            throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
        }
    }

    @PostMapping("/login/reader")
    @ResponseBody
    public ResponseMessage login(@RequestBody Reader reader, HttpServletResponse response) throws UnsupportedEncodingException {
        String email = reader.getEmail();
        String password = reader.getPassword();

        Reader one = readerService.findByEmail(email);
        if (one != null) {
            if (one.getPassword().equals(password)) {
                String jwtToken = JwtToken.createToken(email, one.getUserName(), "reader");
                Cookie jwtTokenCookie = new Cookie("token", jwtToken);
                jwtTokenCookie.setMaxAge(60 * 60 * 12);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
                return ResultUtil.successNoData(request.getRequestURL().toString());
            } else {
                throw new UserDefinedException(ResultEnum.PASSWORD_ERROR);
            }
        } else {
            throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
        }
    }
}
