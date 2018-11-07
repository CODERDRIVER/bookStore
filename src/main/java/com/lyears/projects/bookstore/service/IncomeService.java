package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Income;
import com.lyears.projects.bookstore.model.IncomeData;
import com.lyears.projects.bookstore.repository.IncomeRepository;
import com.lyears.projects.bookstore.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public List<IncomeData> getDailyRecord(String date)
    {
        /**
         *  根据type 确认查询的是 day/week/month
         */
        // 格式化日期
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date now  = null;
        try {
            if (date==null||date.equals(""))
            {
                now = new Date();
            }else{
                now = simpleDateFormat.parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获得当前时间
        // daily
        Timestamp startDay = DateUtil.getDateStartTime(now);
        Timestamp endDay = DateUtil.getDateEndTime(now);
        System.out.println(startDay+"------"+endDay);
        List<Income> dailyIncome = incomeRepository.getDailyIncome(startDay, endDay);

        // weekly
        Timestamp startWeek = DateUtil.getBeginDayOfWeek(now);
        Timestamp endWeek =DateUtil.getEndDayOfWeek(now);
        System.out.println(startWeek+"------"+endWeek);
        List<Income> weeklyIncome = incomeRepository.getDailyIncome(startWeek, endWeek);

        // monthly
        Timestamp startMonth = DateUtil.getBeginDayOfMonth(now);
        Timestamp endMonth = DateUtil.getEndDayOfMonth(now);
        System.out.println(startMonth+"------"+endMonth);
        List<Income> monthlyIncome = incomeRepository.getDailyIncome(startMonth, endMonth);

        //  对 income 进行处理 分为 保证金 收入 和  罚金收入
        List<IncomeData> incomeDataList = new ArrayList<>();

        // daily income
        IncomeData incomeData = new IncomeData();
        for (Income income : dailyIncome) {
            if (income.getType()==1)
            {
                // 保证金收入
                incomeData.setDeposit(incomeData.getDeposit()+income.getMoney());
            }else{
                incomeData.setFine(incomeData.getFine()+income.getMoney());
            }
        }
        incomeDataList.add(incomeData);

        // weekly
        incomeData = new IncomeData();
        for (Income income : weeklyIncome) {
            if (income.getType()==1)
            {
                // 保证金收入
                incomeData.setDeposit(incomeData.getDeposit()+income.getMoney());
            }else{
                incomeData.setFine(incomeData.getFine()+income.getMoney());
            }
        }
        incomeDataList.add(incomeData);

        // monthly
        incomeData = new IncomeData();
        for (Income income : monthlyIncome) {
            if (income.getType()==1)
            {
                // 保证金收入
                incomeData.setDeposit(incomeData.getDeposit()+income.getMoney());
            }else{
                incomeData.setFine(incomeData.getFine()+income.getMoney());
            }
        }
        incomeDataList.add(incomeData);
        return incomeDataList;
    }
}
