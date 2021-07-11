package com.tuyrk.activiti7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.util.ArrayList;

/**
 * 多实例任务的开始监听
 *
 * @author tuyrk@qq.com
 */
public class MultiInstancesStartListener implements ExecutionListener {
    private static final long serialVersionUID = -2773562918083719219L;

    @Override
    public void notify(DelegateExecution execution) {
        ArrayList<String> assigneeList = new ArrayList<>();
        assigneeList.add("bajie");
        assigneeList.add("wukong");
        assigneeList.add("salaboy");
        execution.setVariable("assigneeList", assigneeList);

        // execution.setVariable("isPass",0);
    }
}
