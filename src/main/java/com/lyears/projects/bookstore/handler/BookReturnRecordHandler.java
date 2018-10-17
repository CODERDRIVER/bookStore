package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.service.BookReturnRecordService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/12 22:43
 */
@RestController
public class BookReturnRecordHandler {

    @Autowired
    private BookReturnRecordService bookReturnRecordService;

    @Autowired
    private HttpServletRequest request;
    /**
     * 查看所有的归还记录
     */
    @GetMapping("/book/return/records")
    public ResponseMessage getAllRecord(@CookieValue("readerId")Cookie readerId)
    {
        return ResultUtil.success(bookReturnRecordService.findAllBookReturnRecord(readerId.getValue()),request.getRequestURL().toString());
    }
}
