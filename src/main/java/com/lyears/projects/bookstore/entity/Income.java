package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description 收入表
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 08:57
 */
@Data
@Entity
@Table(name = "income")
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer incomeId;

    @Column(columnDefinition ="timestamp")
    private Date time;  //收入的时间

    private Double money;   //收入的钱数

    private int type;   //收入类型  1 为保证金收入  2 为罚金收入
}

