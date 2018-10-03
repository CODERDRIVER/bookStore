package com.lyears.projects.bookstore.interceptor;

import com.lyears.projects.bookstore.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Component
public class ModifyInterceptor implements HandlerInterceptor {

    @Autowired
    private AdminService adminService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //不拦截get方法
        if ("/login/admin".equals(httpServletRequest.getServletPath())) {
            return true;
        }else {
            if (RequestMethod.POST.name().equalsIgnoreCase(httpServletRequest.getMethod()) || RequestMethod.PUT.name().equalsIgnoreCase(httpServletRequest.getMethod())
                    || RequestMethod.DELETE.name().equalsIgnoreCase(httpServletRequest.getMethod())) {
                Cookie[] cookies = httpServletRequest.getCookies();
                return InterceptorUtil.cookieAuthentication(httpServletRequest, cookies, adminService);
            } else {
                return true;
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
