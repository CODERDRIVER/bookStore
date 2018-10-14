package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.BookReturnRecord;
import com.lyears.projects.bookstore.repository.BookReturnRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 22:46
 */
@Service
public class BookReturnRecordService {

    @Autowired
    private BookReturnRecordRepository bookReturnRecordRepository;


    /**
     * 返回所有的归还书籍记录
     * @return
     */
    public List<BookReturnRecord> getAllRecords()
    {

       return bookReturnRecordRepository.findAll();
    }
}
