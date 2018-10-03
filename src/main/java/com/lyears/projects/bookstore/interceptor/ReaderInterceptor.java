package com.lyears.projects.bookstore.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.ReaderService;
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
public class ReaderInterceptor implements HandlerInterceptor {

    @Autowired
    private ReaderService readerService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null) {
            //未登录，返回异常
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
        } else {
            String token = cookies[0].getValue();
            Map<String, Claim> claims = JwtToken.verifyToken(token);

            String readerType = "reader";
            return InterceptorUtil.loginAuthentication(httpServletRequest, claims, readerType,
                    readerService.findByEmail(claims.get("email").asString()) == null);
        }
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
