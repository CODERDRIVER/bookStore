package com.lyears.projects.bookstore.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Controller
public class ViewHandler {

    @GetMapping("/admin")
    public String adminIndex(){
        return "admin";
    }

    @GetMapping("/404")
    public String notFoundPage(){
        return "admin-404";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }




}
