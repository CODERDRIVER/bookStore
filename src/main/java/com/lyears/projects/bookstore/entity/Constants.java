package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description 常量表
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 11:03
 */
@Data
@Entity
@Table(name = "constants")
public class Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private double fine = 1;    //罚金

    private Integer returnPeriod= 30;   //归还期限

    private Double deposit; //保证金

    private Integer totalBorrowNums;    //总共可以借阅的数量

}
