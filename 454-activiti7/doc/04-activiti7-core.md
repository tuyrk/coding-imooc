# 第4章 精讲最新版Activiti7核心组件【工程化思维，助你成为不敢想的团队中流砥柱】 

> 所谓“九层之台起于垒土”，想要完成后续大项目《企业级可视化Activiti工作流系统》的开发任务，抓住核心将事半功倍，即：打通antiviti工作流引擎的任督二脉。本章精讲流程发布、启动；任务的创建、流转、完成，查询等，须注意领悟其背后工程化思维。...

### 4-1 Activiti7介绍

> Activiti项目是基于Apache License许可的开源项目
>
> Activiti项目前主管Tom Baeyens，2010年立项
>
> > Activiti共3个版本，5、6、7。Activiti是基于JBPM5开发的衍生产品
>
> Activiti项目支持BPMN标准

#### Activiti7新特性介绍

- 与SpringBoot更好的原生支持
- 引入SpringSecurity作为默认用户与角色的默认安全机制
- 核心API进行了封装
- 对云发布、分布式的支持等

> ProcessEngine：RepositoryService、RuntimeService、ManagementService、TaskService、HistoryService、~~FormService~~、~~IdentityService~~
> ===>
> ProcessRuntime、TaskRuntime

#### 工作流常见业务场景介绍

- 线性审批

  <img src="https://tva1.sinaimg.cn/large/008eGmZEly1gork5qzr38j30ui05ldgx.jpg" alt="线性审批" style="zoom:40%;" />

- 会签审批

  <img src="https://tva1.sinaimg.cn/large/008eGmZEly1gork73xpkjj30zp0h7wgs.jpg" alt="会签审批" style="zoom:50%;" />

- 条件流程

  <img src="https://tva1.sinaimg.cn/large/008eGmZEly1gorka6pflaj30zi0edac2.jpg" alt="条件流程" style="zoom:50%;" />

### 4-2 BPMN2.0标准

#### BPMN2.0业务图简介

> BPMN：业务流程建模标注（Business Process Model and Notation）
>
> OMG：对象管理组织（The Object Management Group）国际性的、开放式会员制的非营利性技术标准联盟

#### BPMN2.0图常见工具解释

| 英文             | 中文       | 英文          | 中文          |
| ---------------- | ---------- | ------------- | ------------- |
| StartEvent       | 事件开始点 | UserTask      | 活动          |
| EndEvent         | 事件结束点 | SubProcess    | 子流程        |
| ParallelGateway  | 并行网关   | Pool          | 泳道          |
| ExclusiveGateway | 排他网关   | BoundaryEvent | 边界/自动事件 |
| InclusiveGateway | 包含网关   |               |               |
| EventGateway     | 事件网关   |               |               |

活动图是技术性的，而BPMN是业务性的

#### Activiti7对BPMN的自动转化

定义流程、发布实例、执行任务、监控报警、统计优化

### 4-3 Springboot与Activit7整合

Activiti7从M5版本开始，每次启动项目便会向ACT_RE_DEPLOYMENT添加一条数据

Activiti7启动项目将会自动创建的数据库表

```properties
# Activiti7历史表创建
spring.activiti.db-history-used=true
spring.activiti.history-level=full
# 自动部署验证设置:true-开启（默认）、false-关闭
spring.activiti.check-process-definitions=false
```

### 4-4 流程部署Deployment

> Activiti7经典类：
>
> - 流程部署Deployment
> - 流程定义ProcessDefinition
> - 流程实例ProcessInstance
> - 任务处理Task
> - 历史任务HistoricTaskInstance

#### 课程内容

> 绘制BPMN图：指定流程key、流程名称、任务指定执行人
>
> 上传BPMN、图片、Zip
>
> 流程部署的增删改查
>
> 查询BPMN列表和xml

通过bpmn部署流程

```java
String filename = "BPMN/Part1_Deployment.bpmn";
// String pngname = "BPMN/Part1_Deployment.png";
Deployment deployment = repositoryService.createDeployment()
  .addClasspathResource(filename)
  // .addClasspathResource(pngname)
  .name("流程部署测试BPMN")
  .deploy();
```

通过ZIP部署流程

```java
File file = ResourceUtils.getFile("classpath:BPMN/Part1_DeploymentV2.zip");
System.out.println(file.getAbsolutePath());
InputStream fis = new FileInputStream(file);
ZipInputStream zip = new ZipInputStream(fis);
Deployment deployment = repositoryService.createDeployment()
  .addZipInputStream(zip)
  .name("流程部署测试zip")
  .deploy();
System.out.println(deployment);
```

查询流程部署

```java
List<Deployment> deploymentList = repositoryService.createDeploymentQuery().list();
for (Deployment deployment : deploymentList) {
  System.out.println("Id：" + deployment.getId());
  System.out.println("Name：" + deployment.getName());
  System.out.println("DeploymentTime：" + deployment.getDeploymentTime());
  System.out.println("Key：" + deployment.getKey());
}
```

### 4-5 流程定义ProcessDefinition

> Deployment：添加资源文件、获取部署信息、部署时间
>
> ProcessDefinition：获取版本号、key、资源名称、部署ID等

查询流程定义

```java
List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
for (ProcessDefinition pd : list) {
  System.out.println("Id：" + pd.getId());
  System.out.println("Name：" + pd.getName());
  System.out.println("Key：" + pd.getKey());
  System.out.println("ResourceName：" + pd.getResourceName());
  System.out.println("DeploymentId：" + pd.getDeploymentId());
  System.out.println("Version：" + pd.getVersion());
}
```

删除流程定义

```java
String pdID = "44b15cfe-ce3e-11ea-92a3-dcfb4875e032";
// cascade为true，表示级联删除所有的流程实例、任务、历史；false会保留历史数据
repositoryService.deleteDeployment(pdID, true);
```

### 4-6 流程实例ProcessInstance

> ProcessDefinition与ProcessInstance是一对多的关系
>
> 理解为：行动计划与具体行动的关系

初始化流程实例

> businessKey为业务流水ID，把业务数据与Activiti7流程数据关联

```java
ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
```

获取流程实例列表

```java
List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
for (ProcessInstance pi : list) {
  System.out.println("ProcessInstanceId：" + pi.getProcessInstanceId());
  System.out.println("ProcessDefinitionId：" + pi.getProcessDefinitionId());
  System.out.println("isEnded：" + pi.isEnded());
  System.out.println("isSuspended：" + pi.isSuspended());
}
```

暂停与激活流程实例

```java
runtimeService.suspendProcessInstanceById(processInstanceId);
runtimeService.activateProcessInstanceById(processInstanceId);
```

删除流程实例

```java
runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
```

### 4-7 任务处理Task（上）

#### 任务的类型

> 任务、发送任务、接受任务、手工任务、业务规则任务、服务任务、脚本任务、调用活动、子流程(折叠)、子流程(展开)

任务的图形化是以矩形为基础，在左侧添加具体的图标，用来描述一种特定任务类型。

用户任务需要人来参与，需要人为触发。

#### 用户任务的属性面板

> Assignee：执行人/代理人。可通过流程变量赋值
>
> Candidate Users：候选人。设置多人时需用逗号分隔
>
> Candidate Groups：候选组
>
> Due Date：任务到期时间。自动触发定时任务

任务查询

```java
List<Task> taskList = taskService.createTaskQuery().list();
for (Task task : taskList) {
  System.out.println("Id：" + task.getId());
  System.out.println("Name：" + task.getName());
  System.out.println("Assignee：" + task.getAssignee());
}
```

查询我的代办任务

```java
List<Task> taskList = taskService.createTaskQuery()
  .taskAssignee("bajie")
  .list();
```

执行任务

```java
taskService.complete(taskId);
```

拾取任务

```java
String candidateUser = "bajie";
List<Task> taskList = taskService.createTaskQuery()
  .taskCandidateUser(candidateUser)
  .list();
Task task = taskService.createTaskQuery().taskId(taskList.get(0).getId()).singleResult();
taskService.claim(task.getId(), candidateUser);
```

归还与交办任务

```java
Task task = taskService.createTaskQuery().taskId("b3d5f8a3-8a3b-11eb-af72-3edce3e71d32").singleResult();
taskService.setAssignee(task.getId(), "null"); // 归还候选任务
taskService.setAssignee(task.getId(), "wukong"); // 交办
```

### 4-8 任务处理Task（下）

### 4-9 历史任务HistoryService

> 历史综合信息：HistoricTaskInstance
>
> 历史变量：HistoricVariableInstance

根据用户名查询历史记录

```java
List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
  .orderByHistoricTaskInstanceEndTime().asc()
  .taskAssignee("bajie")
  .list();
for (HistoricTaskInstance hti : list) {
  System.out.println("Id：" + hti.getId());
  System.out.println("ProcessInstanceId：" + hti.getProcessInstanceId());
  System.out.println("Name：" + hti.getName());
}
```

根据流程实例ID查询历史

```java
List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
  .orderByHistoricTaskInstanceEndTime().asc()
  .processInstanceId("aedaa0e6-8a37-11eb-874f-3edce3e71d32")
  .list();
```

根据流程实例ID查询历史参数

```java
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
```

### 4-10 UEL表达式(上)

> EL为表达式语言（Expression Language）
>
> UEL统一表达式语言（Unified Expression Language）

#### UEL表达式描述

> 表达式以`${`开始，以`}`结束，例如`${day > 10}`
>
> 支持逻辑运算：`${username == "bajie" and password == "123"}`
>
> 支持变量与实体类赋值

注：建议执行流程变量的时候不要进行覆盖操作

#### 对应Activiti7数据表

> ACT_RU_VARIABLE运行时参数表、ACT_HI_VARINST历史参数表

#### UEL表达式的保留关键字

and、or

eq、lt、gt、le、ge

instanceof、div

true、false、empty、not

#### UEL表达式的运算符

| 运算符 | 功能 | 示例                                               | 结果                |
| ------ | ---- | -------------------------------------------------- | ------------------- |
| +      | 加   | ${1+1}                                             | 2                   |
| -      | 减   | ${1-1}                                             | 0                   |
| *      | 乘   | ${2*2}                                             | 4                   |
| /或div | 除   | `${2/1}`或`${2 div 1}`<br />`${2/0}`或`${2 div 0}` | 2<br />Infinity     |
| %或mod | 求余 | `${3%2}`或`${3 mod 2}`<br />`${3%0}`或`${3 mod 0}` | 1<br />异常/by zero |

UEL表达式的作用是：动态对某些变量赋值

主要使用场合：执行人、候选人、候选组、流程变量分支的动态赋值

UEL表达式可赋值环节：任务启动环节、流程实例启动环节、任务完成环节、代码可在任意环节赋值

### 4-11 UEL表达式(中)

带参数启动流程实例，指定执行人

```java
// 流程变量
Map<String, Object> variables = new HashMap<>();
variables.put("ZhiXingRen", "wukong");
ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
```

带参数完成任务，指定流程变量测试

```java
Map<String, Object> variables = new HashMap<>();
variables.put("pay", "101");
taskService.complete(taskId, variables);
```

带参数启动流程实例，使用实体类

```java
@Data
public class UEL_POJO implements Serializable {
  private String zhiXingRen;
  private String pay;
}
```

```java
UEL_POJO uel_pojo = new UEL_POJO();
uel_pojo.setZhiXingRen("bajie");
// 流程变量
Map<String, Object> variables = new HashMap<>();
variables.put("uelpojo", uel_pojo);
ProcessInstance pi = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
```

带参数任务完成环节，指定多个候选人

```java
Map<String, Object> variables = new HashMap<String, Object>();
variables.put("houXuanRen", "wukong,tangseng");
taskService.complete(taskId, variables);
```

直接指定流程变量

```java
runtimeService.setVariable(executionId, variableName, value);
runtimeService.setVariables(executionId, variables);
taskService.setVariable(taskId, variableName, value);
taskService.setVariables(taskId, variables);
```

局部变量

```java
runtimeService.setVariableLocal(executionId, variableName, value);
runtimeService.setVariablesLocal(executionId, variables);
taskService.setVariableLocal(taskId, variableName, value);
taskService.setVariablesLocal(taskId, variables);
```

### 4-12 UEL表达式(下)

### 4-13 流程网关-并行

BPMN2.0网关

> 并行网关：把任务拆分为多路，并把多路任务合并为一路。用于需要多人审核的场景
>
> 排他网关：按照顺序计算条件进行处理。当计算条件为true时继续执行下一个环节，没有满足条件则抛出异常，如果满足多个条件则根据BPMN顺序靠前的任务执行(只能执行一个条件)
>
> 包容网关：能够添加条件的并行网关。可以在多路连接线上设置条件，并且可以执行多条线路
>
> 事件网关：只能连接到事件，且连接的事件必须大于等于2

Activiti7高级应用

> 边界事件、中间事件、子流程

动态多人并行执行任务（人员可自定义）：设置候选人变量(Candidate Users)，勾选多实例-并行(Multi Instance,Sequential)

### 4-14 流程网关-排他



### 4-15 流程网关-包容