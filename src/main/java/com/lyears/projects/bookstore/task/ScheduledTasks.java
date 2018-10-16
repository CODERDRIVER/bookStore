package com.lyears.projects.bookstore.task;

import com.lyears.projects.bookstore.entity.Borrow;
import com.lyears.projects.bookstore.service.BorrowAndOrderService;
import com.lyears.projects.bookstore.service.ReaderService;
import com.lyears.projects.bookstore.util.SendEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Component
public class ScheduledTasks {

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    @Autowired
    private ReaderService readerService;

    /**
     * 每十分钟取消取消预约
     */
    @Scheduled(fixedDelay = 600 * 1000)
    public void cancelOrder() {

        /**
         *  对书籍2小时内未被借出，则预约失效
         */
        borrowAndOrderService.cancelOrderWithTime(LocalDateTime.now());
    }

    /**
     *  书籍到期前将邮件发送至读者邮箱
     *  默认一天前
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void sendEmailToReader(){

        /**
         *  首先检测 书籍是否超期
         */
        // 获得当前时间
        LocalDate now = LocalDate.now();
        //获得所有的 Borrow列表
        List<Borrow> allBrrows = borrowAndOrderService.findAllBrrows(now);
        // TODO: 2018/10/3 send email
        allBrrows.forEach(borrow -> {
            // 获得readerId
            String email = borrow.getReader().getEmail();
            String readerName = borrow.getReader().getUserName();
            String bookName = borrow.getBook().getBookName();
            // 发送邮件
            SendEmail.sendMail(email,"书籍逾期提醒",readerName+",你好，你所借阅的书籍"+bookName+"即将到期，请及时归还");
        });
    }


}
