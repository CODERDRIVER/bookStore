package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Reader;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/9/30
 **/
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    List<Borrow> getAllByBorrowDate(LocalDate date);

    List<Borrow> getAllByReader(Reader reader);

    Borrow getByBookAndAndReaderAndAndBorrowStatus(Book book, Reader reader, boolean status);

}
