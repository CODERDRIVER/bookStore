package com.lyears.projects.bookstore.util;

/**
 * @author fzm
 * @date 2018/9/28
 **/
public class ResultUtil {

    public static ResponseMessage success(Object o, String url) {
        ResponseMessage<Object> result = new ResponseMessage<>();
        result.setCode(ResultEnum.SUCCESS.getCode());
        result.setMessage(ResultEnum.SUCCESS.getMsg());
        result.setRequestUrl(url);
        result.setData(o);
        return result;
    }

    /**
     * 操作成功不返回消息
     *
     * @return success
     */
    public static ResponseMessage successNoData(String url) {
        return success(null, url);
    }

    /**
     * 操作失败返回的消息
     *
     * @param code
     * @param msg
     * @return
     */
    public static ResponseMessage error(Integer code, String msg, String url) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(code);
        responseMessage.setMessage(msg);
        responseMessage.setRequestUrl(url);
        return responseMessage;
    }

    /**
     * 操作失败返回消息，对error的重载
     *
     * @param resultEnum
     * @return
     */
    public static ResponseMessage error(ResultEnum resultEnum, String url) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setCode(resultEnum.getCode());
        responseMessage.setMessage(resultEnum.getMsg());
        responseMessage.setRequestUrl(url);
        return responseMessage;
    }

    /**
     *  操作失败，带有数据
     */
    public static ResponseMessage errorWithData(Object o, String url) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setData(o);
        responseMessage.setRequestUrl(url);
        return responseMessage;
    }

}
