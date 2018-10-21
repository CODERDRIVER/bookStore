package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Income;
import com.lyears.projects.bookstore.model.IncomeData;
import com.lyears.projects.bookstore.repository.IncomeRepository;
import com.lyears.projects.bookstore.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
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
        income.setType(1);  // 保证金收入
        incomeRepository.save(income);
    }

    /**
     * 获得每天的记录
     */

    public IncomeData getDailyRecord(String type)
    {
        /**
         *  根据type 确认查询的是 day/week/month
         */
        //获得当前时间
        Date now = new Date();
        List<Income> list = new ArrayList<>();
        switch (type){
            case "daily":
                Timestamp startDay = DateUtil.getDateStartTime(now);
                Timestamp endDay = DateUtil.getDateEndTime(now);
                list =  incomeRepository.getDailyIncome(startDay,endDay);
            case "weekly":
                Timestamp startWeek = DateUtil.getDateStartTime(DateUtil.getBeginDayOfWeek());
                Timestamp endWeek = DateUtil.getDateEndTime(DateUtil.getEndDayOfWeek());
                list =  incomeRepository.getDailyIncome(startWeek,endWeek);
            case "monthly":
                Timestamp startMonth = DateUtil.getDateStartTime(DateUtil.getBeginDayOfMonth());
                Timestamp endMonth = DateUtil.getDateEndTime(DateUtil.getEndDayOfMonth());
                list =  incomeRepository.getDailyIncome(startMonth,endMonth);
        }
        //  对 income 进行处理 分为 保证金 收入 和  罚金收入
        IncomeData incomeData = new IncomeData();
        for (Income income : list) {
            if (income.getType()==1)
            {
                // 保证金收入
                incomeData.setDeposit(incomeData.getDeposit()+income.getMoney());
            }else{
                incomeData.setFine(incomeData.getFine()+income.getMoney());
            }
        }
        return incomeData;
    }
}
