package com.tuyrk.activiti7.handler;

import com.tuyrk.activiti7.exception.Activiti7Exception;
import com.tuyrk.activiti7.util.AjaxResponse;
import org.activiti.api.runtime.shared.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 错误统一处理
 *
 * @author tuyrk@qq.com
 * @date 2021/4/21 00:58
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Activiti7Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<?> handleException(Activiti7Exception exception) {
        return AjaxResponse.error(exception.getLocalizedMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<?> handleException(NotFoundException exception) {
        return AjaxResponse.error(exception.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse<?> handleException(Exception exception) {
        return AjaxResponse.error(exception.getLocalizedMessage());
    }
}
