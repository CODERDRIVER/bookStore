package com.lyears.projects.bookstore.service;

import com.lyears.projects.bookstore.entity.Constants;
import com.lyears.projects.bookstore.repository.ConstantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:14
 */
@Service
public class ConstantsService {

    @Autowired
    private ConstantsRepository constantsRepository;

    /**
     * 获得当前保证金的钱数
     */
    public Double getDeposit()
    {
        List<Constants> all = constantsRepository.findAll();
        if (all!=null&&all.size()!=0)
        {
            return all.get(0).getDeposit();
        }else{
            return 300d;
        }
    }

    /**
     * 获取罚金的钱数
     */

    public Double getFine()
    {
        List<Constants> all = constantsRepository.findAll();
        if (all!=null&&all.size()!=0)
        {
            return all.get(0).getFine();
        }else{
            return 1d;
        }
    }

    /**
     *  获取归还期限的天数
     */
    public int getRuturnPeriod(){
        List<Constants> all = constantsRepository.findAll();
        if (all!=null&&all.size()!=0)
        {
            return all.get(0).getReturnPeriod();
        }else{
            return 30;
        }
    }


    /**
     *  获取读者总共可以借阅的数量
     */
    public int getTotalBorrowNums(){
        List<Constants> constants = constantsRepository.findAll();
        if (constants!=null&&constants.size()!=0)
        {
            return constants.get(0).getTotalBorrowNums();
        }else{
            return 3;
        }
    }
}
