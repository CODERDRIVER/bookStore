package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author fzm
 * @date 2018/2/26
 **/
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Override
    List<Book> findAll();

    @Override
    Page<Book> findAll(Pageable pageable);

    /**
     * 根据传入的字段进行分页模糊查询
     * @param author    书作者
     * @param bookName  书名
     * @param bookType  书类别
     * @param pageable  分页功能
     * @return  书的分页列表
     */
    Page<Book> findAllByAuthorContainingOrBookNameContainingOrBookTypeContaining(String author, String bookName, String bookType, Pageable pageable);

    Book findByBookName(String bookName);
}
