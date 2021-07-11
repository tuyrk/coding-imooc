package com.tuyrk.activiti7.controller;

import com.tuyrk.activiti7.SecurityUtil;
import com.tuyrk.activiti7.exception.Activiti7Exception;
import com.tuyrk.activiti7.mapper.ActivitiMapper;
import com.tuyrk.activiti7.util.AjaxResponse;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.FormProperty;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务
 *
 * @author tuyrk@qq.com
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskRuntime taskRuntime;
    @Autowired
    private SecurityUtil securityUtil;
    @Autowired
    private ProcessRuntime processRuntime;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ActivitiMapper activitiMapper;

    @Value("${activiti7.test}")
    private boolean test;

    /**
     * 我的待办任务
     */
    @GetMapping("/getTasks")
    public AjaxResponse<?> getTasks() {
        if (test) {
            securityUtil.logInAs("bajie");
        }
        Page<Task> taskPage = taskRuntime.tasks(Pageable.of(0, 100));

        List<HashMap<String, Object>> listMap = new ArrayList<>();
        for (Task task : taskPage.getContent()) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", task.getId());
            hashMap.put("name", task.getName());
            hashMap.put("status", task.getStatus());
            hashMap.put("createdDate", task.getCreatedDate());
            // 执行人，null时前台显示未拾取
            if (task.getAssignee() == null) {
                hashMap.put("assignee", "待拾取任务");
            } else {
                hashMap.put("assignee", task.getAssignee());
            }
            ProcessInstance processInstance = processRuntime.processInstance(task.getProcessInstanceId());
            hashMap.put("instanceName", processInstance.getName());
            listMap.add(hashMap);
        }
        return AjaxResponse.success(listMap);
    }

    /**
     * 完成待办任务
     *
     * @param taskId 任务ID
     * @return 完成
     */
    @PostMapping("/completeTask")
    public AjaxResponse<?> completeTask(@RequestParam("taskId") String taskId) {
        if (test) {
            securityUtil.logInAs("bajie");
        }

        Task task = taskRuntime.task(taskId);
        // 执行人为空，先进性拾取操作
        if (task.getAssignee() == null) {
            taskRuntime.claim(TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
        }

        taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(task.getId())
                //.withVariable("参数", "参数的值")
                .build());

        return AjaxResponse.success(true);
    }

    //启动
    @GetMapping("/startProcess4")
    public AjaxResponse<?> startProcess3(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                         @RequestParam("instanceName") String instanceName,
                                         @RequestParam("instanceVariable") String instanceVariable) {
        try {
            if (test) {
                securityUtil.logInAs("wukong");
            }


/*            @RequestMapping("/approval_msg")
            @ResponseBody
            public JsonResponse approvalPass(String id,String msg){
                JsonResponse jsonResponse = new JsonResponse();

                if(StringUtil.isNotEmpty(msg)){
                    String str= msg.replace("\"", "");
                    taskService.setVariable(id,"msg",str);
                }
                taskService.complete(id);
                return jsonResponse;
            }*/
            return AjaxResponse.success(null);
        } catch (Exception e) {
            throw new Activiti7Exception("失败", e);
        }
    }


    /**
     * 渲染动态任务表单
     * <p>
     * <p>
     * FormProperty_0ueitp2-_!类型-_!名称-_!默认值-_!是否参数
     * <p>
     * 例子：
     * <p>
     * FormProperty_0lovri0-_!string-_!姓名-_!请输入姓名-_!f
     * <p>
     * FormProperty_1iu6onu-_!int-_!年龄-_!请输入年龄-_!s
     * <p>
     * 默认值：无、字符常量、FormProperty_开头定义过的控件ID
     * <p>
     * 是否参数：f为不是参数，s是字符，t是时间(不需要int，因为这里int等价于string)
     * <p>
     * 注：类型是可以获取到的，但是为了统一配置原则，都配置到
     * <p>
     * <p>
     * 注意!!!!!!!!:表单Key必须要任务编号一模一样，因为参数需要任务key，但是无法获取，只能获取表单key“task.getFormKey()”当做任务key
     *
     * @param taskId 任务ID
     * @return 表单数据
     */
    @GetMapping("/formDataShow")
    public AjaxResponse<?> formDataShow(@RequestParam("taskId") String taskId) {
        if (test) {
            securityUtil.logInAs("wukong");
        }
        Task task = taskRuntime.task(taskId);

        // 构建表单控件历史数据字典
        // 本实例所有保存的表单数据HashMap，为了快速读取控件以前环节存储的值
        HashMap<String, String> controlListMap = new HashMap<>();
        // 本实例所有保存的表单数据
        List<HashMap<String, Object>> tempControlList = activitiMapper.selectFormData(task.getProcessInstanceId());
        for (HashMap<String, Object> ls : tempControlList) {
            String controlId = ls.get("Control_ID_").toString();
            String controlValue = ls.get("Control_VALUE_").toString();
            controlListMap.put(controlId, controlValue);
        }

        UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
                .getFlowElement(task.getFormKey());
        if (userTask == null) {
            return AjaxResponse.success("无表单");
        }
        List<FormProperty> formPropertyList = userTask.getFormProperties();
        List<Map<String, Object>> listMap = new ArrayList<>();
        for (FormProperty formProperty : formPropertyList) {
            String[] splitFormProperty = formProperty.getId().split("-_!");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", splitFormProperty[0]);
            hashMap.put("controlType", splitFormProperty[1]);
            hashMap.put("controlLabel", splitFormProperty[2]);
            /// hashMap.put("controlDefValue", splitFormProperty[3]);
            // 默认值如果是表单控件ID
            if (splitFormProperty[3].startsWith("FormProperty_")) {
                // 控件ID存在
                if (controlListMap.containsKey(splitFormProperty[3])) {
                    hashMap.put("controlDefValue", controlListMap.get(splitFormProperty[3]));
                } else {
                    // 控件ID不存在
                    hashMap.put("controlDefValue", String.format("读取失败，检查%s配置", splitFormProperty[0]));
                }
            } else {
                // 默认值如果不是表单控件ID则写入默认值
                hashMap.put("controlDefValue", splitFormProperty[3]);
            }
            hashMap.put("controlParam", splitFormProperty[4]);
            listMap.add(hashMap);
        }
        return AjaxResponse.success(listMap);
    }

    /**
     * 保存动态任务表单
     * <p>
     * formData:控件id-_!控件值-_!是否参数!_!控件id-_!控件值-_!是否参数
     * <p>
     * FormProperty_0lovri0-_!不是参数-_!f!_!FormProperty_1iu6onu-_!数字参数-_!s
     * <p>
     * 是否参数：f为不是参数，s是字符，t是时间(不需要int，因为int可等价于string)
     *
     * @param taskId         任务ID
     * @param formDataString 表单数据
     * @return 保存数据
     */
    @PostMapping("/formDataSave")
    public AjaxResponse<?> formDataSave(@RequestParam("taskId") String taskId,
                                        @RequestParam("formDataString") String formDataString) {
        if (test) {
            securityUtil.logInAs("bajie");
        }

        Task task = taskRuntime.task(taskId);

        HashMap<String, Object> variables = new HashMap<>();
        boolean hasVariables = false; // 没有任何参数


        List<HashMap<String, Object>> listMap = new ArrayList<>();

        // 前端传来的字符串，拆分成每个控件
        String[] formDataList = formDataString.split("!_!");
        for (String controlItem : formDataList) {
            String[] formDataItem = controlItem.split("-_!");

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("PROC_DEF_ID_", task.getProcessDefinitionId());
            hashMap.put("PROC_INST_ID_", task.getProcessInstanceId());
            hashMap.put("FORM_KEY_", task.getFormKey());
            hashMap.put("Control_ID_", formDataItem[0]);
            hashMap.put("Control_VALUE_", formDataItem[1]);
            /// hashMap.put("Control_PARAM_", formDataItem[2]);
            listMap.add(hashMap);

            // 构建参数集合
            switch (formDataItem[2]) {
                case "f": // 非参数
                    log.info("控件值不作为参数,Control_PARAM_={}", formDataItem[2]);
                    break;
                case "s": // string
                    variables.put(formDataItem[0], formDataItem[1]);
                    hasVariables = true;
                    break;
                case "t": // datetime
                    DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    variables.put(formDataItem[0], LocalDateTime.parse(formDataItem[2], pattern));
                    hasVariables = true;
                    break;
                case "b": // boolean
                    variables.put(formDataItem[0], BooleanUtils.toBoolean(formDataItem[2]));
                    hasVariables = true;
                    break;
                default:
                    log.error("控件ID：{}的参数{}不存在", formDataItem[0], formDataItem[2]);
            }
        }

        if (hasVariables) {
            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withTaskId(taskId)
                    .withVariables(variables)
                    .build()); // 带参数完成任务
        } else {
            taskRuntime.complete(TaskPayloadBuilder.complete()
                    .withTaskId(taskId)
                    .build());
        }

        // 写入数据库
        activitiMapper.insertFormData(listMap);

        return AjaxResponse.success(listMap);
    }
}
