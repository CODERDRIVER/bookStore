package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.BookDeleteRecord;
import com.lyears.projects.bookstore.repository.BookDeleteRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 20:42
 */
@Service
public class BookDeleteRecordService {

    @Autowired
    private BookDeleteRecordRepository recordRepository;

    /**
     * 获取所有的删除记录
     */
    public List<BookDeleteRecord> findAllRecord()
    {
        List<BookDeleteRecord> all = recordRepository.findAll();
        return all;
    }
}
