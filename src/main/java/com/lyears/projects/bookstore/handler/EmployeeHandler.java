package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author fzm
 * @date 2018/2/25
 **/
@Controller
public class EmployeeHandler {

    @Autowired
    private ReaderService userService;

    @GetMapping("/emps")
    public String list(@RequestParam(name = "pageNo",required = false,defaultValue = "1")String pageNoStr,
                       Map<String,Object> map){

        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(pageNoStr);
            if (pageNo < 1){
                pageNo = 1;
            }
        }catch (Exception ignored){}
        int pageSize = 5;
        Page<Reader> page = userService.getPage(pageNo,pageSize);
        map.put("page",page);
        return "list";
    }
}
