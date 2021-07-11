package com.tuyrk.activiti7;

import org.activiti.engine.repository.Deployment;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class Part1_Deployment extends ActivitiwebApplicationTests {

    /**
     * 通过bpmn部署流程
     */
    @Test
    public void initDeploymentBPMN() {
        // String filename = "BPMN/Part1_Deployment.bpmn";
        // String filename = "BPMN/Part4_Task.bpmn";
        String filename = "BPMN/Part8_ProcessRuntime.bpmn";
        // String pngname="BPMN/Part1_Deployment.png";
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource(filename)
                //.addClasspathResource(pngname) // 图片
                .name("流程部署测试ProcessRuntime")
                .deploy();
        System.out.println(deployment);
    }

    /**
     * 通过ZIP部署流程
     */
    @Test
    public void initDeploymentZIP() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:BPMN/Part1_DeploymentV2.zip");
        System.out.println(file.getAbsolutePath());
        InputStream fis = new FileInputStream(file);
        ZipInputStream zip = new ZipInputStream(fis);
        Deployment deployment = repositoryService.createDeployment()
                .addZipInputStream(zip)
                .name("流程部署测试zip")
                .deploy();
        System.out.println(deployment);
    }

    /**
     * 查询流程部署
     */
    @Test
    public void getDeployments() {
        List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
        for (Deployment deployment : deploymentList) {
            System.out.println("Id：" + deployment.getId());
            System.out.println("Name：" + deployment.getName());
            System.out.println("DeploymentTime：" + deployment.getDeploymentTime());
            System.out.println("Key：" + deployment.getKey());
        }
    }
}
