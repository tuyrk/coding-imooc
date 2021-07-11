package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器1-bajie
 *
 * @author tuyrk@qq.com
 */
public class TkListener1 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人：" + delegateTask.getAssignee());
        // TODO 根据用户名查询用户电话并调用发送短信接口
        delegateTask.setVariable("delegateAssignee", delegateTask.getAssignee());
    }
}
