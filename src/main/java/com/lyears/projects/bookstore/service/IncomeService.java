package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Income;
import com.lyears.projects.bookstore.repository.IncomeRepository;
import com.lyears.projects.bookstore.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:09
 */
@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    /**
     * 增加一条记录
     */
    public void addIncome(double money)
    {
        Date date = new Date();
        Income income = new Income();
        income.setTime(date);
        income.setMoney(money);
        incomeRepository.save(income);
    }

    /**
     * 获得每天的记录
     */

    public List<Income> getDailyRecord(String type)
    {
        /**
         *  根据type 确认查询的是 day/week/month
         */
        //获得当前时间
        Date now = new Date();
        switch (type){
            case "day":
                Timestamp startDay = DateUtil.getDateStartTime(now);
                Timestamp endDay = DateUtil.getDateEndTime(now);
                return incomeRepository.getDailyIncome(startDay,endDay);
            case "week":
                Timestamp startWeek = DateUtil.getDateStartTime(DateUtil.getBeginDayOfWeek());
                Timestamp endWeek = DateUtil.getDateEndTime(DateUtil.getEndDayOfWeek());
                return incomeRepository.getDailyIncome(startWeek,endWeek);
            case "month":
                Timestamp startMonth = DateUtil.getDateStartTime(DateUtil.getBeginDayOfMonth());
                Timestamp endMonth = DateUtil.getDateEndTime(DateUtil.getEndDayOfMonth());
                return incomeRepository.getDailyIncome(startMonth,endMonth);
        }

        return null;
    }
}
