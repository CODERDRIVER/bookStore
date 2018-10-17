package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
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

    @GetMapping(value = "/readers")
    @ResponseBody
    public ResponseMessage getAllReaders() {

        return ResultUtil.success(avoidStackOverflow(readerService.getAllReaders()),
                request.getRequestURL().toString());
    }

    /**
     * 注册读者
     * @param reader    传入的读者信息
     * @return  返回结果
     */
    @PostMapping(value = "/add/reader")
    @ResponseBody
    public ResponseMessage saveReader(@RequestBody Reader reader, @CookieValue(value = "token")Cookie token) {
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
        readerService.save(reader);
        // 查询当前的保证金是多少元
        double deposit = constantsService.getDeposit();
        incomeService.addIncome(deposit);

        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  查看当前待支付罚金
     *
     */
    @GetMapping("/reader/unpaidFine")
    @ResponseBody
    public ResponseMessage getUnpaidFine(@CookieValue(value = "readerId")Cookie readerId)
    {
        Reader one = readerService.findOne(readerId.getVersion());
        return ResultUtil.success(one,request.getRequestURL().toString());
    }

    /**
     * 书籍预约功能
     */

    @RequestMapping(value = "/book/order",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage orderBook(@RequestBody String bookName,@CookieValue("readerId")Cookie readerId)
    {
        if (readerId==null)
        {
            ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }else{

            // 保存一条预约记录
           ResponseMessage responseMessage =  borrowAndOrderService.orderBookWithReaderIdAndBookId(Integer.parseInt(readerId.getValue()),bookName);

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
                    bookService.updateInventoryByBookName(bookName,book.getInventory()+1);
                }
            };
            // 两个小时候后执行
            new Timer().schedule(timerTask,1000*60*60*2);
            return responseMessage;
        }
        return null;
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
