package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author fzm
 * @date 2018/2/25
 **/
public interface ReaderRepository extends JpaRepository<Reader,Integer> {

    Reader findByUserName(String lastName);

    Reader findByEmail(String email);

    //设置读者创建账户时所要缴纳的保证金
    @Query(nativeQuery = true,value = "UPDATE constants as c set c.deposit = :deposit")
    int updateReaderDeposit(@Param("deposit")double deposit);


}
