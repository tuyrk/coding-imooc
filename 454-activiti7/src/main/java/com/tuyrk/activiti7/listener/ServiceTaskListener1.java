package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 服务任务
 *
 * @author tuyrk@qq.com
 */
public class ServiceTaskListener1 implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // 输出服务任务的一些属性
        System.out.println(execution.getEventName());
        System.out.println(execution.getProcessDefinitionId());
        System.out.println(execution.getProcessInstanceId());
        // 设置流程变量
        execution.setVariable("aa", "bb");
    }
}
