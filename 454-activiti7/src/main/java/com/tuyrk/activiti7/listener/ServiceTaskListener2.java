package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务任务
 *
 * @author tuyrk@qq.com
 */
public class ServiceTaskListener2 implements JavaDelegate {
    @Override
    public void execute(DelegateExecution execution) {
        // 获取流程变量的值
        System.out.println(execution.getVariable("aa"));
    }
}
