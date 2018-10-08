package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fzm
 * @date 2018/10/8
 **/
@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;
    @Autowired
    private HttpServletRequest request;

    @GetMapping("/accounts")
    @ResponseBody
    public ResponseMessage getAllAccount(){

        return ResultUtil.success(adminService.findAllAccount(),request.getRequestURL().toString());
    }
}
