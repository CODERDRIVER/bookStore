package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Order;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.repository.BorrowRepository;
import com.lyears.projects.bookstore.repository.OrderRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import com.lyears.projects.bookstore.util.ResultEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fzm
 * @date 2018/2/26
 **/
@Service
public class BorrowAndOrderService {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveBorrowInfoWithBookAndReader(String bookName, String readerName, Borrow borrow) {
        Book book = bookRepository.findByBookName(bookName);
        Reader reader = readerRepository.findByUserName(readerName);

        book.getBorrows().add(borrow);
        reader.getBorrows().add(borrow);
        //此时reader的借书量减一
        reader.setBorrowNum(reader.getBorrowNum() - 1);
        if (reader.getBorrowNum() < 0) {
            throw new UserDefinedException(ResultEnum.ERROR_NUMBER);
        }
        //新建借书项
        borrow.setBorrowStatus(true);
        borrow.setBook(book);
        borrow.setReader(reader);
        borrowRepository.save(borrow);

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveOrderInfoWithBookAndReader(String bookName, String readerName) {
        Order order = new Order();
        Book book = bookRepository.findByBookName(bookName);
        Reader reader = readerRepository.findByUserName(readerName);

        book.getOrders().add(order);
        reader.getOrders().add(order);

        //新建预定项
        order.setOrderStatus(true);
        order.setBook(book);
        order.setReader(reader);
        readerRepository.save(reader);
        orderRepository.save(order);
    }
}
