package com.lyears.projects.bookstore.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
        } else {
            String token = cookies[0].getValue();
            Map<String, Claim> claims = JwtToken.verifyToken(token);
            String userName = claims.get("userName").asString();
            String email = claims.get("email").asString();
            if (email == null) {
                throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            } else {
                if (adminService.findByEmail(email) == null) {
                    throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
                } else {
                    httpServletRequest.setAttribute("userName", userName);
                    return true;
                }
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
