package com.tuyrk.activiti7;

import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part4_Task extends ActivitiwebApplicationTests {

    /**
     * 任务查询
     */
    @Test
    public void getTasks() {
        List<Task> taskList = taskService.createTaskQuery().list();
        for (Task task : taskList) {
            System.out.println("Id：" + task.getId());
            System.out.println("Name：" + task.getName());
            System.out.println("Assignee：" + task.getAssignee());
        }
    }

    /**
     * 查询我的代办任务
     */
    @Test
    public void getTasksByAssignee() {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("bajie")
                // .taskAssignee("wukong")
                // .taskAssignee("wukong")
                // .taskAssignee("shaseng")
                // .taskId("55c9a6c9-9236-11eb-a3c2-6617e358527a")
                .list();
        for (Task task : taskList) {
            System.out.println("Id：" + task.getId());
            System.out.println("Name：" + task.getName());
            System.out.println("Assignee：" + task.getAssignee());
            System.out.println(task.getOwner());
        }
    }

    /**
     * 执行任务
     */
    @Test
    public void completeTask() {
        taskService.complete("2d1159fa-8a3a-11eb-bd2a-3edce3e71d32");
        System.out.println("完成任务");
    }

    /**
     * 拾取任务
     */
    @Test
    public void claimTask() {
        String candidateUser = "bajie";
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser(candidateUser)
                .list();
        Task task = taskService.createTaskQuery().taskId(taskList.get(0).getId()).singleResult();
        taskService.claim(task.getId(), candidateUser);
    }

    /**
     * 归还与交办任务
     */
    @Test
    public void setTaskAssignee() {
        Task task = taskService.createTaskQuery().taskId("b3d5f8a3-8a3b-11eb-af72-3edce3e71d32").singleResult();
        taskService.setAssignee(task.getId(), "null"); // 归还候选任务
        taskService.setAssignee(task.getId(), "wukong"); // 交办
    }
}
