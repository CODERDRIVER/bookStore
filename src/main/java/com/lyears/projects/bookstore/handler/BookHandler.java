package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.service.BookDeleteRecordService;
import com.lyears.projects.bookstore.service.BookService;
import com.lyears.projects.bookstore.service.FtpService;
import com.lyears.projects.bookstore.util.AuthUtil;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/4
 **/
@RestController
public class BookHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private FtpService ftpService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDeleteRecordService bookDeleteRecordService;

    @GetMapping("/books")
    public ResponseMessage getAllBooks(@RequestParam(name = "pageNo", required = false, defaultValue = "1")
                                               String pageNoStr,
                                       @RequestParam(name = "pageSize", defaultValue = "10") String pageSizeStr,
                                       @RequestParam(name = "keyStr", defaultValue = "", required = false) String keyStr) {
        int pageNo = 1;
        int pageSize = 10;

        try {
            pageNo = Integer.parseInt(pageNoStr);
            pageSize = Integer.parseInt(pageSizeStr);
            if (pageNo < 1) {
                pageNo = 1;
            }
        } catch (Exception ignored) {
        }
        Page<Book> bookPage;
        if ("".equals(keyStr)) {
            bookPage = bookService.getAllBooks(pageNo, pageSize);
        } else {
            bookPage = bookService.getAllBooks(pageNo, pageSize, keyStr);
        }
        return ResultUtil.success(avoidStackOverflowInReader(bookPage),
                request.getRequestURL().toString());
    }

    /**
     *  获得所有可以借阅的书
     */
    @GetMapping("/borrow/books")
    public ResponseMessage getAllBorrowBook(@RequestParam(name = "pageNo", required = false, defaultValue = "1")
                                                        String pageNoStr,
                                            @RequestParam(name = "pageSize", defaultValue = "10") String pageSizeStr)
    {
        Page<Book> bookPage;
        int pageNo = Integer.parseInt(pageNoStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        if (pageNo<1)
        {
            pageNo = 1;
        }
        bookPage = bookService.getAllBorrowBooks(pageNo,pageSize);
        return ResultUtil.success(avoidStackOverflowInReader(bookPage),request.getRequestURL().toString());
    }


    @PostMapping("/book/add")
    public ResponseMessage addNewBook(@RequestBody Book book){
        System.out.println(book.getPubdate());
        bookService.save(book);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 使用Stream避免栈溢出
     *
     * @param books 传入的bookList
     * @return 得到的结果
     */
    private Page<Book> avoidStackOverflow(Page<Book> books) {
        books.forEach(
                r -> {
                    r.getBorrows().forEach(
                            b -> {
                                b.setBook(null);
                                b.getReader().setBorrows(null);
                                b.getReader().setOrders(null);
                            }
                    );
                    r.getOrders().forEach(
                            b -> {
                                b.setBook(null);
                                b.getReader().setBorrows(null);
                                b.getReader().setOrders(null);
                            }
                    );
                }
        );
        return books;
    }

    /**
     * 使用Stream避免栈溢出,返回给读者时不返回订单和预定数据
     *
     * @param books 传入的bookList
     * @return 得到的结果
     */
    private Page<Book> avoidStackOverflowInReader(Page<Book> books) {
        books.forEach(
                r -> {
                    r.setOrders(null);
                    r.setBorrows(null);
                }
        );
        return books;
    }


    /**
     * 更新书籍图片
     */
    @PostMapping("/book/update/image/")
    public ResponseMessage updateBookImg(MultipartFile multipartFile)
    {
        //进行图片上传
        boolean b = ftpService.uploadFile(multipartFile);
        System.out.println(b);
        //保存图片的url
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 增加 书籍所在的位置
     */

    @PostMapping("/boo/add/position/{bookId}")
    public ResponseMessage addBookPosition(@PathVariable("bookId")int bookId,String position,@CookieValue(value = "token") String token)
    {
        //需要判断当前用户是否是图书管理员
        if (!AuthUtil.isLibrarian(token))
        {
             return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }
        //有权利
        //查询当前用户
        Book bookById = bookService.findBookById(bookId);
        if (bookById!=null)
        {
            bookById.setLocation(position);
            bookService.save(bookById);
            return ResultUtil.successNoData(request.getRequestURL().toString());
        }else{
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST,request.getRequestURL().toString());
        }
    }

    /**
     * 编辑书籍的位置
     */

    @PostMapping("/book/edit/location/{bookId}")
    public ResponseMessage updateBookPosition(@PathVariable("bookId")int bookId,String position,@CookieValue("token")String token)
    {
        //判断当前用户是否是管理员
        if (!AuthUtil.isLibrarian(token))
        {
            return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }
        int status = bookService.updateBookPosition(bookId, position);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  删除书籍的位置
     */

    public ResponseMessage deleteBookPosition(@PathVariable("bookId")int bookId,@CookieValue("token")String token)
    {
        if (AuthUtil.isLibrarian(token))
        {
            return ResultUtil.error(ResultEnum.NO_RIGHT,request.getRequestURL().toString());
        }
        int status = bookService.updateBookCategoryById(bookId,"");
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  获取书的删除记录列表
     */

    @RequestMapping(value = "/book/deleteRecords",method = RequestMethod.GET)
    public ResponseMessage getAllDeleteRecords()
    {
        return ResultUtil.success(bookDeleteRecordService.findAllRecord(),request.getRequestURL().toString());
    }
}
