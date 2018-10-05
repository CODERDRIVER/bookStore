package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Book;
import com.lyears.projects.bookstore.service.BookService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/4
 **/
@RestController
public class BookHandler {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseMessage getAllBooks(@RequestParam(name = "pageNo", required = false, defaultValue = "1")
                                               String pageNoStr,
                                       @RequestParam(name = "pageSize", defaultValue = "3") String pageSize) {
        int pageNo = 1;
        int pageSizeInt = 3;

        try {
            pageNo = Integer.parseInt(pageNoStr);
            pageSizeInt = Integer.parseInt(pageSize);
            if (pageNo < 1) {
                pageNo = 1;
            }
        } catch (Exception ignored) {

        }
        return ResultUtil.success(avoidStackOverflow(bookService.getAllBooks(pageNo, pageSizeInt)),
                request.getRequestURL().toString());
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
}
