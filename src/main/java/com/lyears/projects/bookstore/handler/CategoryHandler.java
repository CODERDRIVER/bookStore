package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Category;
import com.lyears.projects.bookstore.service.CatogoryService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/11/6 19:46
 */
@Controller
public class CategoryHandler {

    @Autowired
    private CatogoryService catogoryService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/categories",method = RequestMethod.GET)
    @ResponseBody
    public ResponseMessage getAllCategory()
    {
        return ResultUtil.success(catogoryService.findAllCategory(),request.getRequestURL().toString());
    }

    @RequestMapping(value = "/add/category",method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage addCategory(@RequestBody Category category)
    {
        catogoryService.addCategory(category);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @ResponseBody
    @RequestMapping(value = "/category/{id}",method = RequestMethod.POST)
    public ResponseMessage editCategory(@PathVariable("id")String id,@RequestBody Category category)
    {
        catogoryService.editCategory(category,Integer.parseInt(id));
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }
}
