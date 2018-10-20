package com.lyears.projects.bookstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Description 书籍归还记录返回数据
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/15 20:46
 */
@Data
public class BookReturnRecordData {

    private int bookId;
    private String bookName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    private Integer returnId;
    private Integer readerId;
    private double fine;    //罚金数
}
