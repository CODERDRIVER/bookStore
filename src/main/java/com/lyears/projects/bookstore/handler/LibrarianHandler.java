package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.*;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.model.BorrowData;
import com.lyears.projects.bookstore.model.IdManageData;
import com.lyears.projects.bookstore.model.IncomeData;
import com.lyears.projects.bookstore.service.*;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import com.lyears.projects.bookstore.util.ZxingCodeUtil;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.io.*;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Controller
public class LibrarianHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private BookDeleteRecordService recordService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private BookService bookService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private BookDeleteRecordService bookDeleteRecordService;
    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private BookReturnRecordService bookReturnRecordService;
    /**
     * 注册图书管理员
     *
     * @param librarian 传入的图书管理员信息
     * @return 返回结果
     */
    @PostMapping(value = "/add/librarian")
    @ResponseBody
    public ResponseMessage saveReader(@RequestBody Librarian librarian, @CookieValue(value = "token") Cookie token) {
        String type = "admin";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new UserDefinedException(ResultEnum.NO_RIGHT);
        }
        String email = librarian.getEmail();
        // 判断该邮箱是否已经被注册
        Librarian byEmail = librarianService.findByEmail(email);
        if (byEmail!=null)
        {
            return ResultUtil.error(ResultEnum.EMAIL_EXITS,request.getRequestURL().toString());
        }
        if (librarian.getPassword()==null||librarian.getPassword().equals(""))
        {
            librarian.setPassword("00010001");
        }
        librarianService.save(librarian);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *获得书籍删除记录
     */
    @ResponseBody
    @GetMapping("/book/records")
    public ResponseMessage getAllDeleteRecord()
    {
        /**
         * 需要判断当前用户是否是图书管理员
         */
        List<BookDeleteRecord> allRecord = recordService.findAllRecord();
        //对结果进行处理
        return ResultUtil.success(allRecord,request.getRequestURL().toString());
    }

    /**
     * 更新书籍信息，除了图书图片
     */
    @ResponseBody
    @PostMapping("/book/update/profile")
    public ResponseMessage updateBookInfo(@RequestBody Book book)
    {
        bookService.save(book);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 图书管理员能够删除读者账户
     */
    @ResponseBody
    @DeleteMapping("/reader")
    public ResponseMessage deleteReader(@RequestBody  String readerIds)
    {
        System.out.println(readerIds);
        // 如果要删除读者账号，读者账号内的罚金必须被缴纳且为归还的书籍必须归还
        readerIds = readerIds.split("=")[1];
        String[] ids = readerIds.split("%2C");
        List<ResponseMessage> list = readerService.deleteReaderByIds(ids);
        if (list==null ||list.size() == 0)
        {
            return ResultUtil.errorWithData(list,request.getRequestURI().toString());
        }else{
            return ResultUtil.successNoData(request.getRequestURL().toString());
        }
    }

    /**
     *  图书管理员能够删除图书
     * @param bookIds
     * @return
     */
    @ResponseBody
    @DeleteMapping("/book")
    public ResponseMessage deleteBook(@RequestBody String bookIds,@CookieValue("librarianId")Cookie cookie)
    {
        bookIds = bookIds.split("=")[1];
        for (String bookId:bookIds.split(","))
        {
            boolean b = bookService.deleteBookById(Integer.parseInt(bookId));
            if (!b)
            {
                return ResultUtil.error(ResultEnum.CAN_NOT_DELETE,request.getRequestURL().toString());
            }
            /**
             *  将删除记录添加到数据库中
             *
             */
            BookDeleteRecord bookDeleteRecord = new BookDeleteRecord();
            bookDeleteRecord.setBookId(Integer.parseInt(bookId));
            bookDeleteRecord.setDeleteDate(new Date());
            bookDeleteRecord.setLibrarainId(Integer.parseInt(cookie.getValue()));
            bookDeleteRecordService.saveRecord(bookDeleteRecord);
        }
        return ResultUtil.successNoData(request.getRequestURL().toString());

    }

    /**
     * 添加书籍类别
     *
     */
    @ResponseBody
    @PostMapping("/book/category/{id}")
    public ResponseMessage addBookCategory(@PathVariable("id")Integer bookId,String category)
    {
        Book bookById = bookService.findBookById(bookId);
        bookById.setBookType(category);
        bookService.save(bookById);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 编辑书籍类别
     */
    @ResponseBody
    @PostMapping("/book/edit/category/{id}")
    public ResponseMessage updateBookCategory(@PathVariable("id")Integer bookId,String category)
    {
        int status = bookService.updateBookCategoryById(bookId, category);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 获得图书馆 每日/每周/每月的收入记录
     * @param type
     * @return
     */
    @ResponseBody
    @GetMapping("/libralian/income/records")
    public ResponseMessage getDailyRecord(String type)
    {

        String date = "";
        IncomeData dailyRecord = incomeService.getDailyRecord(type);
        /**
         * 收入记录分为保证金收入和罚金收入
         *TODO 对收入进行区分
         */

        if (dailyRecord!=null)
        {
            return ResultUtil.success(dailyRecord,request.getRequestURL().toString());
        }
        return null;
    }


    /**
     *  图书管理员对借阅图书进行确认
     */

    @ResponseBody
    @PostMapping("/librarian/confirm/bookBorrow")
    public ResponseMessage confirmBookBorrow(@RequestBody IdManageData idManageData,@CookieValue("librarianId")Cookie librarianId)
    {

        // 权限判定 是否是管理员
        /**
         *  确认借阅需要发生的事件
         *  1. borrow_status 状态变为借阅成功
         *  2. reader 不发生变化
         *  3. book 不发生变化
         */
        int borrowId = idManageData.getBorrowId();
        // 设置 borrowId 中 borrow_status  设置为借阅成功
        Borrow byId = borrowAndOrderService.findById(borrowId);
        byId.setBorrowStatus(1);    // 设置为借阅成功
        borrowAndOrderService.updateBorrowInfo(byId);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  图书管理员拒绝图书借阅
     */
    @ResponseBody
    @PostMapping("/librarian/reject/bookBorrow")
    public ResponseMessage rejcetBookBorrow(@RequestBody IdManageData idManageData,@CookieValue("librarianId")Cookie librarianId)
    {
        /**
         *  拒绝借阅发生的事件
         *  1. borrow_status 状态发生变化
         *  2. reader 的borrow_num 发生变化
         *  3. book 的 status  和 inventory 发生变化
         */
        return librarianService.rejectBookBorrow(idManageData);
    }

    /**
     *  图书管理员对预约图书进行确认
     */
    @ResponseBody
    @PostMapping("/librarian/confirm/bookOrder")
    public ResponseMessage confirmBookOrder(@RequestBody IdManageData idManageData)
    {
        /**
         *  更新 order_status 的状态
         */
        int orderId = idManageData.getOrderId();
        Order byOrderId = borrowAndOrderService.findByOrderId(orderId);
        byOrderId.setOrderStatus(1);
        borrowAndOrderService.updateOrderInfo(byOrderId);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @ResponseBody
    @PostMapping("/librarian/reject/bookOrder")
    public ResponseMessage rejectBookOrderr(@RequestBody IdManageData idManageData)
    {
        /**
         *  更新 order_status 的状态
         *  更新 reader 的 borrow_num
         *  更新 book status inventory 数量
         */
        return librarianService.rejectBookOrder(idManageData);
    }

    /**
     *  书籍借阅功能
     */
    @RequestMapping(value = "/book/borrow",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseMessage borrowBook(@RequestBody BorrowData borrowData)
    {
        String bookName = borrowData.getBookName();
        String phoneNumber = borrowData.getPhoneNumber();
        Reader byPhoneNumber = readerService.findByPhoneNumber(phoneNumber);
        Integer readerId = byPhoneNumber.getReaderId();
        if (readerId==null)
        {
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST,request.getRequestURL().toString());
        }else{
            // 增加一条借阅记录
            ResponseMessage responseMessage = borrowAndOrderService.borrowBookWithBookNameAndReaderId(readerId, bookName);
            System.out.println(responseMessage);
            return responseMessage;
        }
    }

    /**
     *  图书管理员对 归还图书进行确认
     */
    @ResponseBody
    @PostMapping("/librarian/book/return")
    public ResponseMessage confirmBookReturn(@RequestBody IdManageData idManageData)
    {
        return librarianService.confirmReturnBook(idManageData);
    }

    /**
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/librarian/upload")
    public ResponseMessage uploadBarCodeImg(@RequestParam("file") MultipartFile multipartfile)
    {
        System.out.println(multipartfile);
        String decode = librarianService.decodeBarCodeImg(multipartfile);
        return ResultUtil.success(decode,request.getRequestURL().toString());
    }

}
