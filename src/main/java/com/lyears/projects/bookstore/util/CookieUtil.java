package com.lyears.projects.bookstore.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/19 08:58
 */
public class CookieUtil {

    /**
     *  创建一个cookie
     * @param key
     * @param value
     * @param expire
     * @param path
     * @param response
     */
    public static void createCookie(String key, String value, int expire, String path, HttpServletResponse response)
    {
        Cookie jwtTokenCookie = new Cookie(key,value);
        jwtTokenCookie.setMaxAge(expire);
        jwtTokenCookie.setPath(path);
        response.addCookie(jwtTokenCookie);
    }
}
