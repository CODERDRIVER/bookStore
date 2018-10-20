package com.lyears.projects.bookstore.model;

import lombok.Data;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/20 22:39
 */
@Data
public class ReaderData {

    private Integer readerId;
    private String email;
    private String userName;
    private String password;
    private Integer borrowNum;
    private String phoneNumber;    //电话号码
    private double paidFine; //已经交过的罚金
    private double unPaidFine; //未交过的罚金
}
