package com.lyears.projects.bookstore.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author fzm
 * @date 2018/9/27
 **/
@Data
@Entity
@Table(name = "administrator")
public class Administrator {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String email;
    private String userName;
    private String password;
}
