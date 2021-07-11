package com.tuyrk.activiti7.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.Expression;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 执行监听器
 */
@Slf4j
public class PiListener implements ExecutionListener {
    @Autowired
    private Expression sendType;

    @Override
    public void notify(DelegateExecution execution) {
        log.info("事件名称:{}", execution.getEventName());
        log.info("流程定义ID:{}", execution.getProcessDefinitionId());
        if ("start".equals(execution.getEventName())) {
            log.info("记录节点开始时间");
        } else if ("end".equals(execution.getEventName())) {
            log.info("记录节点结束时间");
        }
        log.info("sendType:{}", sendType.getValue(execution).toString());
    }
}
