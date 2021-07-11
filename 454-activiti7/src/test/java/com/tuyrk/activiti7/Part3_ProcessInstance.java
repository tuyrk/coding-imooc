package com.tuyrk.activiti7;

import org.activiti.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part3_ProcessInstance extends ActivitiwebApplicationTests {

    /**
     * 初始化流程实例
     */
    @Test
    public void initProcessInstance() {
        // 1、获取页面表单填报的内容，请假时间，请假事由，String fromData
        // 2、fromData 写入业务表，返回业务表主键ID==businessKey
        // 3、把业务数据与Activiti7流程数据关联
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess_claim", "bKey003");
        System.out.println("流程实例ID：" + pi.getProcessDefinitionId());
    }

    /**
     * 获取流程实例列表
     */
    @Test
    public void getProcessInstances() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance pi : list) {
            System.out.println("--------流程实例------");
            System.out.println("ProcessInstanceId：" + pi.getProcessInstanceId());
            System.out.println("ProcessDefinitionId：" + pi.getProcessDefinitionId());
            System.out.println("isEnded：" + pi.isEnded());
            System.out.println("isSuspended：" + pi.isSuspended());
        }
    }


    /**
     * 暂停与激活流程实例
     */
    @Test
    public void activitieProcessInstance() {
        // runtimeService.suspendProcessInstanceById("fe75ac97-8a30-11eb-90a4-3edce3e71d32");
        // System.out.println("挂起流程实例");

        runtimeService.activateProcessInstanceById("fe75ac97-8a30-11eb-90a4-3edce3e71d32");
        System.out.println("激活流程实例");
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        runtimeService.deleteProcessInstance("fe75ac97-8a30-11eb-90a4-3edce3e71d32", "删着玩");
        System.out.println("删除流程实例");
    }
}
