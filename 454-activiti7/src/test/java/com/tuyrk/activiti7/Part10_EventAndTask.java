package com.tuyrk.activiti7;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.StartMessagePayloadBuilder;
import org.activiti.engine.runtime.Execution;
import org.junit.jupiter.api.Test;

public class Part10_EventAndTask extends ActivitiwebApplicationTests {

    @Test
    public void signalStart() {
        runtimeService.signalEventReceived("Signal_0ilprd7");
    }

    /**
     * 消息撤回
     */
    @Test
    public void msgBack() {
        Execution exec = runtimeService.createExecutionQuery()
                .messageEventSubscriptionName("Message_0gu091p")
                .processInstanceId("94797440-dcde-11eb-8e0f-acde48001122")
                .singleResult();
        runtimeService.messageEventReceived("Message_0gu091p", exec.getId());

        //


    }

    /**
     * 消息启动
     */
    @Test
    public void msgStart() {
        runtimeService.startProcessInstanceByMessage("Message_0gu091p");
    }

    /**
     * 消息启动
     */
    @Test
    public void msgStartNew() {
        ProcessInstance pi = processRuntime.start(StartMessagePayloadBuilder
                .start("Message_0gu091p")
                .build()
        );
    }


}
