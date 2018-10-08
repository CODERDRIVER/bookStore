package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.exception.ErrorPageException;
import com.lyears.projects.bookstore.exception.UserDefinedException;
import com.lyears.projects.bookstore.jwt.JwtToken;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultEnum;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/2
 **/
@Controller
public class ReaderHandler {

    @Autowired
    private ReaderService readerService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping(value = "/readers")
    @ResponseBody
    public ResponseMessage getAllReaders() {

        return ResultUtil.success(avoidStackOverflow(readerService.getAllReaders()),
                request.getRequestURL().toString());
    }

    /**
     * 注册读者
     * @param reader    传入的读者信息
     * @return  返回结果
     */
    @PostMapping(value = "/add/reader")
    @ResponseBody
    public ResponseMessage saveReader(@RequestBody Reader reader, @CookieValue(value = "token")Cookie token) {
        String type = "admin";
        if (token == null) {
            //如果未登录，返回未登录页面
            throw new UserDefinedException(ResultEnum.NEED_LOGIN);
            //如果不是管理员，返回没有权限错误页面
        } else if (!JwtToken.verifyToken(token.getValue())
                .get("type").asString().equals(type)) {
            throw new UserDefinedException(ResultEnum.NO_RIGHT);
        }
        readerService.save(reader);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    /**
     * 使用Stream避免栈溢出
     * @param readers 传入的readerList
     * @return 得到的结果
     */
    private List<Reader> avoidStackOverflow(List<Reader> readers) {
        readers.forEach(
                r -> {
                    r.getBorrows().forEach(
                            b -> {
                                b.setReader(null);
                                b.getBook().setBorrows(null);
                                b.getBook().setOrders(null);
                            }
                    );
                    r.getOrders().forEach(
                            b -> {
                                b.setReader(null);
                                b.getBook().setBorrows(null);
                                b.getBook().setOrders(null);
                            }
                    );
                }
        );
        return readers;
    }
}
