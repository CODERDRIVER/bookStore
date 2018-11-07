package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @Description 书籍的位置表
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:19
 */
@Entity
@Table(name = "book_location")
@Data
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
}
