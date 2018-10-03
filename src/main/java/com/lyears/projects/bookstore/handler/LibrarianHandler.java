package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.service.LibrarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

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


}
