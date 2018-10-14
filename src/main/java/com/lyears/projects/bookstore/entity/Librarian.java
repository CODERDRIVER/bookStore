package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Data
@Entity
@Table(name = "librarian")
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String email;
    private String userName;
    @Column(columnDefinition ="varchar(25) default 00010001")
    private String password;

}
