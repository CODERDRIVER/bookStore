package com.lyears.projects.bookstore.task;

import com.lyears.projects.bookstore.service.BorrowAndOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author fzm
 * @date 2018/10/3
 **/
@Component
public class ScheduledTasks {

    @Autowired
    private BorrowAndOrderService borrowAndOrderService;

    /**
     * 每十分钟取消取消预约
     */
    @Scheduled(fixedDelay = 600 * 1000)
    public void cancelOrder() {
        borrowAndOrderService.cancelOrderWithTime(LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 10 ? * *")
    public void sendEmailToReader(){
        // TODO: 2018/10/3 send email
        // TODO: 2018/10/3 Deducted the deposit from reader who had book to return
    }
}
