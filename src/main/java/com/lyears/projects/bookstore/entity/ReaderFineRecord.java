package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Description 读者罚金记录
 *  记录哪些读者交过罚金
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 20:00
 */
@Data
@Entity
@Table(name = "reader_fine_record")
public class ReaderFineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer readerId;
}
