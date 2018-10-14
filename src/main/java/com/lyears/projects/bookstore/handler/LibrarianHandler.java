package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.entity.Income;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.entity.BookDeleteRecord;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.*;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        return null;
    }

    /**
     * 更新书籍信息，除了图书图片
     */
    @ResponseBody
    @PostMapping("/book/update/profile")
    public ResponseMessage updateBookInfo(@RequestBody Book book)
    {
        bookService.save(book);
        return null;
    }

    /**
     * 图书管理员能够删除读者账户
     */
    @ResponseBody
    @DeleteMapping("/reader/{id}")
    public ResponseMessage deleteReader(@PathVariable(value = "id")int readerId)
    {
        // 如果要删除读者账号，读者账号内的罚金必须被缴纳且为归还的书籍必须归还
        return readerService.deleteReaderById(readerId);
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
        List<Income> dailyRecord = incomeService.getDailyRecord(type);
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

}
