package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/26
 **/
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Override
    List<Book> findAll();

    Book findByBookName(String bookName);
}
