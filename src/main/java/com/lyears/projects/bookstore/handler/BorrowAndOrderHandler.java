package com.lyears.projects.bookstore.handler;

import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.entity.Reader;
import com.lyears.projects.bookstore.service.BorrowAndOrderService;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.ResponseMessage;
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
 * @date 2018/10/1
 **/
@Controller
public class BorrowAndOrderHandler {

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private ReaderService readerService;

    @Autowired
    private HttpServletRequest request;


    @PostMapping(value = "/borrow", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage addNewBorrow(@RequestParam("bookName") String bookName,
                                        @RequestParam("readerName") String readerName) {

        borrowAndOrderService.saveBorrowInfoWithBookAndReader(bookName, readerName);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }

    @PostMapping(value = "/order", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseMessage addNewOrder(@RequestParam("bookName") String bookName,
                                       @RequestParam("readerName") String readerName) {

        borrowAndOrderService.saveOrderInfoWithBookAndReader(bookName, readerName);
        return ResultUtil.successNoData(request.getRequestURL().toString());
    }



    @GetMapping("/borrows/today")
    @ResponseBody
    public ResponseMessage getTodayBorrows() {
        //查看当天的借阅情况
        return ResultUtil.success(avoidStackOverflow(borrowAndOrderService.getTodayBorrow()),
                request.getRequestURL().toString());
    }

    /**
     *  获得所有读者的借阅情况
     */
    @GetMapping("/librarian/borrows")
    @ResponseBody
    public ResponseMessage getAllBorrows()
    {
        return borrowAndOrderService.getAllBorrows();
    }

    /**
     *  获得所有读者的预约情况
     * @return
     */
    @GetMapping("/librarian/orders")
    @ResponseBody
    public ResponseMessage getAllOrders()
    {
        return borrowAndOrderService.getAllOrders();
    }

    /**
     * 查看所有读者的借阅信息
     * @param readerName
     * @return
     */
    @GetMapping("/borrows/readerName/{readerName}")
    @ResponseBody
    public ResponseMessage getBorrowsByReader(@PathVariable(name = "readerName")String readerName) {
        Reader reader = readerService.findByUserName(readerName);
        //查看一个读者的所有借阅信息
        return ResultUtil.success(avoidStackOverflow(borrowAndOrderService.getBorrowByReader(reader)),
                request.getRequestURL().toString());
    }

    /**
     *  根据readerid 查询读者的所有借阅信息
     * @param cookie
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/borrows/readerId",method = RequestMethod.GET)
    public ResponseMessage getBorrowsByReaderId(@RequestParam(value = "readerId",required = false)String readerId,@CookieValue(value = "readerId",required = false)Cookie cookie,@CookieValue(value = "librarianId",required = false)Cookie librarianId)
    {
        String id = "";
        if (cookie!=null)
        {
            id = cookie.getValue();
        }else{
            id = readerId;
        }
        // type 0 表示根据id 和borrowStatus查询  1 表示只根据id
        int type = 0;
        if (librarianId!=null)
        {
            type = 1;
        }
        return ResultUtil.success(borrowAndOrderService.getBorrowsByReaderId(Integer.parseInt(id),type),request.getRequestURL().toString());
    }

    private List<Borrow> avoidStackOverflow(List<Borrow> borrows){
        borrows.forEach(
                borrow -> {
                    borrow.getBook().setBorrows(null);
                    borrow.getBook().setOrders(null);
                    borrow.getReader().setBorrows(null);
                    borrow.getReader().setOrders(null);
                });
        return borrows;
    }
}
