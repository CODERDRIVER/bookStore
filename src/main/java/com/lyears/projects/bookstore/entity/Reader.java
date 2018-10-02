package com.lyears.projects.bookstore.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * @author fzm
 * @date 2018/2/25
 **/
@Entity
@Table(name = "reader")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String email;
    private String userName;
    private String password;


    private Integer borrowNum = 3;
    private Double deposit;

    @OneToMany(mappedBy = "reader", targetEntity = Borrow.class)
    private Set<Borrow> borrows;

    @OneToMany(mappedBy = "reader", targetEntity = Order.class)
    private Set<Order> orders;


    @Override
    public String toString() {
        return "Reader{" + "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", borrowNum=" + borrowNum +
                ", deposit=" + deposit +
                ", borrows=" + borrows +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Set<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(Set<Borrow> borrows) {
        this.borrows = borrows;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
}
