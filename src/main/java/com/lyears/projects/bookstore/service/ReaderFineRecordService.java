package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.ReaderFineRecord;
import com.lyears.projects.bookstore.repository.ReaderFineRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description 读者罚金记录
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 22:49
 */
public class ReaderFineRecordService {

    @Autowired
    private ReaderFineRecordRepository readerFineRecordRepository;


    /**
     *  获得所有 读者罚金记录
     * @return
     */
    public List<ReaderFineRecord> getAllRecords()
    {
        List<ReaderFineRecord> all = readerFineRecordRepository.findAll();
        return all;
    }
}
