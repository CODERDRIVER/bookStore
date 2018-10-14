package com.lyears.projects.bookstore.util;

import java.util.UUID;

/**
 * @Description id 生成器
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 17:13
 */
public class IDGenerator {

    public static String getUniqueId()
    {
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();
        String[] params  = id.split("-");
        return params[0]+"-"+params[1]+"-"+params[2];
    }

    public static void main(String[] args) {
        System.out.println(getUniqueId());
    }
}
