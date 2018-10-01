package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.service.BorrowService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/1
 **/
@Controller
public class BorrowHandler {

    @Autowired
    private BorrowService borrowService;

    @PostMapping(value = "/borrow",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage addNewBorrow(HttpServletRequest request,
                                        @RequestParam("bookName") String bookName,
                                        @RequestParam("readerName") String readerName,
                                        Borrow borrow) {

        borrowService.saveBorrowInfoWithBookAndReader(bookName, readerName, borrow);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }
}
