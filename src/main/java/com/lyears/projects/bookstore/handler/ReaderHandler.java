package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.BookReturnRecord;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.model.IdManageData;
import com.lyears.projects.bookstore.service.*;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author fzm
 * @date 2018/10/2
 **/
@Controller
public class ReaderHandler {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ConstantsService constantsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private BookReturnRecordService bookReturnRecordService;

    /**
     *  根据readerId 查找Reader信息
     * @param readerId
     * @return
     */
    @GetMapping("/reader/readerId")
    @ResponseBody
    public ResponseMessage findReaderById(@CookieValue("readerId")Cookie readerId)
    {
        return ResultUtil.success(readerService.findOne(Integer.parseInt(readerId.getValue())),request.getRequestURL().toString());
    }

    /**
     *  修改reader 的信息
     * @return
     */

    @PostMapping("/reader/edit")
    @ResponseBody
    public ResponseMessage updateReaderInfo(@RequestBody Reader reader,@CookieValue(value = "readerId",required = false)Cookie readerId)
    {
        System.out.println(reader.toString());
        if (readerId!=null)
        {
            reader.setReaderId(Integer.parseInt(readerId.getValue()));
        }
        readerService.save(reader);
        // 更新数据库
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @GetMapping(value = "/readers")
    @ResponseBody
    public ResponseMessage getAllReaders() {

        return ResultUtil.success(readerService.getAllReaders(),
                request.getRequestURL().toString());
    }

    @GetMapping("/reader/pages/{page}")
    public String readerView(@PathVariable("page")String page)
    {
        return page;
    }
    /**
     * 注册读者
     * @param reader    传入的读者信息
     * @return  返回结果
     */
    @PostMapping(value = "/add/reader")
    @ResponseBody
    public ResponseMessage saveReader(@RequestBody Reader reader, @CookieValue("token")Cookie token) {
        String type = "librarian";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new UserDefinedException(ResultEnum.NO_RIGHT);
        }
        /**
         * 图书馆输入income表，增加deposit元
         */
        reader.setPassword("12345678"); //设置密码
        // 查询当前的保证金是多少元
        double deposit = constantsService.getDeposit();
        incomeService.addIncome(deposit);
        // 设置读者的已付钱数
        reader.setPaidFine(deposit);
        readerService.save(reader);


        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  查看当前待支付罚金
     *
     */
    @GetMapping("/reader/unpaidFine")
    @ResponseBody
    public ResponseMessage getUnpaidFine(@RequestParam(value = "readerId",required = false)String readerId,@CookieValue(value = "readerId",required = false)Cookie readerIdCookie)
    {

        Reader one = null;
        if (readerIdCookie!=null&&!readerIdCookie.getValue().equals(""))
        {
            one = readerService.findOne(Integer.parseInt(readerIdCookie.getValue()));
        }else{
            one = readerService.findOne(Integer.parseInt(readerId));
        }
        one.setBorrows(null);
        one.setOrders(null);
        return ResultUtil.success(one,request.getRequestURL().toString());
    }

    /**
     * 书籍预约功能
     */

    @RequestMapping(value = "/book/order",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseMessage orderBook(@RequestBody String bookName,@CookieValue("readerId")Cookie readerId)
    {
        try {
            // 转码
            bookName = URLDecoder.decode(bookName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(bookName.split("=")[1]);
//        bookName = bookName.split("=")[1];
        final String  name = bookName.split("=")[1];
        if (readerId==null)
        {
            ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }else{

            // 保存一条预约记录
           ResponseMessage responseMessage =  borrowAndOrderService.orderBookWithReaderIdAndBookName(Integer.parseInt(readerId.getValue()),bookName.split("=")[1]);

           // 启动一个定时器，在两个小时后，取消预约
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    // 取消预约
                    // 设置书的状态
                    // 设置预约时的状态
                    // 获得书的id
                    Book book = (Book)responseMessage.getData();
                    int bookId = book.getBookId();
                    bookService.updateBookStatusByBookId(0,bookId); //设置书为正常状态
                    // 库存量增加1
                    bookService.updateInventoryByBookName(name,book.getInventory()+1);
                }
            };
            // 两个小时候后执行
            new Timer().schedule(timerTask,1000*60*60*2);
            return responseMessage;
        }
        return null;
    }

    /**
     *  书籍借阅功能
     */
    @RequestMapping(value = "/book/borrow",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public ResponseMessage borrowBook(@RequestBody String bookName,@CookieValue("readerId")Cookie readerId)
    {
        try {
            // 转码
            bookName = URLDecoder.decode(bookName,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (readerId==null)
        {
            return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }else{
            // 增加一条借阅记录
            ResponseMessage responseMessage = borrowAndOrderService.borrowBookWithBookNameAndReaderId(Integer.parseInt(readerId.getValue()), bookName.split("=")[1]);
            System.out.println(responseMessage);
            return responseMessage;
        }
    }

    /**
     *  书籍归还请求
     */
    @ResponseBody
    @PostMapping("/reader/book/return")
    public ResponseMessage returnBook(@RequestBody IdManageData idManageData,@CookieValue("readerId")Cookie readerId)
    {
         // 增加一条归还记录
        BookReturnRecord bookReturnRecord = new BookReturnRecord();
        bookReturnRecord.setBookId(idManageData.getBookId());
        bookReturnRecord.setBorrowId(idManageData.getBorrowId());
        bookReturnRecord.setReaderId(Integer.parseInt(readerId.getValue()));
        bookReturnRecord.setReturnDate(LocalDate.now());
        bookReturnRecord.setReturnStatus(0);
        bookReturnRecordService.saveReturnRecord(bookReturnRecord);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }
    /**
     * 使用Stream避免栈溢出
     * @param readers 传入的readerList
     * @return 得到的结果
     */
    private List<Reader> avoidStackOverflow(List<Reader> readers) {
        readers.forEach(
                r -> {
                    r.getBorrows().forEach(
                            b -> {
                                b.setReader(null);
                                b.getBook().setBorrows(null);
                                b.getBook().setOrders(null);
                            }
                    );
                    r.getOrders().forEach(
                            b -> {
                                b.setReader(null);
                                b.getBook().setBorrows(null);
                                b.getBook().setOrders(null);
                            }
                    );
                }
        );
        return readers;
    }
}
