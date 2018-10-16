package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Reader;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/9/30
 **/
@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    List<Borrow> getAllByBorrowDate(LocalDate date);

    List<Borrow> getAllByReader(Reader reader);

    Borrow getByBookAndAndReaderAndAndBorrowStatus(Book book, Reader reader, boolean status);

    /**
     *  根据读者id 查询所有借阅信息
     */
    @Query(nativeQuery = true,value = "SELECT * from book_borrow as b WHERE b.reader_id = :readerId ")
    List<Borrow> findBorrowsByReaderId(@Param("readerId") int readerId);

    /**
     *  查询所有过期书籍
     */
    List<Borrow> findAllByReturnDateBetween(LocalDate pre,LocalDate next);

}
