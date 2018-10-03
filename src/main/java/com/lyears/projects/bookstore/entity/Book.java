package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fzm
 * @date 2018/09/25
 **/
@Entity
@Table(name = "book")
public class Book {

    public interface IdView{}
    public interface NameView extends IdView{}
    public interface PriceView extends NameView{}
    public interface AuthorView extends PriceView{}
    public interface TypeView extends AuthorView{}
    public interface BarView extends TypeView{}

    public interface BorrowView extends BarView{}
    public interface OrderView extends BarView{}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer bookId;
    @Column(unique = true)
    private String bookName;
    private Double price;
    private String author;

    private String bookType;
    private String barCode;


    @OneToMany(mappedBy = "book", targetEntity = Borrow.class)
    private Set<Borrow> borrows = new HashSet<>();

    @OneToMany(mappedBy = "book", targetEntity = Order.class)
    private Set<Order> orders = new HashSet<>();

    @JsonView(IdView.class)
    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    @JsonView(NameView.class)
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    @JsonView(PriceView.class)
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonView(AuthorView.class)
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonView(TypeView.class)
    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    @JsonView(BarView.class)
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    @JsonView(BorrowView.class)
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
}
