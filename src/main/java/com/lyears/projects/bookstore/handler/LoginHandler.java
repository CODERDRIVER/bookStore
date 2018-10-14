package com.lyears.projects.bookstore.handler;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.service.LibrarianService;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;

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
    private LibrarianService librarianService;

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

    /**
     * 超级管理员登录
     * @param librarian
     * @param response
     * @return
     */
    @PostMapping("/login/librarian")
    @ResponseBody
    public ResponseMessage login(@RequestBody Librarian librarian, HttpServletResponse response) {
        String email = librarian.getEmail();
        String password = librarian.getPassword();

        Librarian one = librarianService.findByEmail(email);
        if (one != null) {
            if (one.getPassword().equals(password)) {
                String jwtToken = JwtToken.createToken(email, one.getUserName(), "librarian");
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

    @ResponseBody
    @DeleteMapping("/logout")
    public ResponseMessage logout(@CookieValue(name = "token") Cookie token, HttpServletResponse response) {
        if (token == null) {
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
        } else {
            token.setMaxAge(0);
            response.addCookie(token);
            return ResultUtil.successNoData(request.getRequestURL().toString());
        }
    }

    @GetMapping("/login/all")
    public String defaultLogin(@CookieValue(value = "token") String token) {
        Map<String, Claim> claims = JwtToken.verifyToken(token);

        String type = claims.get("type").asString();
        if ("admin".equals(type)) {
            return "redirect:/admin";
        } else if ("reader".equals(type)) {
            return "redirect:/reader";
        } else if ("librarian".equals(type)) {
            return "redirect:/librarian";
        } else {
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
        }
    }
}
