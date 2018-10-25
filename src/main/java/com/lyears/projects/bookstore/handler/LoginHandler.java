package com.lyears.projects.bookstore.handler;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.model.PasswordForget;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.service.LibrarianService;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
    private JavaMailSender javaMailSender;

    @Autowired
    private HttpServletRequest request;
    public static final String FROM = "1187697635@qq.com";

    @PostMapping("/login/admin")
    @ResponseBody
    public ResponseMessage login(@RequestBody Administrator administrator, HttpServletResponse response) throws UnsupportedEncodingException {
        String email = administrator.getEmail();
        String password = administrator.getPassword();

        Administrator one = adminService.findByEmail(email);
        System.out.println(one);
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
        if (one.getStatus()!=0)
        {
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST,request.getRequestURL().toString());
        }
        if (one != null) {
            if (one.getPassword().equals(password)) {
                String jwtToken = JwtToken.createToken(email, one.getUserName(), "reader");
                Cookie jwtTokenCookie = new Cookie("token", jwtToken);
                jwtTokenCookie.setMaxAge(60 * 60 * 12);
                jwtTokenCookie.setPath("/");
                response.addCookie(jwtTokenCookie);
                Cookie readerCookie = new Cookie("readerId",one.getReaderId()+"");
                readerCookie.setMaxAge(60*60*12);
                readerCookie.setPath("/");
                response.addCookie(readerCookie);
                return ResultUtil.successNoData(request.getRequestURL().toString());
            } else {
                throw new UserDefinedException(ResultEnum.PASSWORD_ERROR);
            }
        } else {
            throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
        }
    }


    /**
     *读者登录，手机号
     */
    @PostMapping("/login/reader/phoneNumber")
    @ResponseBody
    public ResponseMessage loginWithPhone(@RequestBody Reader reader,HttpServletResponse response)
    {
        String phone  = reader.getPhoneNumber();
        String password = reader.getPassword();
        Reader one = readerService.findByPhoneNumber(phone);
        if (one.getStatus()!=0)
        {
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST,request.getRequestURL().toString());
        }
        if (one!=null)
        {
            if (one.getPassword()!=null&&one.getPassword().equals(password))
            {
                // 说明手机号和密码都是正确的
                String jwtToken = JwtToken.createToken(phone,one.getUserName(),"reader");
                CookieUtil.createCookie("token",jwtToken,60*60*12,"/",response);
                CookieUtil.createCookie("readerId",one.getReaderId()+"",60*60*12,"/",response);
                return ResultUtil.successNoData(request.getRequestURL().toString());
            }
        }else{
            return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }
        return null;
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
                Cookie librarianCookie = new Cookie("librarianId",one.getId()+"");
                librarianCookie.setMaxAge(60 * 60 * 12);
                librarianCookie.setPath("/");
                response.addCookie(librarianCookie);
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
            String type = JwtToken.verifyToken(token.getValue()).get("type").asString();
            Cookie temp = new Cookie(type+"Id","");
            temp.setMaxAge(0);
            // 删除相对应的cookie
            response.addCookie(temp);
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

    /**
     *  忘记密码，通过邮箱发送密码
     */
    @PostMapping(value = "/send/password")
    @ResponseBody
    public ResponseMessage forgetPassword(@RequestBody PasswordForget passwordForget)
    {
        String email = passwordForget.getEmail();
        String type = passwordForget.getType();
        System.out.println(email);
        if (email==null||type==null)
        {
            return ResultUtil.error(ResultEnum.FAILURE,request.getRequestURL().toString());
        }
        String password = "";
        switch (type)
        {
            case "admin":password = adminService.findByEmail(email).getPassword();break;
            case "librarian":password = librarianService.findByEmail(email).getPassword();break;
            case "reader":password = readerService.findByEmail(email).getPassword();break;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(email);
        message.setSubject("主题：密码找回");
        message.setText("你的密码为"+password);
        javaMailSender.send(message);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }
}
