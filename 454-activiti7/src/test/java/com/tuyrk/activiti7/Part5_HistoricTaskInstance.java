package com.tuyrk.activiti7;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part5_HistoricTaskInstance extends ActivitiwebApplicationTests {
    /**
     * 根据用户名查询历史记录
     */
    @Test
    public void HistoricTaskInstanceByUser() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .taskAssignee("bajie")
                .list();
        for (HistoricTaskInstance hti : list) {
            System.out.println("==============================");
            System.out.println("Id：" + hti.getId());
            System.out.println("ProcessInstanceId：" + hti.getProcessInstanceId());
            System.out.println("Name：" + hti.getName());
        }
    }


    /**
     * 根据流程实例ID查询历史
     */
    @Test
    public void HistoricTaskInstanceByPiID() {
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().asc()
                .processInstanceId("aedaa0e6-8a37-11eb-874f-3edce3e71d32")
                .list();
        for (HistoricTaskInstance hti : list) {
            System.out.println("==============================");
            System.out.println("Id：" + hti.getId());
            System.out.println("ProcessInstanceId：" + hti.getProcessInstanceId());
            System.out.println("Name：" + hti.getName());
        }
    }

    /**
     * 根据流程实例ID查询历史参数
     */
    @Test
    public void HistoricVariableInstance() {
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery()
                .orderByProcessInstanceId().asc()
                .processInstanceId("aedaa0e6-8a37-11eb-874f-3edce3e71d32")
                .list();
        for (HistoricVariableInstance hvi : list) {
            System.out.println("Id：" + hvi.getId());
            System.out.println("VariableTypeName：" + hvi.getVariableTypeName());
            System.out.println("VariableName：" + hvi.getVariableName());
            System.out.println("Value：" + hvi.getValue());
        }
    }
}
