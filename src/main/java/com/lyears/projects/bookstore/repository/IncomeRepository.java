package com.lyears.projects.bookstore.repository;

import com.lyears.projects.bookstore.entity.Income;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/11 21:08
 */
@Repository
public interface IncomeRepository extends JpaRepository<Income,Integer>{

    /**
     * 获得每日的收入
     */
    @Query(nativeQuery = true,value = "select * from income as i where i.time >= :start AND  i.time <= :end")
    public List<Income> getDailyIncome(@Param("start") Timestamp start, @Param("end") Timestamp end);
}
