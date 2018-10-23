package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fzm
 * @date 2018/2/25
 **/
@Entity
@Table(name = "reader")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer readerId;
    @Column(unique = true)
    private String email;
    private String userName;
    @Column(columnDefinition = "varchar(25) default 12345678")
    private String password;
    private Integer borrowNum = 0;
    private String phoneNumber;    //电话号码
    @Column(columnDefinition = "double default 0")
    private double paidFine; //已经交过的罚金
    @Column(columnDefinition = "double default 0")
    private double unPaidFine; //未交过的罚金
    @OneToMany(mappedBy = "reader", targetEntity = Borrow.class)
    private Set<Borrow> borrows = new HashSet<>();
    @OneToMany(mappedBy = "reader", targetEntity = Order.class)
    private Set<Order> orders = new HashSet<>();
    private int status = 0 ; // 表示是否注销 0 表示正常 1 表示注销

    @Override
    public String toString() {
        return "Reader{" + "ReaderId=" + readerId +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", borrowNum=" + borrowNum +
                ", borrows=" + borrows +
                '}';
    }
}
