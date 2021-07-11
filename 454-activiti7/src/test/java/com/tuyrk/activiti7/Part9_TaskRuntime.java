package com.tuyrk.activiti7;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part9_TaskRuntime extends ActivitiwebApplicationTests {

    /**
     * 获取当前登录用户任务
     */
    @Test
    public void getTasks() {
        securityUtil.logInAs("wukong");

        Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100),
                TaskPayloadBuilder.tasks().build()
        );
        List<Task> list = tasks.getContent();
        for (Task tk : list) {
            System.out.println("-------------------");
            System.out.println("getId：" + tk.getId());
            System.out.println("getName：" + tk.getName());
            System.out.println("getStatus：" + tk.getStatus());
            System.out.println("getCreatedDate：" + tk.getCreatedDate());
            // 候选人为当前登录用户，null的时候需要前端拾取
            System.out.println("Assignee：" + StringUtils.defaultString(tk.getAssignee(), "待拾取任务"));
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        securityUtil.logInAs("wukong");

        Task task = taskRuntime.task("194476ea-8b25-11eb-a453-3edce3e71d32");
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim()
                    .withTaskId(task.getId())
                    .build());
        }
        taskRuntime.complete(TaskPayloadBuilder
                .complete()
                .withTaskId(task.getId())
                // .withVariable("", "")
                .build());
        System.out.println("任务执行完成");
    }
}
