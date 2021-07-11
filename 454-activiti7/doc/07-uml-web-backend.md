# 第7章 项目：可视化UML工作流引擎web系统：后端接口设计与实现

> 本章讲解PostMan接口调试工具，基于调试工具，编写首页、用户、流程定义、流程实例、任务、历史任务这5模块，超过40个功能点的开发。为我们的工作流系统打好坚实的后台基础，穿插分享编程心得，例如SwaggerUI如何与Activit7和安全框架整合等。 ...

### 7-1 返回值与配置工具类

> AjaxResponse.java 数据返回给前端的处理
>
> PathMapping.java 路径映射，发布不同操作系统路径不同

```java
@Getter
@AllArgsConstructor
public enum ResponseCode {
  SUCCESS(0, "成功"),
  ERROR(1, "错误"),
  ;
  private final int code;
  private final String desc;
}
```

```yaml
activiti7:
  # 测试场景
  test: true
  bpmn-path-mapping: file:~/454-activiti7/src/main/resources/resources/bpmn/
```

```java
@Data
public class AjaxResponse<T> {
  private int code;
  private String msg;
  private T data;

  public static <T> AjaxResponse<T> success(T obj) {
    AjaxResponse<T> ajaxResponse = new AjaxResponse<>();
    ajaxResponse.setCode(ResponseCode.SUCCESS.getCode());
    ajaxResponse.setMsg(ResponseCode.SUCCESS.getDesc());
    ajaxResponse.setData(obj);
    return ajaxResponse;
  }

  public static <T> AjaxResponse<T> error(String msg, T obj) {
    AjaxResponse<T> ajaxResponse = new AjaxResponse<>();
    ajaxResponse.setCode(ResponseCode.SUCCESS.getCode());
    ajaxResponse.setMsg(msg);
    ajaxResponse.setData(obj);
    return ajaxResponse;
  }
}
```

### 7-2 登录接口

SpringSecurity安全配置：ActivitiSecurityConfig.java

登录成功处理类：LoginSuccessHandler.java

```java
@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
  response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  response.setCharacterEncoding(StandardCharsets.UTF_8.name());
  response.getWriter().write(
    objectMapper.writeValueAsString(AjaxResponse.success(authentication))
  );
}
```

登录失败处理类：LoginFailureHandler.java

```java
@Override
public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
  response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
  response.setContentType(MediaType.APPLICATION_JSON_VALUE);
  response.setCharacterEncoding(StandardCharsets.UTF_8.name());
  response.getWriter().write(
    objectMapper.writeValueAsString(AjaxResponse.error("登录失败", e.getLocalizedMessage()))
  );
}
```

### 7-3 流程定义接口（上）

获取流程定义列表

```java
@GetMapping("/getDefinitions")
public AjaxResponse<?> getDefinitions() {
  List<HashMap<String, Object>> listMap = new ArrayList<>();
  List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().list();
  processDefinitionList.sort((x, y) -> y.getVersion() - x.getVersion());

  for (ProcessDefinition processDefinition : processDefinitionList) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("processDefinitionID", processDefinition.getId());
    hashMap.put("name", processDefinition.getName());
    hashMap.put("key", processDefinition.getKey());
    hashMap.put("resourceName", processDefinition.getResourceName());
    hashMap.put("deploymentID", processDefinition.getDeploymentId());
    hashMap.put("version", processDefinition.getVersion());
    listMap.add(hashMap);
  }

  return AjaxResponse.success(listMap);
}
```

### 7-4 流程定义接口（中）

添加流程定义-上传BPMN流媒体

```java
@PostMapping("/uploadStreamAndDeployment")
public AjaxResponse<?> uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile,
                                                 @RequestParam("deploymentName") String deploymentName) throws IOException {
  String fileName = multipartFile.getOriginalFilename(); // 获取上传的文件名
  InputStream fileInputStream = multipartFile.getInputStream(); // 得到输入流（字节流）对象
  String extension = FilenameUtils.getExtension(fileName); // 文件的扩展名

  Deployment deployment;
  if ("zip".equals(extension)) {
    ZipInputStream zip = new ZipInputStream(fileInputStream);
    deployment = repositoryService.createDeployment() // 初始化流程
      .addZipInputStream(zip)
      .name(deploymentName)
      .deploy();
  } else {
    deployment = repositoryService.createDeployment() // ¬初始化流程
      .addInputStream(fileName, fileInputStream)
      .name(deploymentName)
      .deploy();
  }

  return AjaxResponse.success(deployment);
}
```

添加流程定义-部署BPMN字符

```java
@PostMapping("/addDeploymentByString")
public AjaxResponse<?> addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN,
                                             @RequestParam("deploymentName") String deploymentName) {
  Deployment deployment = repositoryService.createDeployment()
    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
    .name(deploymentName)
    .deploy();
  return AjaxResponse.success(deployment);
}
```

### 7-5 流程定义接口（下）

获取流程定义XML

```java
@GetMapping("/getDefinitionXML")
public AjaxResponse<?> getProcessDefineXML(HttpServletResponse response,
                                           @RequestParam("deploymentId") String deploymentId,
                                           @RequestParam("resourceName") String resourceName) throws IOException {
  InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
  String processDefineXML = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
  return AjaxResponse.success(processDefineXML);
}
```

获取流程部署列表

```java
@GetMapping("/getDeployments")
public AjaxResponse<?> getDeployments() {
  List<HashMap<String, Object>> listMap = new ArrayList<>();
  List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
  for (Deployment deployment : deploymentList) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("id", deployment.getId());
    hashMap.put("name", deployment.getName());
    hashMap.put("deploymentTime", deployment.getDeploymentTime());
    listMap.add(hashMap);
  }

  return AjaxResponse.success(listMap);
}
```

格式化输出时间格式数据

删除流程定义

```java
@DeleteMapping("/deleteDefinition")
public AjaxResponse<?> delDefinition(@RequestParam("deploymentId") String deploymentId) {
  repositoryService.deleteDeployment(deploymentId, true);
  return AjaxResponse.success(null);
}
```

### 7-6 流程实例接口（上）

获取流程实例列表

```java
@GetMapping("/getInstances")
public AjaxResponse<?> getInstances() {
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
```

启动流程实例

```java
@PostMapping("/startProcess")
public AjaxResponse<?> startProcess(@RequestParam("processDefinitionKey") String processDefinitionKey,
                                    @RequestParam("instanceName") String instanceName,
                                    @RequestParam("instanceVariable") String instanceVariable) {
  ProcessInstance processInstance = processRuntime.start(
    ProcessPayloadBuilder.start()
    .withProcessDefinitionKey(processDefinitionKey)
    .withName(instanceName)
    //.withVariable("参数", "参数的值")
    .withBusinessKey("自定义BusinessKey")
    .build());
  return AjaxResponse.success(processInstance.getId() + ";" + processInstance.getName());
}
```

### 7-7 流程实例接口（下）

删除流程实例

```java
@DeleteMapping("/deleteInstance")
public AjaxResponse<?> deleteInstance(@RequestParam("instanceId") String instanceId) {
  ProcessInstance processInstance = processRuntime.delete(
    ProcessPayloadBuilder.delete()
    .withProcessInstanceId(instanceId)
    .build()
  );
  return AjaxResponse.success(processInstance.getName());
}
```

挂起流程实例

```java
@PostMapping("/suspendInstance")
public AjaxResponse<?> suspendInstance(@RequestParam("instanceId") String instanceId) {
  ProcessInstance processInstance = processRuntime.suspend(
    ProcessPayloadBuilder.suspend()
    .withProcessInstanceId(instanceId)
    .build()
  );
  return AjaxResponse.success(processInstance.getName());
}
```

激活流程实例

```java
@PostMapping("/resumeInstance")
public AjaxResponse<?> resumeInstance(@RequestParam("instanceId") String instanceId) {
  ProcessInstance processInstance = processRuntime.resume(
    ProcessPayloadBuilder.resume()
    .withProcessInstanceId(instanceId)
    .build()
  );
  return AjaxResponse.success(processInstance.getName());
}
```

获取流程参数

```java
@GetMapping("/variables")
public AjaxResponse<?> variables(@RequestParam("instanceId") String instanceId) {
  List<VariableInstance> variableInstanceList = processRuntime.variables(ProcessPayloadBuilder.variables()
    .withProcessInstanceId(instanceId)
    .build());
  return AjaxResponse.success(variableInstanceList);
}
```

### 7-8 工作任务接口

我的待办任务

```java
@GetMapping("/getTasks")
public AjaxResponse<?> getTasks() {
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
```

完成待办任务

```java
@PostMapping("/completeTask")
public AjaxResponse<?> completeTask(@RequestParam("taskId") String taskId) {
  Task task = taskRuntime.task(taskId);
  // 执行人为空，先进性拾取操作
  if (task.getAssignee() == null) {
    taskRuntime.claim(TaskPayloadBuilder.claim()
                      .withTaskId(task.getId()).build());
  }

  taskRuntime.complete(TaskPayloadBuilder.complete()
                       .withTaskId(task.getId())
                       //.withVariable("参数", "参数的值")
                       .build());
  return AjaxResponse.success(true);
}
```

### 7-9 历史查询接口

用户历史任务

```java
@GetMapping("/getTasks")
public AjaxResponse<?> getTasks(@AuthenticationPrincipal UserInfoBean userInfoBean) {
  List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
    .orderByHistoricTaskInstanceEndTime().desc()
    .taskAssignee(userInfoBean.getUsername())
    .list();
  return AjaxResponse.success(historicTaskInstances);
}
```

实例历史任务

```java
@GetMapping("/getTasksByInstanceId")
public AjaxResponse<?> getTasksByInstanceId(@RequestParam("instanceId") String instanceId) {
  List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
    .orderByHistoricTaskInstanceEndTime().desc()
    .processInstanceId(instanceId)
    .list();
  return AjaxResponse.success(historicTaskInstances);
}
```

### 7-10 动态表单渲染方案

动态表单：使用拼接字符串的方式描述表单信息

普通表单：需要设置businessKey以及任务与表单一对一的关系

什么是动态表单：可以将数据描述渲染成页面的功能，主要包括控件类型、控件数据、数据归属于哪个业务流。

Activiti中的表单

在BPMNJS中绘制表单字段，然后使用接口代码获取表单字段

Process_dynamic_form.bpmn

```java
@GetMapping("/formDataShow")
public AjaxResponse<?> formDataShow1(@RequestParam("taskId") String taskId) {
  Task task = taskRuntime.task(taskId);
  UserTask userTask = (UserTask) repositoryService.getBpmnModel(task.getProcessDefinitionId())
    .getFlowElement(task.getFormKey());
  List<FormProperty> formPropertyList = userTask.getFormProperties();
  return AjaxResponse.success(formPropertyList);
}
```

### 7-11 动态表单渲染接口

#### Activiti表单字段约定内容

表单控件命名约束：`FormProperty_Oueitp2-_!类型-_!名称-_!默认值-_!是否参数`

说明：

- 默认值：无、字符常量、FormProperty_开头定义过的控件ID
- 是否参数：f为不是参数，s是字符，t是时间(不需要int，因为int可等价于string)

例子：Process_dynamic_form2.bpmn

```
FormProperty_34g917h-_!string-_!姓名-_!请输入姓名-_!f
FormProperty_23loph9-_!int-_!年龄-_!请输入年龄-_!s
```

注意：表单Key必须要和任务编号一模一样，因为参数需要任务Key，但是无法获取，只能获取表单Key`task.getFormKey()`当做任务Key

```java
for (FormProperty formProperty : formPropertyList) {
  String[] splitFormProperty = formProperty.getId().split("-_!");
  HashMap<String, Object> hashMap = new HashMap<>();
  hashMap.put("id", splitFormProperty[0]);
  hashMap.put("controlType", splitFormProperty[1]);
  hashMap.put("controlLabel", splitFormProperty[2]);
  hashMap.put("controlDefValue", splitFormProperty[3]);
  hashMap.put("controlParam", splitFormProperty[4]);
  listMap.add(hashMap);
}
```

### 7-12 动态表单解析提交数据

> 解析提交数据
>
> 提交表单内容写入数据库
>
> 参数数据作为UEL表达式参数

Activiti表单提交参数约定内容

动态表单提交内容约定：

formData：`控件ID-_!控件值-_!是否参数!_!控件ID-_!控件值-_!是否参数`

例子：

```
FormProperty_34g917h-_!不是参数-_!f!_!FormProperty_23loph9-_!我是参数-_!s
```

```java
@PostMapping("/formDataSave")
public AjaxResponse<?> formDataSave(@RequestParam("taskId") String taskId,
                                    @RequestParam("formData") String formData) {
  Task task = taskRuntime.task(taskId);

  List<HashMap<String, Object>> listMap = new ArrayList<>();
  // 前端传来的字符串，拆分成每个控件
  String[] formDataList = formData.split("!_!");
  for (String controlItem : formDataList) {
    String[] formDataItem = controlItem.split("-_!");
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("PROC_DEF_ID_", task.getProcessDefinitionId());
    hashMap.put("PROC_INST_ID_", task.getProcessInstanceId());
    hashMap.put("FORM_KEY_", task.getFormKey());
    hashMap.put("Control_ID_", formDataItem[0]);
    hashMap.put("Control_VALUE_", formDataItem[1]);
    hashMap.put("Control_PARAM_", formDataItem[2]);
    listMap.add(hashMap);
  }

  return AjaxResponse.success(listMap);
}
```

### 7-13 动态表单提交入库

写入表单

```java
@Insert("<script>" +
        "    INSERT INTO formdata (PROC_DEF_ID_, PROC_INST_ID_, FORM_KEY_, Control_ID_, Control_VALUE_)" +
        "    VALUES" +
        "    <foreach collection=\"formDataList\" item=\"formData\" index=\"index\" separator=\",\">" +
        "        (#{formData.PROC_DEF_ID_,jdbcType=VARCHAR}," +
        "        #{formData.PROC_INST_ID_,jdbcType=VARCHAR}," +
        "        #{formData.FORM_KEY_,jdbcType=VARCHAR}," +
        "        #{formData.Control_ID_,jdbcType=VARCHAR}," +
        "        #{formData.Control_VALUE_,jdbcType=VARCHAR})" +
        "    </foreach>" +
        "</script>")
int insertFormData(@Param("formDataList") List<HashMap<String, Object>> formDataList);
```

### 7-14 动态表单UEL表达式

Process_param_form.bpmn

```
FormProperty_22gpv37-_!string-_!姓名-_!请填写姓名-_!f
FormProperty_3lpm5pm-_!long-_!年龄-_!无-_!s
```

```
FormProperty_22gpv37-_!八戒-_!f!_!FormProperty_3lpm5pm-_!99-_!s
```

```java
@PostMapping("/formDataSave")
public AjaxResponse<?> formDataSave(@RequestParam("taskId") String taskId,
                                    @RequestParam("formDataString") String formDataString) {
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
```

### 7-15 动态表单读取历史数据接口

查询本实例所有保存的表单数据

```java
@Select("SELECT Control_ID_, Control_VALUE_ FROM formdata WHERE PROC_INST_ID_ = #{procInstId}")
List<HashMap<String, Object>> selectFormData(@Param("procInstId") String procInstId);
```

```java
@GetMapping("/formDataShow")
public AjaxResponse<?> formDataShow(@RequestParam("taskId") String taskId) {
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
```

### 7-16 动态表单读取历史数据操作

Process_input_data.bpmn

八戒输入数据

```
FormProperty_18mijf9-_!string-_!姓名-_!请输入姓名-_!f
FormProperty_0faft3o-_!string-_!性别-_!男女-_!f
```

悟空查看数据

```
FormProperty_35074q9-_!string-_!姓名-_!写死的姓名-_!f
FormProperty_1u3bmda-_!string-_!性别-_!FormProperty_0faft3o-_!f
```

八戒前端提交的数据内容

```
FormProperty_18mijf9-_!我是八戒-_!f!_!FormProperty_0faft3o-_!男-_!f
```

### 7-17 高亮历史流程渲染接口（上）

灰色：办理过的节点

绿色：我办理过的节点

黄色：当前流程停留的节点

Process_highline.bpmn

### 7-18 高亮历史流程渲染接口（下）

```java
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

  // 5. 当前用户已完成的任务
  List<HistoricTaskInstance> taskInstanceList = historyService.createHistoricTaskInstanceQuery()
    .taskAssignee(userInfoBean.getUsername())
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
```

### 7-19 自定义用户控件

获取用户列表<姓名, 账号>

```java
@Select("SELECT name, username FROM user")
List<HashMap<String, Object>> selectUser();
```

### 7-20 统计查询语句

```mysql
-- 流程定义数
SELECT COUNT(ID_) FROM ACT_RE_PROCDEF;
-- 进行中的流程实例
SELECT COUNT(DISTINCT PROC_INST_ID_) FROM ACT_RU_EXECUTION;
-- 查询流程定义产生的流程实例数
SELECT p.NAME_, COUNT(DISTINCT e.PROC_INST_ID_) AS PiNUM
FROM ACT_RU_EXECUTION AS e
         RIGHT JOIN ACT_RE_PROCDEF AS p ON e.PROC_DEF_ID_ = p.ID_
WHERE p.NAME_ IS NOT NULL
GROUP BY p.NAME_;
```

