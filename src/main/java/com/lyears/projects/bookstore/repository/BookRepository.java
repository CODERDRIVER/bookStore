package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
     *
     * @param author   书作者
     * @param bookName 书名
     * @param bookType 书类别
     * @param pageable 分页功能
     * @return 书的分页列表
     */
    Page<Book> findAllByAuthorContainingOrBookNameContainingOrBookTypeContaining(String author, String bookName, String bookType, Pageable pageable);

    Book findByBookName(String bookName);

    @Query(nativeQuery = true, value = "UPDATE constants AS c SET c.fine = :fine")
    int updateBookFine(@Param("fine") double fine);

    //更改所有书籍的归还期限
    @Query(nativeQuery = true, value = "UPDATE constants AS c SET b.return_period = :days")
    int updateBookReturnDate(@Param("days") int days);


    //根据id 更新书籍类别
    @Query(nativeQuery = true, value = "UPDATE book AS b SET b.book_type =:category WHERE b.book_id = :id")
    int updateBookCategoryById(@Param("id") int bookId, @Param("category") String category);

    //根据书籍id 更新位置
    @Query(nativeQuery = true,value = "UPDATE book as b SET  b.location = :location WHERE b.book_id = :id")
    int updateBookPositionById(@Param("id")int bookId,@Param("location")String location);
}
