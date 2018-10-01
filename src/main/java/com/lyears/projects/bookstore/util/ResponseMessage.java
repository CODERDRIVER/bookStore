package com.lyears.projects.bookstore.util;

import lombok.Data;

/**
 * @author fzm
 * @date 2018/9/28
 **/
@Data
public class ResponseMessage<T> {
    private Integer code;
    private String message;
    private String requestUrl;
    private T data;
}
