package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.ReaderFineRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 22:49
 */
@Repository
public interface ReaderFineRecordRepository extends JpaRepository<ReaderFineRecord,Integer>{
}
