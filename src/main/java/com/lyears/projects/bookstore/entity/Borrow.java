package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/9/30
 **/
@Entity
@Table(name = "book_borrow")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer borrowId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate borrowDate = LocalDate.now();
    /**
     * 用户借书默认一个月还
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate = this.getBorrowDate().plusMonths(1);

    private int borrowStatus;   //借阅状态  0 借阅中 1 借阅成功 2 借阅失败 3 已归还





}
