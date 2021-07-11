package com.tuyrk.activiti7;

import org.activiti.engine.repository.ProcessDefinition;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Part2_ProcessDefinition extends ActivitiwebApplicationTests {

    /**
     * 查询流程定义
     */
    @Test
    public void getDefinitions() {
        List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
        for (ProcessDefinition pd : list) {
            System.out.println("------流程定义--------");
            System.out.println("Id：" + pd.getId());
            System.out.println("Name：" + pd.getName());
            System.out.println("Key：" + pd.getKey());
            System.out.println("ResourceName：" + pd.getResourceName());
            System.out.println("DeploymentId：" + pd.getDeploymentId());
            System.out.println("Version：" + pd.getVersion());
        }
    }

    /**
     * 删除流程定义
     */
    @Test
    public void delDefinition() {
        String pdID = "fe7ccf11-8a24-11eb-8e2b-3edce3e71d32";
        // String pdID = "123"; // Could not find a deployment with id '123'.
        repositoryService.deleteDeployment(pdID, true);
        System.out.println("删除流程定义成功");
    }
}
