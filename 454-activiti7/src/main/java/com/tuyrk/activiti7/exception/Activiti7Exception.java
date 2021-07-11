package com.tuyrk.activiti7.exception;

/**
 * @author tuyrk@qq.com
 * @date 2021/4/21 01:01
 */
public class Activiti7Exception extends RuntimeException {
    private static final long serialVersionUID = 962923420166473954L;

    public Activiti7Exception(String message) {
        super(message);
    }

    public Activiti7Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
