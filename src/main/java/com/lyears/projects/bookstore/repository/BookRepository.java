package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

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

    Page<Book> findAllByInventoryGreaterThanEqual(int inventory,Pageable pageable);

    Page<Book> findBooksByStatus(int status,Pageable pageable);

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

    @Query(nativeQuery = true,value = "SELECT * FROM book as b WHERE b.book_name=:bookName")
    List<Book> findByBookName(@Param("bookName") String bookName);

    @Query(nativeQuery = true,value = "UPDATE book as b SET b.inventory = :inventory WHERE b.book_name = :bookName")
    @Modifying
    @Transactional
    void updateInventoryByBookName(@Param("bookName") String bookName,@Param("inventory") int inventory);


    /**
     *
     * @param bookId
     * @param category
     * @return
     */
    //根据id 更新书籍类别
    @Query(nativeQuery = true, value = "UPDATE book AS b SET b.book_type =:category WHERE b.book_id = :id")
    @Modifying
    @Transactional
    int updateBookCategoryById(@Param("id") int bookId, @Param("category") String category);

    //根据书籍id 更新位置
    @Query(nativeQuery = true,value = "UPDATE book as b SET  b.location = :location WHERE b.book_id = :id")
    @Modifying
    @Transactional
    int updateBookPositionById(@Param("id")int bookId,@Param("location")String location);

    // 根据id 更新bookstatus
    @Query(nativeQuery = true,value = "update book as b set b.status = :stat where b.book_id = :bookId")
    @Modifying
    @Transactional
    void updateStatusByBookId(@Param("stat") int stat,@Param("bookId") int bookId);

    /**
     *  查看某本书的剩余库存
     */
    @Query(value = "SELECT COUNT(*) FROM book as b where b.status = 0 AND  b.book_name=:bookName",nativeQuery = true)
    int findInventoryByBookName(@Param("bookName")String bookName);

    /**
     *  根据barcode 查询书籍
     * @param barCode
     * @return
     */
    Book findBookByBarCode(String barCode);

}
