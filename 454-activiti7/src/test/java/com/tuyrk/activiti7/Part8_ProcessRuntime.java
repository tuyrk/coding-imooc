package com.tuyrk.activiti7;

import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part8_ProcessRuntime extends ActivitiwebApplicationTests {

    /**
     * 获取流程实例
     */
    @Test
    public void getProcessInstance() {
        securityUtil.logInAs("bajie");
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 100));
        System.out.println("流程实例数量：" + processInstancePage.getTotalItems());
        List<ProcessInstance> list = processInstancePage.getContent();
        for (ProcessInstance pi : list) {
            System.out.println("-----------------------");
            System.out.println("getId：" + pi.getId());
            System.out.println("getName：" + pi.getName());
            System.out.println("getStartDate：" + pi.getStartDate());
            System.out.println("getStatus：" + pi.getStatus());
            System.out.println("getProcessDefinitionId：" + pi.getProcessDefinitionId());
            System.out.println("getProcessDefinitionKey：" + pi.getProcessDefinitionKey());
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey("myProcess_ProcessRuntime")
                .withName("第一个流程实例名称")
                // .withVariable("", "")
                .withBusinessKey("自定义bKey")
                .build()
        );
        System.out.println("getId：" + processInstance.getId());
    }

    /**
     * 删除流程实例
     */
    @Test
    public void delProcessInstance() {
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId("6207553a-9301-11eb-8035-6617e358527a")
                .withReason("测试删除原因")
                .build()
        );
        System.out.println("getId：" + processInstance.getId());
    }

    /**
     * 挂起流程实例
     */
    @Test
    public void suspendProcessInstance() {
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
                .build()
        );
    }

    /**
     * 激活流程实例
     */
    @Test
    public void resumeProcessInstance() {
        securityUtil.logInAs("bajie");
        ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
                .build()
        );
    }

    /**
     * 流程实例参数
     */
    @Test
    public void getVariables() {
        securityUtil.logInAs("bajie");
        List<VariableInstance> list = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
                .build()
        );
        for (VariableInstance vi : list) {
            System.out.println("-------------------");
            System.out.println("getName：" + vi.getName());
            System.out.println("getValue：" + vi.getValue());
            System.out.println("getTaskId：" + vi.getTaskId());
            System.out.println("getProcessInstanceId：" + vi.getProcessInstanceId());
            System.out.println("getType：" + vi.getType());
            System.out.println("isTaskVariable：" + vi.isTaskVariable());
        }
    }
}
