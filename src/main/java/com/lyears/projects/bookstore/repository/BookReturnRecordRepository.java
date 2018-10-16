package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.BookReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 22:45
 */
@Repository
public interface BookReturnRecordRepository extends JpaRepository<BookReturnRecord,Integer>{

    /**
     *  根据 读者id 查询所有的归还记录
     */
    public List<BookReturnRecord> findAllByReaderId(int readerId);
}
