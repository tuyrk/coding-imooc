package com.tuyrk.activiti7;

import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Part7_Gateway extends ActivitiwebApplicationTests {
    @Test
    public void initProcessInstance() {
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("myProcess_Inclusive");
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    @Test
    public void completeTask() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("day", "1");
        // taskService.complete("4010ca5f-923b-11eb-9297-6617e358527a", variables);
        taskService.complete("76bf5b3b-923b-11eb-88d2-6617e358527a");
        System.out.println("完成任务");
    }
}
