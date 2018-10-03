package com.lyears.projects.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * @author fzm
 * @date 2018/9/30
 **/
@Entity
@Table(name = "book_borrow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"reader_id", "book_id"})
})
public class Borrow {


    public interface IdView{}
    public interface BorrowTimeView extends IdView{}
    public interface ReturnTimeView extends BorrowTimeView{}
    public interface StatusView extends ReturnTimeView{}

    public interface ReaderView extends StatusView{}
    public interface BookView extends ReaderView{}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer borrowId;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "reader_id")
    private Reader reader;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate borrowDate = LocalDate.now();
    /**
     * 用户借书默认一个月还
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate = this.getBorrowDate().plusMonths(1);

    private Boolean borrowStatus;


    @JsonView(IdView.class)
    public Integer getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Integer borrowId) {
        this.borrowId = borrowId;
    }

    @JsonView(ReaderView.class)
    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    @JsonView(BookView.class)
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @JsonView(BorrowTimeView.class)
    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    @JsonView(ReturnTimeView.class)
    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @JsonView(StatusView.class)
    public Boolean getBorrowStatus() {
        return borrowStatus;
    }

    public void setBorrowStatus(Boolean borrowStatus) {
        this.borrowStatus = borrowStatus;
    }
}
