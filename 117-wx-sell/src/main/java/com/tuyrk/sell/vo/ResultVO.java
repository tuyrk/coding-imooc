package com.tuyrk.sell.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * User: 涂元坤
 * Mail: 766564616@qq.com
 * Create: 2019/1/9 20:16 星期三
 * Description:
 * http请求返回的最外层对象
 */
@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> implements Serializable {
    private static final long serialVersionUID = 973928566122832645L;
    /*错误码*/
    private Integer code;
    /*提示信息*/
    private String msg;
    /*具体内容*/
    private T data;
}
