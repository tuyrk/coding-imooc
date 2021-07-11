# 第6章 项目：可视化UML工作流引擎web系统：需求分析与设计

> 项目开发前的准备，对页面功能使用设计软件绘制原型，并分析需求，规划接口类，类参数与返回值设计，建立数据库，用户表、业务表等功能设计，磨刀不误砍柴工，做好规划在动手写代码，做到胸有成竹。

### 6-1 页面功能设计

- 页面设计
- 数据库设计
- 接口设计

工作流引擎（Work Flow）作为应用系统的一部分，为应用系统的业务流转服务

项目开发前需要做哪些工作

> 可行性研究报告、需求调研、建设方案、原型设计与效果图

### 6-2 数据库设计

数据库设计注意事项

- 主键与外键

  主键用来明确唯一性，一般用ID、Auto- increment、UUID，也有多主键情况

- 一对多、多对多关系

- 中间表的使用

动态表单存值

```mysql
CREATE TABLE IF NOT EXISTS `FORMDATA`
(
  `PROC_DEF_ID_`   VARCHAR(64)   DEFAULT NULL COMMENT '流程定义ID',
  `PROC_INST_ID_`  VARCHAR(64)   DEFAULT NULL COMMENT '流程实例ID',
  `FORM_KEY_`      VARCHAR(255)  DEFAULT NULL COMMENT '表单KEY',
  `CONTROL_ID_`    VARCHAR(100)  DEFAULT NULL COMMENT '控件ID',
  `CONTROL_VALUE_` VARCHAR(2000) DEFAULT NULL COMMENT '控件值'
) ENGINE = InnoDB COMMENT '表单业务数据';
```

### 6-3 流程定义接口设计(上)

上传BPMN流媒体/uploadStreamAndDeployment

```
POST http://localhost:8080/processDefinition/uploadStreamAndDeployment
{
  "processFile": "BPMN文件",
  "deploymentName": "流程定义的名字"
}
```

部署BPMN字符/addDeploymentByString

```
POST http://localhost:8080/processDefinition/addDeploymentByString
{
  "stringBPMN": "BPMN文件的XML",
  "deploymentName": "流程定义的名字"
}
```

获取流程定义列表/getDefinitions

```
GET http://localhost:8080/processDefinition/getDefinitions
```

获取流程定义XML/getDefinitionXML

```
GET http://localhost:8080/processDefinition/getDefinitionXML?deploymentId=&resourceName=
```

获取流程部署列表/getDeployments

```
GET http://localhost:8080/processDefinition/getDeployments
```

删除流程部署(同时删除流程定义)/delDefinition

```
DELETE http://localhost:8080/processDefinition/delDefinition
{
  "deploymentId": "流程部署ID"
}
```

### 6-4 流程定义接口设计（下）

### 6-5 流程实例接口设计

获取流程实例列表/getInstances

```
GET http://localhost:8080/processInstance/getInstances
```

启动流程实例/startProcess

```
POST http://localhost:8080/processInstance/startProcess
{
  "processDefinitionKey": "流程定义Key",
  "instanceName": "流程实例名称",
  "instanceVariable": "流程实例参数"
}
```

删除流程实例/deleteInstance

```
DELETE http://localhost:8080/processInstance/deleteInstance
{
  "instanceId": "流程实例ID"
}
```

挂起流程实例/suspendInstance

```
POST http://localhost:8080/processInstance/suspendInstance
{
  "instanceId": "流程实例ID"
}
```

激活流程实例/resumeInstance

```
POST http://localhost:8080/processInstance/resumeInstance
{
  "instanceId": "流程实例ID"
}
```

获取流程参数/variables

```
GET http://localhost:8080/processInstance/variables?instanceId
```

### 6-6 任务接口设计

我的待办任务/getTasks

```
GET http://localhost:8080/task/getTasks
```

完成待办任务/completeTask

```
POST http://localhost:8080/task/completeTask
{
  "taskId": "任务ID"
}
```

渲染动态任务表单/formDataShow

```
GET http://localhost:8080/task/formDataShow?taskId
```

保存动态任务表单/formDataSave

```
POST http://localhost:8080/task/formDataSave
{
  "taskId": "任务ID",
  "formDataString": "页面所有控件的ID和值组成的字符串"
}
```

### 6-7 历史数据接口设计

用户历史任务/getTasksByUsername

```
GET http://localhost:8080/activitiHistory/getTasks
```

实例历史任务/getTasksByInstanceID

```
GET http://localhost:8080/activitiHistory/getTasksByInstanceID?instanceId
```

高亮渲染流程实例历史/getHighLine

```
GET http://localhost:8080/activitiHistory/getHighLine?instanceId
```

