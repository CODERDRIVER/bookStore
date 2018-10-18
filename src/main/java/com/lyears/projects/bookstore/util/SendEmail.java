package com.lyears.projects.bookstore.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @Description 发送邮箱的工具类
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 09:25
 */
public class SendEmail {

    @Autowired
    private static JavaMailSender javaMailSender;

    //qq邮箱服务器
    public static final String FROM = "1187697635@qq.com";
    public static final String PASSWORD = "omzpbrwtziqwjabb";

    public static boolean sendMail(String to,String subject,String content)
    {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(FROM);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
            return true;
        }catch (Exception e)
        {
            return false;
        }

    }

    /**
     *  发送找回密码
     */
    public static void sendPasswordMail(String to,String password)
    {
        String subject = "密码找回";
        sendMail(to,subject,"你的密码为："+password);
    }

}
