package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description 书籍删除记录表
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 17:35
 */
@Data
@Entity
@Table(name = "book_delete_record")
public class BookDeleteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer bookId;     //所删除书的id
    private Integer librarainId;    //删除书籍的图书管理员id
    private String bookName;    // 删除书的名字

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteDate;    // 删除日期
}
