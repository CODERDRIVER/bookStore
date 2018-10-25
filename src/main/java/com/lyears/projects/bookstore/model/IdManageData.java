package com.lyears.projects.bookstore.model;

import lombok.Data;

/**
 * @Description
 * @Author: liuXuyang
 * @studentNo 15130110024
 * @Emailaddress 1187697635@qq.com
 * @Date: 2018/10/20 10:48
 */
@Data
public class IdManageData {

    private Integer borrowId;
    private Integer orderId;
    private Integer returnId;
    private Integer bookId;
    private Integer readerId;
    private String phoneNumber;
    private String barCode;
}
