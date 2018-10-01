package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/2
 **/
@Controller
public class ReaderHandler {

    @Autowired
    private ReaderService readerService;

    @GetMapping(value = "/readers")
    @ResponseBody
    public ResponseMessage getAllReaders(HttpServletRequest request) {
        return ResultUtil.success(readerService.getAllReaders(), request.getRequestURL().toString());
    }
}
