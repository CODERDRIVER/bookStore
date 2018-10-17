package com.lyears.projects.bookstore.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * @Description 书籍结余信息归还数据记录
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/17 20:46
 */
@Data
public class BookBorrowRecordData {

    private Integer bookId;

    private String bookName;

    private LocalDate borrowDate;

    private double fine;    //如果有逾期，罚金数

}
