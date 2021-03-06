package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Order;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.model.BookBorrowRecordData;
import com.lyears.projects.bookstore.model.BookOrderRecordData;
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
import java.time.Period;
import java.util.ArrayList;
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
    private HttpServletRequest request;

    @Autowired
    private ConstantsService constantsService;

    /**
     *  根据id 查询该条记录
     */
    public Borrow findById(int borrowId)
    {
        return borrowRepository.findOne(borrowId);
    }

    public Order findByOrderId(int orderId)
    {
        return orderRepository.findOne(orderId);
    }
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
        borrow.setBorrowStatus(0);
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
    public ResponseMessage orderBookWithReaderIdAndBookName(int readerId, String bookName)
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
            System.out.println(bookName);
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
                         *  读者的借阅数量加一
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
                        reader.setBorrowNum(reader.getBorrowNum()+1);
                        readerRepository.save(reader);
                        return ResultUtil.successNoData(request.getRequestURL().toString());
                    }
                }
            }else{
                return ResultUtil.error(ResultEnum.NO_BOOK_STORE,request.getRequestURL().toString());
            }
        }
        return null;

    }

    /**
     *  根据读者id,和书名进行借书操作
     * @param readerId
     * @param bookName
     * @return
     */
    public ResponseMessage borrowBookWithBookNameAndReaderId(int readerId,String bookName)
    {
        /**
         *  首先查询该读者能否借书
         */
        Reader one = readerRepository.findOne(readerId);
        int borrowNum = one.getBorrowNum();

        // 得到每个读者能够借书的数量
        int totalBorrowNums = constantsService.getTotalBorrowNums();

        if (borrowNum==totalBorrowNums)
        {
            // 不能借阅
            return ResultUtil.error(ResultEnum.NO_BORROWNUM_LEFT,request.getRequestURL().toString());
        }else{
            // 查询该书的库存量
            System.out.println(bookName);
            List<Book> byBookName = bookRepository.findByBookName(bookName);
            int counts = byBookName.get(0).getInventory();
            if (counts>0)
            {
                // 说明有库存
                // 增加一条借阅查询 并更新书的状态以及书的库存
               for (Book book:byBookName){
                   if (book.getStatus()==0)
                   {
                       // 说明是正常的，能够进行借阅的书
                       book.setStatus(1);  //设置成已借出
                       bookRepository.save(book);

                       //更新库存
                       bookRepository.updateInventoryByBookName(bookName,counts-1);

                       Borrow borrow = new Borrow();
                       borrow.setBook(book);
                       borrow.setBorrowDate(LocalDate.now());
                       borrow.setReader(one);
                       borrow.setBorrowStatus(1);    // 设置为借阅中
                       borrowRepository.save(borrow);
                       one.setBorrowNum(one.getBorrowNum()+1);
                       readerRepository.save(one);
                       return ResultUtil.successNoData(request.getRequestURL().toString());
                   }
               }
            }else{
                return ResultUtil.error(ResultEnum.NO_BOOK_STORE,request.getRequestURL().toString());
            }
        }
        return null;
    }

    /**
     *  获得所有读者的借阅记录  已经借阅成功
     * @return
     */
    public ResponseMessage getAllBorrows(int type)
    {
        List<Borrow> all = null;
        if (type ==1)
        {
            // 查询已经借阅成功的
            all= borrowRepository.findAllByBorrowStatus(1);
        }else if(type==2){
            // 查询所有的借阅信息
            all = borrowRepository.findAll();
        }

        List<BookBorrowRecordData> bookBorrowRecordDatas = new ArrayList<>();
        for (Borrow borrow : all) {
            BookBorrowRecordData bookBorrowRecordData = new BookBorrowRecordData();
            bookBorrowRecordData.setBorrowId(borrow.getBorrowId());
            bookBorrowRecordData.setReaderId(borrow.getReader().getReaderId());
            bookBorrowRecordData.setBorrowDate(borrow.getBorrowDate());
            bookBorrowRecordData.setBookId(borrow.getBook().getBookId());
            bookBorrowRecordData.setBookName(borrow.getBook().getBookName());
            bookBorrowRecordDatas.add(bookBorrowRecordData);
        }
        return ResultUtil.success(bookBorrowRecordDatas,request.getRequestURL().toString());
    }

    /**
     *  获得所有读者的预定记录
     */
    public ResponseMessage getAllOrders()
    {
        List<Order> all = orderRepository.findAllByOrderStatus(0);
        List<BookOrderRecordData> bookOrderRecordDatas = new ArrayList<>();
        for (Order order : all) {
            BookOrderRecordData bookOrderRecordData  = new BookOrderRecordData();
            bookOrderRecordData.setOrderId(order.getOrderId());
            bookOrderRecordData.setBookId(order.getBook().getBookId());
            bookOrderRecordData.setBookName(order.getBook().getBookName());
            bookOrderRecordData.setOrderDate(order.getOrderDate());
            bookOrderRecordData.setReaderId(order.getReader().getReaderId());
            bookOrderRecordData.setBarCode(order.getBook().getBarCode());
            bookOrderRecordData.setPrice(order.getBook().getPrice());
            bookOrderRecordDatas.add(bookOrderRecordData);
        }
        return ResultUtil.success(bookOrderRecordDatas,request.getRequestURL().toString());
    }
    /**
     *
     * @param bookName
     * @param readerName
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void returnBookWithReaderName(String bookName, String readerName) {
        Book book = bookRepository.findByBookName(bookName).get(0);
        Reader reader = readerRepository.findByUserName(readerName);
        Borrow borrow = borrowRepository.getByBookAndAndReaderAndAndBorrowStatus(book, reader, true);
        if (borrow == null) {
            throw new UserDefinedException(ResultEnum.BORROW_NOT_EXIST);
        } else {
            borrow.setBorrowStatus(0);
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
    public List<BookBorrowRecordData> getBorrowsByReaderId(int readerId,int type)
    {
        // 获得所有的 借阅信息
        List<Borrow> borrowsByReaderId = new ArrayList<>();
        if (type==0)
        {
            borrowsByReaderId =  borrowRepository.findBorrowsByReaderIdAndBorrowStatus(readerId,type);
        }else if (type == 1)
        {
            borrowsByReaderId = borrowRepository.findBorrowsByReaderId(readerId);
        }
        List<BookBorrowRecordData> bookBorrowRecordDatas = new ArrayList<>();
        // 查询是否有延期的
        borrowsByReaderId.forEach(borrow -> {
            BookBorrowRecordData bookBorrowRecordData = new BookBorrowRecordData();
            // 获得借阅日期
            LocalDate borrowDate = borrow.getBorrowDate();
            // 初始化今天的时间
            LocalDate now = LocalDate.now();

            // 获得借阅的天数
            Period between = Period.between(borrowDate, now);
            int days = between.getDays();

            // 获得书籍归还的逾期数
            int delayDays = constantsService.getRuturnPeriod();

            if (days>delayDays)
            {
                bookBorrowRecordData.setFine(days-delayDays);
            }
            bookBorrowRecordData.setBorrowDate(borrowDate);
            bookBorrowRecordData.setBookId(borrow.getBook().getBookId());
            bookBorrowRecordData.setBookName(borrow.getBook().getBookName());
            bookBorrowRecordData.setBorrowId(borrow.getBorrowId());
            bookBorrowRecordData.setReaderId(readerId);
            bookBorrowRecordDatas.add(bookBorrowRecordData);

        });
        return bookBorrowRecordDatas;
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

    /**
     *  更新borrow 信息
     */
    public void updateBorrowInfo(Borrow borrow)
    {
        borrowRepository.save(borrow);
    }

    /**
     *  更新 order 信息
     *
     */
    public void updateOrderInfo(Order order)
    {
        orderRepository.save(order);
    }

    public Borrow findBorrowByReaderIdAndBookIdAndBorrowStatus(int readerId,int bookId,int borrowStatus)
    {
        return borrowRepository.findBorrowByReaderIdAndBookIdAndBAndBorrowStatus(readerId,bookId,borrowStatus);
    }
}
