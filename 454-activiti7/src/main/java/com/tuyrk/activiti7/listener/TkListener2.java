package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 任务监听器2-wukong
 *
 * @author tuyrk@qq.com
 */
public class TkListener2 implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println("执行人2：" + delegateTask.getVariable("delegateAssignee"));
        // TODO 根据执行人username获取组织机构代码，加工后得到领导是wukong
        delegateTask.setAssignee("wukong");
    }
}
