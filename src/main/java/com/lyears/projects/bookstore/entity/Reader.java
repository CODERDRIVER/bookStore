package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.HashSet;
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
    private Integer readerId;
    @Column(unique = true)
    private String email;
    private String userName;
    private String password;
    private Integer borrowNum = 3;
    private Double deposit;
    @OneToMany(mappedBy = "reader", targetEntity = Borrow.class)
    private Set<Borrow> borrows = new HashSet<>();
    @OneToMany(mappedBy = "reader", targetEntity = Order.class)
    private Set<Order> orders = new HashSet<>();

    @Override
    public String toString() {
        return "Reader{" + "ReaderId=" + readerId +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", borrowNum=" + borrowNum +
                ", deposit=" + deposit +
                ", borrows=" + borrows +
                '}';
    }

    @JsonView(IdView.class)
    public Integer getReaderId() {
        return readerId;
    }

    public void setReaderId(Integer readerId) {
        this.readerId = readerId;
    }

    @JsonView(EmailView.class)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonView(NameView.class)
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

    @JsonView(NumView.class)
    public Integer getBorrowNum() {
        return borrowNum;
    }

    public void setBorrowNum(Integer borrowNum) {
        this.borrowNum = borrowNum;
    }

    @JsonView(DepositView.class)
    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    @JsonView({BorrowView.class, Borrow.StatusView.class})
    public Set<Borrow> getBorrows() {
        return borrows;
    }

    public void setBorrows(Set<Borrow> borrows) {
        this.borrows = borrows;
    }

    @JsonView(OrderView.class)
    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public interface IdView {
    }

    public interface EmailView extends IdView {
    }

    public interface NameView extends EmailView {
    }

    public interface NumView extends NameView {
    }

    public interface DepositView extends NumView {
    }

    public interface BorrowView extends DepositView {
    }

    public interface OrderView extends DepositView {
    }
}
