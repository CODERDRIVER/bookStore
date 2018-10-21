package com.lyears.projects.bookstore.util;



import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @Description 日期工具类
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/13 16:12
 */
public class DateUtil {


    /**
     * 获取本周开始时间
     * @return
     */
    public static Date getBeginDayOfWeek()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //默认周日是 1
        if (dayOfWeek == 1)
        {
            dayOfWeek+=7;
        }
        calendar.add(Calendar.DATE,2-dayOfWeek);
        return getDateStartTime(calendar.getTime());
    }

    /**
     *  获取本周结束时间
     */
    public static Date getEndDayOfWeek()
    {
        Calendar calendar = Calendar.getInstance();
        // 给本周开始时间加6
        calendar.setTime(getBeginDayOfWeek());
        calendar.add(Calendar.DAY_OF_WEEK,6);
        return getDateEndTime(calendar.getTime());
    }

    /**
     *  获取本月开始时间
     */
    public static Date getBeginDayOfMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
        return getDateStartTime(calendar.getTime());
    }

    /**
     *  获取本月结束时间
     */
    public static Date getEndDayOfMonth()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),1);
        // 获得该月总共有多少天
        int actualMaximum = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),actualMaximum);
        return getDateEndTime(calendar.getTime());
    }

    /**
     *  获取某个日期的开始时间
     */

    public static Timestamp getDateStartTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (date!=null)
        {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
        calendar.set(Calendar.MILLISECOND,0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     *  获得某个日期的结束时间
     */
    public static Timestamp getDateEndTime(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        if (date!=null)
        {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),23,59,59);
        calendar.set(Calendar.MILLISECOND,999);
        return new Timestamp(calendar.getTimeInMillis());
    }

}
