package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Administrator;
import com.lyears.projects.bookstore.entity.Librarian;
import com.lyears.projects.bookstore.service.AdminService;
import com.lyears.projects.bookstore.service.LibrarianService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/8
 **/
@Controller
@RequestMapping("/admin")
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private LibrarianService librarianService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 查询所有的图书管理员列表
     * @return
     */
    @GetMapping("/librarians")
    @ResponseBody
    public List<Librarian> getAllLibrarians()
    {
        List<Librarian> allLibralians = librarianService.getAllLibralians();
        return allLibralians;
    }


    @GetMapping("/accounts")
    @ResponseBody
    public ResponseMessage getAllAccount(){

        return ResultUtil.success(adminService.findAllAccount(),request.getRequestURL().toString());
    }

    /**
     *  修改书籍逾期罚金
     */
    @RequestMapping(value = "/book/fine",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateBookFine(@RequestBody String fine)
    {
        System.out.println(fine);
        fine = fine.split("=")[1];
        double f = Double.parseDouble(fine);
        adminService.updateBookFine(f);
        //判断status 的状态，返回相应的结果
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 设置书籍归还期限，以天为单位
     * @param days
     * @return
     */
    @RequestMapping(value = "/book/returnDate",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateBookReturnDate(@RequestBody String days)
    {
        days = days.split("=")[1];
        adminService.updateBookReturnDate(Integer.parseInt(days));
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 修改读者创建账户时需要缴纳的保证金
     * @param deposit
     * @return
     */
    @RequestMapping(value = "/reader/deposit",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage updateReaderDeposit(double deposit)
    {
        int status = adminService.updateReaderDeposit(deposit);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 根据邮箱修改该账户的密码
     * @param email
     * @param newPassword
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/profile",method = RequestMethod.POST)
    public ResponseMessage updatePassword(String email,String newPassword)
    {
        // 首先查询该邮箱的用户是否存在
        Administrator byEmail = adminService.findByEmail(email);
        if (byEmail==null)
        {
            return ResultUtil.error(ResultEnum.USER_NOT_EXIST,request.getRequestURL().toString());
        }
        //更新密码
        int status = adminService.updatePasswordByEmail(email, newPassword);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 超级管理员对图书管理员账号进行编辑
     */
    @ResponseBody
    @RequestMapping(value = "/edit/librarian/",method = RequestMethod.POST)
    public ResponseMessage editLibrarianProfile(Librarian librarian)
    {
        if (librarian==null)
        {
            //异常处理
        }
        librarianService.save(librarian);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 对图书管理员账号进行删除
     */
    @ResponseBody
    @RequestMapping(value = "/delete/librarain",method = RequestMethod.POST)
    public ResponseMessage deleteLibrarianByEmail(String email)
    {
        //
        librarianService.deleteByEmail(email);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     *  对图书管理员账号进行删除 根据id
     */
    @ResponseBody
    @RequestMapping(value = "/delete/librarian/id/",method = RequestMethod.DELETE)
    public ResponseMessage deleteLibralianById(@RequestBody String ids)
    {
        // 获得数据
        ids = ids.split("=")[1];
        String[] id = ids.split("%2C");
        librarianService.deleteById(id);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }



}
