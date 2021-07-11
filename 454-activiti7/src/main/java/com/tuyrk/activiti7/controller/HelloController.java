package com.tuyrk.activiti7.controller;


import com.tuyrk.activiti7.SecurityUtil;
import com.tuyrk.activiti7.exception.Activiti7Exception;
import com.tuyrk.activiti7.mapper.ActivitiMapper;
import com.tuyrk.activiti7.pojo.Act_ru_task;
import com.tuyrk.activiti7.pojo.UserInfoBean;
import com.tuyrk.activiti7.util.AjaxResponse;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ActivitiMapper mapper;
    @Autowired
    private ProcessRuntime processRuntime;

    @Value("${activiti7.test}")
    private boolean test;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say() {
        return "Activiti7欢迎你";
    }

    @RequestMapping(value = "/me1", method = RequestMethod.GET)
    public Object getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/me2", method = RequestMethod.GET)
    public Object user(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        return userInfoBean.name;
    }


    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public Object gettask() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        securityUtil.logInAs(username);
        try {
            Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 10));
            return ">  Other cannot see the task: " + tasks.getTotalItems();
        } catch (Exception e) {
            return "错误：" + e;
        }
    }


    @RequestMapping(value = "/db", method = RequestMethod.GET)
    public Object getdb() {
        try {
            List<Act_ru_task> act_ru_task123 = mapper.selectName();
            return act_ru_task123.toString();
        } catch (Exception e) {
            return "错误：" + e;
        }
    }

    /**
     * 启动带参数-测试方法
     *
     * @param processDefinitionKey
     * @param instanceName
     * @return
     */
    @GetMapping(value = "/testStartProcess")
    public AjaxResponse testStartProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                         @RequestParam("instanceName") String instanceName) {
        try {
            if (test) {
                securityUtil.logInAs("bajie");
            } else {
                securityUtil.logInAs(SecurityContextHolder.getContext().getAuthentication().getName());
            }

            ProcessInstance processInstance = processRuntime.start(ProcessPayloadBuilder
                    .start()
                    .withProcessDefinitionKey(processDefinitionKey)
                    .withName(instanceName)
                    .withVariable("assignee", "bajie")
                    .withVariable("day", "4")
                    .withBusinessKey("自定义BusinessKey")
                    .build());
            return AjaxResponse.success(processInstance.getName() + "；" + processInstance.getId());
        } catch (Exception e) {
            throw new Activiti7Exception("创建流程实例失败:", e);
        }
    }


    /**
     * 完成待办任务带参数
     *
     * @param taskID
     * @return
     */
    @GetMapping(value = "/testcompleteTask")
    public AjaxResponse testcompleteTask(@RequestParam("taskID") String taskID) {
        try {
            if (test) {
                securityUtil.logInAs("bajie");
            }
            Task task = taskRuntime.task(taskID);
            taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                    .withVariable("day", "2")//执行环节设置变量
                    .build());
            return AjaxResponse.success(null);
        } catch (Exception e) {
            throw new Activiti7Exception("完成失败:", e);
        }
    }
}
