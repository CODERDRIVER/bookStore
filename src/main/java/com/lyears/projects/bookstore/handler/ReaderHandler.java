package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
import com.lyears.projects.bookstore.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping(value = "/reader", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage saveReader(Reader reader) {
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
