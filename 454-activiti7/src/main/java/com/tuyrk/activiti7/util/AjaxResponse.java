package com.tuyrk.activiti7.util;

import lombok.Data;

@Data
public class AjaxResponse<T> {
    private int code;
    private String msg;
    private T data;

    public static <T> AjaxResponse<T> success(T obj) {
        AjaxResponse<T> ajaxResponse = new AjaxResponse<>();
        ajaxResponse.setCode(ResponseCode.SUCCESS.getCode());
        ajaxResponse.setMsg(ResponseCode.SUCCESS.getDesc());
        ajaxResponse.setData(obj);
        return ajaxResponse;
    }

    public static <T> AjaxResponse<T> error(String msg) {
        AjaxResponse<T> ajaxResponse = new AjaxResponse<>();
        ajaxResponse.setCode(ResponseCode.ERROR.getCode());
        ajaxResponse.setMsg(msg);
        return ajaxResponse;
    }
}
