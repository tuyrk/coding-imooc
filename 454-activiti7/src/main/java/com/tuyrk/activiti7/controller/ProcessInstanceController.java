package com.tuyrk.activiti7.controller;

import com.tuyrk.activiti7.SecurityUtil;
import com.tuyrk.activiti7.pojo.UserInfoBean;
import com.tuyrk.activiti7.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.model.shared.model.VariableInstance;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 流程实例
 *
 * @author tuyrk@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/processInstance")
public class ProcessInstanceController {

    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private RepositoryService repositoryService;

    @Value("${activiti7.test}")
    private boolean test;

    /**
     * 获取流程实例列表
     */
    @GetMapping("/getInstances")
    public AjaxResponse<?> getInstances(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        // 测试用写死的用户POSTMAN测试用；生产场景已经登录，在processDefinitions中可以获取到当前登录用户的信息
        if (test) {
            securityUtil.logInAs("wukong");
        } else {
            // 实际上，并不需要此代码。登录已经有用户信息
            securityUtil.logInAs(userInfoBean.getUsername());
        }
        Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 50));
        log.info("流程实例数量:{}", processInstancePage.getTotalItems());
        List<ProcessInstance> processInstanceList = processInstancePage.getContent();
        processInstanceList.sort(Comparator.comparing(ProcessInstance::getStartDate, Comparator.naturalOrder()));

        List<HashMap<String, Object>> listMap = new ArrayList<>();
        for (ProcessInstance processInstance : processInstanceList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", processInstance.getId());
            hashMap.put("name", processInstance.getName());
            hashMap.put("status", processInstance.getStatus());
            hashMap.put("processDefinitionId", processInstance.getProcessDefinitionId());
            hashMap.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
            hashMap.put("startDate", processInstance.getStartDate());
            hashMap.put("processDefinitionVersion", processInstance.getProcessDefinitionVersion());
            // 因为processRuntime.processDefinition("流程部署ID")查询的结果没有部署流程与部署ID，所以用repositoryService查询
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processInstance.getProcessDefinitionId())
                    .singleResult();
            hashMap.put("deploymentId", processDefinition.getDeploymentId());
            hashMap.put("resourceName", processDefinition.getResourceName());
            listMap.add(hashMap);
        }

        return AjaxResponse.success(listMap);
    }


    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义Key
     * @param instanceName         流程实例名称
     * @param instanceVariable     流程实例参数
     * @return 流程实例ID;流程实例名称
     */
    @PostMapping("/startProcess")
    public AjaxResponse<?> startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                        @RequestParam("instanceName") String instanceName,
                                        @RequestParam("instanceVariable") String instanceVariable) {
        if (test) {
            securityUtil.logInAs("bajie");
        } else {
            securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                .start()
                .withProcessDefinitionKey(processDefinitionKey)
                .withName(instanceName)
                // .withVariable("content", instanceVariable)
                //.withVariable("参数", "参数的值")
                .withBusinessKey("自定义BusinessKey")
                .build());
        return AjaxResponse.success(processInstance.getId() + ";" + processInstance.getName());
    }

    /**
     * 删除流程实例
     *
     * @param instanceId 流程实例ID
     * @return 流程实例名称
     */
    @PostMapping("/deleteInstance")
    public AjaxResponse<?> deleteInstance(@RequestParam("instanceId") String instanceId) {
        if (test) {
            securityUtil.logInAs("wukong");
        }

        ProcessInstance processInstance = processRuntime.delete(ProcessPayloadBuilder
                .delete()
                .withProcessInstanceId(instanceId)
                .build()
        );
        return AjaxResponse.success(processInstance.getName());
    }

    /**
     * 挂起流程实例
     *
     * @param instanceId 流程实例ID
     * @return 流程实例名称
     */
    @PostMapping("/suspendInstance")
    public AjaxResponse<?> suspendInstance(@RequestParam("instanceId") String instanceId) {
        if (test) {
            securityUtil.logInAs("wukong");
        }

        ProcessInstance processInstance = processRuntime.suspend(ProcessPayloadBuilder
                .suspend()
                .withProcessInstanceId(instanceId)
                .build()
        );
        return AjaxResponse.success(processInstance.getName());
    }

    /**
     * 激活流程实例
     *
     * @param instanceId 流程实例ID
     * @return 流程实例名称
     */
    @PostMapping("/resumeInstance")
    public AjaxResponse<?> resumeInstance(@RequestParam("instanceId") String instanceId) {
        if (test) {
            securityUtil.logInAs("wukong");
        }

        ProcessInstance processInstance = processRuntime.resume(ProcessPayloadBuilder
                .resume()
                .withProcessInstanceId(instanceId)
                .build()
        );
        return AjaxResponse.success(processInstance.getName());
    }


    /**
     * 获取流程参数
     *
     * @param instanceId 流程实例ID
     * @return 流程实例名称
     */
    @GetMapping("/variables")
    public AjaxResponse<?> variables(@RequestParam("instanceId") String instanceId) {
        if (test) {
            securityUtil.logInAs("wukong");
        }

        List<VariableInstance> variableInstanceList = processRuntime.variables(ProcessPayloadBuilder
                .variables()
                .withProcessInstanceId(instanceId)
                .build());
        return AjaxResponse.success(variableInstanceList);
    }
}
