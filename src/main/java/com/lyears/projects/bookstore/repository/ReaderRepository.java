package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/2/25
 **/
public interface ReaderRepository extends JpaRepository<Reader,Integer> {

    Reader findByUserName(String lastName);

    Reader findByEmail(String email);

    Reader findByPhoneNumber(String phoneNumber);

    //设置读者创建账户时所要缴纳的保证金
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE constants as c set c.deposit = :deposit")
    @Transactional
    int updateReaderDeposit(@Param("deposit")double deposit);

    /*
     删除读者
     */
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE reader r SET r.status = 1 WHERE  r.reader_id = :readerId")
    @Transactional
    void deleteById(@Param("readerId")int readerId);


}
