package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Order;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.repository.*;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ConstantsService constantsService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 根据书名和读者名借书
     *
     * @param bookName   书名
     * @param readerName 读者名
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void saveBorrowInfoWithBookAndReader(String bookName, String readerName) {
        Borrow borrow = new Borrow();
        Book book = bookRepository.findByBookName(bookName).get(0);
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

    /**
     *  根据 读者id 和 bookId 保存一条预约记录
     * @param readerId
     * @param bookName
     */
    public ResponseMessage orderBookWithReaderIdAndBookId(int readerId, String bookName)
    {
        /**
         *  1. 需要查询该读者的借阅数量是否已经足够
         *  2. 查询该书的状态
         *  3. 进行借阅
         */
        Reader reader = readerRepository.findOne(readerId);

        // 读者的借阅数量
        Integer borrowNum = reader.getBorrowNum();

        // 获得读者总共可以借阅的数量
        int totalBorrowNums = constantsService.getTotalBorrowNums();

        if (totalBorrowNums == borrowNum)
        {
            // 说明该读者不能借阅书籍
            return ResultUtil.error(ResultEnum.NO_BORROWNUM_LEFT,request.getRequestURL().toString());
        }else{
            // 查询 该书的剩余库存量
            List<Book> byBookName = bookRepository.findByBookName(bookName);
            int counts = byBookName.get(0).getInventory();
            if (counts > 0 )
            {
                // 说明可以借阅/预约
                // 从list 取一本可以借阅的书籍 预约给用户
                for (Book book : byBookName) {
                    if (book.getStatus() == 0)
                    {
                        // 表示正常
                        /**
                         *  设置该书的状态
                         *  增加一条预约记录
                         *  设置该书籍的库存 减一
                         */
                        book.setStatus(2);      // 设置为预约中
                        // 更新数据库
                        bookRepository.save(book);
                        // 将该书名的库存量减一
                        bookRepository.updateInventoryByBookName(bookName,book.getInventory()-1);
                        Order order = new Order();
                        order.setBook(book);
                        order.setReader(reader);
                        order.setOrderStatus(0);    // 预约中
                        order.setOrderDate(LocalDateTime.now());
                        orderRepository.save(order);
                        return ResultUtil.success(book,request.getRequestURL().toString());
                    }
                }
            }else{
                return ResultUtil.error(ResultEnum.NO_BOOK_STORE,request.getRequestURL().toString());
            }
        }
        return null;

    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void returnBookWithReaderName(String bookName, String readerName) {
        Book book = bookRepository.findByBookName(bookName).get(0);
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
        Book book = bookRepository.findByBookName(bookName).get(0);
        Reader reader = readerRepository.findByUserName(readerName);

        book.getOrders().add(order);
        reader.getOrders().add(order);

        //新建预定项
        order.setOrderStatus(0);
        order.setBook(book);
        order.setReader(reader);
        readerRepository.save(reader);
        orderRepository.save(order);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void cancelOrderWithTime(LocalDateTime dateTime) {
        List<Order> orders = orderRepository.getAllByReturnDateBeforeAndOrderStatus(dateTime, 2);
        orders.forEach(
                order -> order.setOrderStatus(0)
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

    /**
     *  根据 读者id 查询所有借阅信息
     */
    public List<Borrow> getBorrowsByReaderId(int readerId)
    {
        return borrowRepository.findBorrowsByReaderId(readerId);
    }

    /**
     * 获得所有的即将过期 borrow 列表
     */
    public List<Borrow> findAllBrrows(LocalDate now)
    {
        LocalDate pre = now.minusDays(1);
        LocalDate next = now.plusDays(1);
        return borrowRepository.findAllByReturnDateBetween(pre,next);
    }
}
