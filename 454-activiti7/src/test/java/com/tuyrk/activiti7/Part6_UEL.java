package com.tuyrk.activiti7;


import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Part6_UEL extends ActivitiwebApplicationTests {

    /**
     * 带参数启动流程实例，指定执行人
     */
    @Test
    public void initProcessInstanceWithArgs() {
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("ZhiXingRen", "wukong");
        // variables.put("ZhiXingRen2", "aaa");
        // variables.put("ZhiXingRen3", "wukbbbong");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_1uel_V2", "bKey002", variables);
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    /**
     * 带参数完成任务，指定流程变量测试
     */
    @Test
    public void completeTaskWithArgs() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("pay", "101");
        taskService.complete("a2cbdb8c-8b22-11eb-98a9-3edce3e71d32", variables);
        System.out.println("完成任务");
    }

    /**
     * 带参数启动流程实例，使用实体类
     */
    @Test
    public void initProcessInstanceWithClassArgs() {
        UEL_POJO uel_pojo = new UEL_POJO();
        uel_pojo.setZhiXingRen("bajie");
        // 流程变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("uelpojo", uel_pojo);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myProcess_uelv3_test", "bKey002", variables);
        System.out.println("流程实例ID：" + processInstance.getProcessDefinitionId());
    }

    /**
     * 带参数任务完成环节，指定多个候选人
     */
    @Test
    public void initProcessInstanceWithCandiDateArgs() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("houXuanRen", "wukong,tangseng");
        taskService.complete("5830e77b-8b26-11eb-9b0c-3edce3e71d32", variables);
        System.out.println("完成任务");
    }

    /**
     * 直接指定流程变量
     */
    @Test
    public void otherArgs() {
        runtimeService.setVariable("a2c8a736-8b22-11eb-98a9-3edce3e71d32", "pay", "33");
        // runtimeService.setVariables();
        // taskService.setVariable();
        // taskService.setVariables();
    }

    /**
     * 局部变量
     */
    @Test
    public void otherLocalArgs() {
        runtimeService.setVariableLocal("a2c8a736-8b22-11eb-98a9-3edce3e71d32", "pay", "20");
        // runtimeService.setVariablesLocal();
        // taskService.setVariableLocal();
        // taskService.setVariablesLocal();
    }
}
