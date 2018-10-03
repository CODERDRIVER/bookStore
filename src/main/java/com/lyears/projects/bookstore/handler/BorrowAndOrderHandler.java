package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.service.BorrowAndOrderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/1
 **/
@Controller
public class BorrowAndOrderHandler {

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private HttpServletRequest request;


    @PostMapping(value = "/borrow", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage addNewBorrow(@RequestParam("bookName") String bookName,
                                        @RequestParam("readerName") String readerName) {

        borrowAndOrderService.saveBorrowInfoWithBookAndReader(bookName, readerName);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage addNewOrder(@RequestParam("bookName") String bookName,
                                       @RequestParam("readerName") String readerName) {

        borrowAndOrderService.saveOrderInfoWithBookAndReader(bookName, readerName);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @GetMapping("/borrows/today")
    @ResponseBody
    public ResponseMessage getTodayBorrows() {
        return ResultUtil.success(borrowAndOrderService.getTodayBorrow(), request.getRequestURL().toString());
    }
}
