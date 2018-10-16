package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.BookReturnRecord;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.model.BookReturnRecordData;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.repository.BookReturnRecordRepository;
import com.lyears.projects.bookstore.repository.BorrowRepository;
import com.lyears.projects.bookstore.repository.ConstantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private ConstantsService constantsService ;
    /**
     * 返回所有的归还书籍记录
     * @return
     */
    public List<BookReturnRecord> getAllRecords()
    {

       return bookReturnRecordRepository.findAll();
    }


    /**
     *  根据读者id 查询所有的书籍归还记录
     */
    public List<BookReturnRecordData> findAllBookReturnRecord(String readerId)
    {
        List<BookReturnRecord> allByReaderId = bookReturnRecordRepository.findAllByReaderId(Integer.parseInt(readerId));
        List<BookReturnRecordData> bookReturnRecordDataList = new ArrayList<>();
        for (BookReturnRecord bookReturnRecord : allByReaderId) {
            BookReturnRecordData bookReturnRecordData = new BookReturnRecordData();
            Integer bookId = bookReturnRecord.getBookId();
            Book one = bookRepository.findOne(bookId);
            bookReturnRecordData.setBookId(bookReturnRecord.getBookId());
            bookReturnRecordData.setBookName(one.getBookName());
            bookReturnRecordData.setReturnDate(bookReturnRecord.getReturnDate());
            // 获得借书时间
            int borrowId = bookReturnRecord.getBorrowId();
            //根据borrowId 查询相对应的borrow记录
            Borrow borrow = borrowRepository.findOne(borrowId);
            LocalDate borrowDate = borrow.getBorrowDate();
            LocalDate returnDate = bookReturnRecord.getReturnDate();
            Period between = Period.between(borrowDate, returnDate);
            int days = between.getDays();

            // 得到借书的期限
            int returnPeriod = constantsService.getRuturnPeriod();
            int diff = 0;
            if (returnPeriod < days)
            {
                // 说明已经超期
                diff = days - returnPeriod;
            }
            bookReturnRecordData.setFine(diff);
            bookReturnRecordDataList.add(bookReturnRecordData);
        }
        return bookReturnRecordDataList;
    }
}
