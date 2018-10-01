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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String userName;
    private String password;

}
