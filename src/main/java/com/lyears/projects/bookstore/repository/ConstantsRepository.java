package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:13
 */
@Repository
public interface ConstantsRepository extends JpaRepository<Constants,Integer>{

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE constants AS c SET c.fine = :fine")
    @Transactional
    void updateBookFine(@Param("fine") double fine);

    //更改所有书籍的归还期限
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE constants AS c SET c.return_period = :days")
    @Transactional
    void updateBookReturnDate(@Param("days") int days);
}
