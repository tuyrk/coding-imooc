package com.tuyrk.activiti7.controller;

import com.tuyrk.activiti7.pojo.UserInfoBean;
import com.tuyrk.activiti7.util.AjaxResponse;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 流程历史
 *
 * @author tuyrk@qq.com
 */
@RestController
@RequestMapping("/activitiHistory")
public class ActivitiHistoryController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;

    @Value("${activiti7.test}")
    private boolean test;

    /**
     * 用户历史任务
     */
    @GetMapping("/getTasks")
    public AjaxResponse<?> getTasks(@AuthenticationPrincipal UserInfoBean userInfoBean) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().desc()
                .taskAssignee(userInfoBean.getUsername())
                .list();
        return AjaxResponse.success(historicTaskInstances);
    }

    /**
     * 实例历史任务
     *
     * @param instanceId 流程实例ID
     * @return 流程实例的历史任务
     */
    @GetMapping("/getTasksByInstanceId")
    public AjaxResponse<?> getTasksByInstanceId(@RequestParam("instanceId") String instanceId) {
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
                .orderByHistoricTaskInstanceEndTime().desc()
                .processInstanceId(instanceId)
                .list();
        return AjaxResponse.success(historicTaskInstances);
    }


    /**
     * 流程图高亮
     *
     * @param instanceId   流程实例ID
     * @param userInfoBean 用户登录信息
     * @return 流程实例的高亮历史任务
     */
    @GetMapping("/getHighLine")
    public AjaxResponse<?> getHighLine(@RequestParam("instanceId") String instanceId, @AuthenticationPrincipal UserInfoBean userInfoBean) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
        // 获取bpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
        // 因为我们这里只定义了一个Process 所以获取集合中的第一个即可
        Process process = bpmnModel.getProcesses().get(0);
        // 获取所有的FlowElement信息
        Collection<FlowElement> flowElementList = process.getFlowElements();

        // 1. 获取流程实例的所有线条
        Map<String, String> sequenceMap = new HashMap<>();
        for (FlowElement flowElement : flowElementList) {
            // 判断是否是线条
            if (flowElement instanceof SequenceFlow) {
                SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
                String ref = sequenceFlow.getSourceRef();
                String targetRef = sequenceFlow.getTargetRef();
                sequenceMap.put(ref + targetRef, sequenceFlow.getId());
            }
        }

        // 2. 获取流程实例所有的历史节点
        List<HistoricActivityInstance> allInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .list();
        // 各个历史节点两两组合成key
        Set<String> keyList = new HashSet<>();
        for (HistoricActivityInstance i : allInstanceList) {
            for (HistoricActivityInstance j : allInstanceList) {
                if (i != j) {
                    keyList.add(i.getActivityId() + j.getActivityId());
                }
            }
        }
        // 高亮连线ID
        Set<String> highLine = keyList.stream().map(sequenceMap::get).collect(Collectors.toSet());

        // 3. 获取流程实例已完成的历史节点
        List<HistoricActivityInstance> finishedInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .finished().list();
        // 高亮已经完成的节点ID
        Set<String> highPoint = finishedInstanceList.stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toSet());

        // 4. 获取流程实例待办的历史节点
        List<HistoricActivityInstance> unFinishedInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(instanceId)
                .unfinished().list();
        // 待办高亮节点
        Set<String> waitingToDo = unFinishedInstanceList.stream().map(HistoricActivityInstance::getActivityId).collect(Collectors.toSet());
        /*Set<String> waitingToDo = new HashSet<>();
        // 需要移除的高亮连线
        Set<String> removeSet = new HashSet<>();
        unFinishedInstanceList.forEach(instance -> {
            waitingToDo.add(instance.getActivityId());

            for (FlowElement flowElement : flowElementList) {
                // 判断是否是 用户节点
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;

                    if (userTask.getId().equals(instance.getActivityId())) {
                        List<SequenceFlow> outgoingFlows = userTask.getOutgoingFlows();
                        // 因为 高亮连线查询的是所有节点  两两组合 把待办 之后  往外发出的连线 也包含进去了  所以要把高亮待办节点 之后 即出的连线去掉
                        if (outgoingFlows != null && outgoingFlows.size() > 0) {
                            outgoingFlows.forEach(a -> {
                                if (a.getSourceRef().equals(instance.getActivityId())) {
                                    removeSet.add(a.getId());
                                }
                            });
                        }
                    }
                }
            }
        });
        highLine.removeAll(removeSet);*/


        // 获取当前用户
        // User sysUser = getSysUser();
        String assigneeName = null;
        if (test) {
            assigneeName = "wukong";
        } else {
            assigneeName = userInfoBean.getUsername();
        }
        // 5. 当前用户已完成的任务
        List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assigneeName)
                .processInstanceId(instanceId)
                .finished().list();
        // 存放 高亮 我的办理节点
        Set<String> iDo = taskInstanceList.stream().map(HistoricTaskInstance::getTaskDefinitionKey).collect(Collectors.toSet());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("highPoint", highPoint);
        resultMap.put("highLine", highLine);
        resultMap.put("waitingToDo", waitingToDo);
        resultMap.put("iDo", iDo);
        return AjaxResponse.success(resultMap);
    }
}
