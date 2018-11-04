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

    @Column(columnDefinition = "double default 1")
    private double fine = 1;    //罚金

    @Column(columnDefinition = "int default 30")
    private Integer returnPeriod= 30;   //归还期限

    @Column(columnDefinition = "double default 300")
    private Double deposit; //保证金

    @Column(columnDefinition = "int default 3")
    private Integer totalBorrowNums;    //总共可以借阅的数量

}
