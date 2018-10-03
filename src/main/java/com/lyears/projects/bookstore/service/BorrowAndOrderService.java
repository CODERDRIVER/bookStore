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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * 根据书名和读者名借书
     *
     * @param bookName   书名
     * @param readerName 读者名
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveBorrowInfoWithBookAndReader(String bookName, String readerName) {
        Borrow borrow = new Borrow();
        Book book = bookRepository.findByBookName(bookName);
        Reader reader = readerRepository.findByUserName(readerName);

        book.getBorrows().add(borrow);
        reader.getBorrows().add(borrow);
        //此时reader的借书量减一
        reader.setBorrowNum(reader.getBorrowNum() - 1);
        if (reader.getBorrowNum() < 0) {
            throw new UserDefinedException(ResultEnum.ERROR_NUMBER);
        }
        book.setInventory(book.getInventory() - 1);
        if (book.getInventory() < 0) {
            throw new UserDefinedException(ResultEnum.NO_BOOK_STORE);
        }
        //新建借书项
        borrow.setBorrowStatus(true);
        borrow.setBook(book);
        borrow.setReader(reader);
        readerRepository.save(reader);
        bookRepository.save(book);
        borrowRepository.save(borrow);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void returnBookWithReaderName(String bookName, String readerName) {
        Book book = bookRepository.findByBookName(bookName);
        Reader reader = readerRepository.findByUserName(readerName);
        Borrow borrow = borrowRepository.getByBookAndAndReaderAndAndBorrowStatus(book, reader, true);
        if (borrow == null) {
            throw new UserDefinedException(ResultEnum.BORROW_NOT_EXIST);
        } else {
            borrow.setBorrowStatus(false);
            reader.setBorrowNum(reader.getBorrowNum() + 1);
            book.setInventory(book.getInventory() + 1);
            borrowRepository.save(borrow);
        }
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

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void cancelOrderWithTime(LocalDateTime dateTime) {
        List<Order> orders = orderRepository.getAllByReturnDateBeforeAndOrderStatus(dateTime, true);
        orders.forEach(
                order -> order.setOrderStatus(false)
        );
        orderRepository.save(orders);
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public List<Borrow> getTodayBorrow() {
        return borrowRepository.getAllByBorrowDate(LocalDate.now());
    }

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public List<Borrow> getBorrowByReader(Reader reader) {
        return borrowRepository.getAllByReader(reader);
    }
}
