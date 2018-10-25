package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.*;
import com.lyears.projects.bookstore.model.IdManageData;
import com.lyears.projects.bookstore.repository.LibrarianRepository;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import com.lyears.projects.bookstore.util.ZxingCodeUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Service
public class LibrarianService {

    @Autowired
    private LibrarianRepository librarianRepository;

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;
    @Autowired
    private BookReturnRecordService bookReturnRecordService;

    @Autowired
    private HttpServletRequest request;
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    public Librarian findByEmail(String email) {
        return librarianRepository.getByEmail(email);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(Librarian librarian){
        librarianRepository.save(librarian);
    }

    /**
     *  根据图书管理员 id  查询该图书管理员
     */
    public Librarian findLibrarianById(int librarianId)
    {
        return librarianRepository.findOne(librarianId);
    }
    /**
     * 根据图书馆管理员邮箱删除该用户
     */
    public void deleteByEmail(String email)
    {
        Librarian librarian = new Librarian();
        librarian.setEmail(email);
        librarianRepository.delete(librarian);
    }

    /**
     *  查询所有的图书管理员列表
     */

    public List<Librarian> getAllLibralians()
    {
        return librarianRepository.findAll();
    }


    /**
     * 根据id 删除图书管理员
     */
    public void deleteById(String[] ids)
    {
        for (String id:ids)
        {
            librarianRepository.delete(Integer.parseInt(id));
        }
    }

    /**
     *  图书管理员拒绝借阅
     * @param idManageData
     * @return
     */
    public ResponseMessage rejectBookBorrow(IdManageData idManageData)
    {

        /**
         *  拒绝借阅发生的事件
         *  1. borrow_status 状态发生变化
         *  2. reader 的borrow_num 发生变化
         *  3. book 的 status  和 inventory 发生变化
         */
        int borrowId = idManageData.getBorrowId();
        Borrow byId = borrowAndOrderService.findById(borrowId);
        byId.setBorrowStatus(2);    // 设置为借阅失败
        borrowAndOrderService.updateBorrowInfo(byId);
        int readerID = idManageData.getReaderId();
        Reader one = readerService.findOne(readerID);
        one.setBorrowNum(one.getBorrowNum()-1);
        readerService.save(one);
        int bookId = idManageData.getBookId();
        Book bookById = bookService.findBookById(bookId);
        bookById.setStatus(0);  // 设置为正常状态
        // 更新book
        bookService.save(bookById);
        String bookName = bookById.getBookName();
        // 根据名称刚更新bookName的inventory
        bookService.updateInventoryByBookName(bookName,bookById.getInventory()+1);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  图书管理员拒绝预定
     */
    public ResponseMessage rejectBookOrder(IdManageData idManageData)
    {
        int orderId = idManageData.getOrderId();
        Order byOrderId = borrowAndOrderService.findByOrderId(orderId);
        byOrderId.setOrderStatus(2);    // 设置为预约失败
        borrowAndOrderService.updateOrderInfo(byOrderId);

        // 更新读者信息
        int readerId = idManageData.getReaderId();
        Reader one = readerService.findOne(readerId);
        one.setBorrowNum(one.getBorrowNum()-1);
        readerService.save(one);


        // 更新 book 信息

        int bookId = idManageData.getBookId();
        Book bookById = bookService.findBookById(bookId);
        bookById.setStatus(0);  // 设置为正常状态
        bookService.save(bookById);

        // 跟新所有书名的库存量
         bookService.updateInventoryByBookName(bookById.getBookName(),bookById.getInventory()+1);
         return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  图书管理员确认归还记录
     *  读者readerId bookId
     */
    public ResponseMessage confirmReturnBook(IdManageData idManageData)
    {
        String barcode = idManageData.getBarCode();

        Book book =null;
        if (barcode!=null&&!"".equals(barcode))
        {
            book = bookService.findBookByBarCode(barcode);
        }else{
            int bookId = idManageData.getBookId();
            book = bookService.findBookById(bookId);
        }
        /**
         *  查看该书有没有被借阅
         *
         */
        if (book.getStatus()==0)
        {
            return ResultUtil.error(ResultEnum.BOOK_NO_BORROW,request.getRequestURL().toString());
        }
        // 更改读者的borrow_num
        String phoneNumber = idManageData.getPhoneNumber();
        Reader byPhoneNumber = readerService.findByPhoneNumber(phoneNumber);
        byPhoneNumber.setBorrowNum(byPhoneNumber.getBorrowNum()-1);
        readerService.save(byPhoneNumber);

        // 更改书的状态和库存

        book.setStatus(0);
        bookService.save(book);

        bookService.updateInventoryByBookName(book.getBookName(),book.getInventory()+1);

        // 获得与还书记录相关的借阅记录
        Borrow borrowByReaderIdAndBookIdAndBorrowStatus = borrowAndOrderService.findBorrowByReaderIdAndBookIdAndBorrowStatus(byPhoneNumber.getReaderId(), book.getBookId(), 1);
        borrowByReaderIdAndBookIdAndBorrowStatus.setBorrowStatus(3);    //设置为已经归还
        // 保存到数据库
        borrowAndOrderService.updateBorrowInfo(borrowByReaderIdAndBookIdAndBorrowStatus);

        // 增加一条还书记录
        BookReturnRecord bookReturnRecord = new BookReturnRecord();
        bookReturnRecord.setReturnDate(LocalDate.now());
        bookReturnRecord.setReaderId(byPhoneNumber.getReaderId());
        bookReturnRecord.setBookId(book.getBookId());
        bookReturnRecord.setBorrowId(borrowByReaderIdAndBookIdAndBorrowStatus.getBorrowId());
        bookReturnRecordService.saveReturnRecord(bookReturnRecord);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  解析条形码图片
     * @return
     */
    public String decodeBarCodeImg(MultipartFile multipartfile)
    {
        /**
         *  将 multipartFile 转file
         */
//        CommonsMultipartFile commonsmultipartfile = (CommonsMultipartFile) multipartfile;
//        DiskFileItem diskFileItem = (DiskFileItem) commonsmultipartfile.getFileItem();
//        File file = diskFileItem.getStoreLocation();

        String filePath =request.getSession().getServletContext().getRealPath("/")+"upload/";

        File dir = new File(filePath);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        String path = filePath+multipartfile.getOriginalFilename();
        File tempFile = null;
        tempFile = new File(path);
        try {
            FileUtils.copyInputStreamToFile(multipartfile.getInputStream(),tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String decode = ZxingCodeUtil.decode(tempFile);
        System.out.println(decode);
        return decode;
    }

    /**
     *  根据用户名和邮箱进行查询
     * @param key
     * @return
     */
    public List<Librarian> findAllByUsernameOrEmail(String key)
    {
        return librarianRepository.findAllByEmailContainingOrUserNameContaining(key,key);
    }

}
