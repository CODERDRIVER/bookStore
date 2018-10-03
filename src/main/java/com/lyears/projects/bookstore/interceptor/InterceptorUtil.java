package com.lyears.projects.bookstore.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.util.ResultEnum;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author fzm
 * @date 2018/10/2
 **/
public class InterceptorUtil {

    /**
     * 检测是否有登录信息
     * @param httpServletRequest 请求request
     * @param cookies   登录信息
     * @param adminService  管理员服务
     * @return  返回是否验证成功
     * @throws Exception    出现异常
     */
    static boolean cookieAuthentication(HttpServletRequest httpServletRequest, Cookie[] cookies, AdminService adminService) throws Exception {
        if (cookies == null) {
            //未登录，返回异常
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
        } else {
            String token = cookies[0].getValue();
            Map<String, Claim> claims = JwtToken.verifyToken(token);
            //拿到管理员用户名
            String adminType = "admin";
            return loginAuthentication(httpServletRequest, claims, adminType,
                    adminService.findByEmail(claims.get("email").asString()) == null);
        }
    }
    static boolean loginAuthentication(HttpServletRequest httpServletRequest, Map<String, Claim> claims , String userType, boolean flag) {
        //拿到用户名
        String userName = claims.get("userName").asString();
        String email = claims.get("email").asString();
        String type = claims.get("type").asString();
        if (userType.equals(type)) {
            if (email == null) {
                throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            } else {
                if (flag) {
                    //数据库中没有此用户名
                    throw new UserDefinedException(ResultEnum.USER_NOT_EXIST);
                } else {
                    httpServletRequest.setAttribute("userName", userName);
                    return true;
                }
            }
        }else{
            throw new UserDefinedException(ResultEnum.NO_RIGHT);
        }
    }
}
