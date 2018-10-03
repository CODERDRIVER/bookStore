package com.lyears.projects.bookstore;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Order;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.repository.BookRepository;
import com.lyears.projects.bookstore.repository.BorrowRepository;
import com.lyears.projects.bookstore.repository.OrderRepository;
import com.lyears.projects.bookstore.repository.ReaderRepository;
import com.lyears.projects.bookstore.service.BorrowAndOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookStoreApplicationTests {

    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BorrowRepository borrowRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testSave() {
        Reader reader = new Reader();
        reader.setEmail("fan@qq.com");
        reader.setPassword("123456");
        reader.setDeposit(300d);
        reader.setUserName("fan");
        readerRepository.save(reader);
        Book book = new Book();
        book.setAuthor("吴承恩");
        book.setBookName("西游记");
        book.setBookType("小说");
        book.setPrice(12d);
        book.setBarCode("123456");
        book.setInventory(20);
        bookRepository.save(book);
    }

    @Test
    public void testSaveBorrow() {

        Book book = bookRepository.findOne(1);
        Reader reader = readerRepository.findOne(2);


        Borrow borrow1 = new Borrow();

        borrow1.setBook(book);
        borrow1.setReader(reader);
        borrow1.setBorrowDate(LocalDate.now());
        borrow1.setBorrowStatus(true);
        borrow1.setReturnDate(LocalDate.of(2018, 10, 4));

        book.getBorrows().add(borrow1);
        reader.getBorrows().add(borrow1);
        reader.setBorrowNum(reader.getBorrowNum() - 1);
        book.setInventory(book.getInventory() - 1);

        bookRepository.save(book);
        readerRepository.save(reader);
        borrowRepository.save(borrow1);
    }

    @Test
    public void testOrder() {

            Book book = bookRepository.findOne(1);
            Reader reader = readerRepository.findOne(1);

            Order order = new Order();
            order.setBook(book);
            order.setReader(reader);

            order.setOrderStatus(true);

            book.getOrders().add(order);
            reader.getOrders().add(order);

            orderRepository.save(order);
    }

    @Test
    public void testGet() {
        List<Borrow> borrows = borrowRepository.getAllByBorrowDate(LocalDate.now());
    }

    @Test
    public void testReturn() {
        borrowAndOrderService.returnBookWithReaderName("西游记", "wan");
    }

    @Test
    public void testCancel() {
        borrowAndOrderService.cancelOrderWithTime(LocalDateTime.now().plusHours(3));
    }


}
