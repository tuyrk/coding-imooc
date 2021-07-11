package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 错误服务任务
 *
 * @author tuyrk@qq.com
 */
public class ErrServiceTaskListener implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        throw new BpmnError("Error_13205hq");
    }
}
