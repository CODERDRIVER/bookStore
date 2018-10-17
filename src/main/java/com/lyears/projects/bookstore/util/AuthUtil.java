package com.lyears.projects.bookstore.util;

import com.auth0.jwt.interfaces.Claim;
import com.lyears.projects.bookstore.jwt.JwtToken;

import java.util.Map;

/**
 * @Description 权限管理工具类
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 15:47
 */
public class AuthUtil {

    /**
     *  判断当去年用户是否是图书管理员
     */
    public static boolean isLibrarian(String token)
    {
        Map<String, Claim> claims = JwtToken.verifyToken(token);
        String type = claims.get("type").asString();
        System.out.println(type);
        return type.equals("librarian");
    }


    /**
     *  判断是否是超级管理员
     */
    public static boolean isAdmin(String token)
    {
        Map<String, Claim> claims = JwtToken.verifyToken(token);
        return claims.get("type").asString().equals("admin");
    }

}
