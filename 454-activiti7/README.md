# Activiti7精讲&Java通用型工作流开发实战

> 掌握Activiti工作流引擎，助力减少硬编码，高效适应需求变更

### 第1章 清晰的学习目标，让学习更轻松

> 首先项目演示了解项目整体情况，便于小伙伴通过本课程学习，能够最终做出的怎样的项目效果。同时，明确学习activiti工作流对今后工作的重要性，确立学习目标。

- 1-1 玩转黑马项目，“技术+业务”能力齐飞

### 第2章 开发前准备：环境搭建篇【选修】

> 所谓“君欲善其事，必先利其器”，因此本章带着小伙伴们将课程所需环境一一构建起来，小伙伴们可以结合自己情况，有选择有重点的去学习。注意：MySQL安装时讲解了如何开启远程访问、设置电脑防火墙；Maven国内镜像仓库修改等。...

- 2-1 JDK安装
- 2-2 Maven安装
- 2-3 MySQL安装
- 2-4 Linux中安装MySQL
- 2-5 SpringBoot项目创建
- 2-6 BPMN插件

### 第3章 项目从git下载与打包部署

> 很多小伙伴在别的实战课程中的反馈：不知道如何下载源码，在本地或者部署到云等环境中，本章特别采用“倒叙”方式，方便小伙伴正确在仓库中下载源码，并在本地或者云环境部署，感受一下项目再进一步深入学习。

- 3-1 源码下载与运行

### 第4章 精讲最新版Activiti7核心组件【工程化思维，助你成为不敢想的团队中流砥柱】 

> 所谓“九层之台起于垒土”，想要完成后续大项目《企业级可视化Activiti工作流系统》的开发任务，抓住核心将事半功倍，即：打通antiviti工作流引擎的任督二脉。本章精讲流程发布、启动；任务的创建、流转、完成，查询等，须注意领悟其背后工程化思维。...

- 4-1 Activiti7介绍
- 4-2 BPMN2.0标准
- 4-3 Springboot与Activit7整合
- 4-4 流程部署Deployment
- 4-5 流程定义ProcessDefinition
- 4-6 流程实例ProcessInstance
- 4-7 任务处理Task（上）
- 4-8 任务处理Task（下）
- 4-9 历史任务HistoryService
- 4-10 UEL表达式(上)
- 4-11 UEL表达式(中)
- 4-12 UEL表达式(下)
- 4-13 流程网关-并行
- 4-14 流程网关-排他
- 4-15 流程网关-包容

### 第5章 Activiti 7 新特性尝鲜 试看

> 本章开始学点“硬”技能，有难度，希望小伙伴们秉承不抛弃不放弃原则，死磕到底，顺利通关。由于Activiti7集成了SpringSecurity安全框架，因此本章先讲解安全框如何集成、配置，访问权限如何写，请求处理，用户与权限分配等。 ...

- 5-1 API新特性ProcessRuntime（上）
- 5-2 API新特性ProcessRuntime（中）
- 5-3 API新特性ProcessRuntime（下）
- 5-4 API新特性TaskRuntime
- 5-5 SpringSecurity用户登录（上）
- 5-6 SpringSecurity用户登录（下）
- 5-7 SpringSecurity配置
- 5-8 SpringSecurity登录响应
- 5-9 BPMN-JS整合

### 第6章 项目：可视化UML工作流引擎web系统：需求分析与设计

> 项目开发前的准备，对页面功能使用设计软件绘制原型，并分析需求，规划接口类，类参数与返回值设计，建立数据库，用户表、业务表等功能设计，磨刀不误砍柴工，做好规划在动手写代码，做到胸有成竹。

- 6-1 页面功能设计
- 6-2 数据库设计
- 6-3 流程定义接口设计(上)
- 6-4 流程定义接口设计（下）
- 6-5 流程实例接口设计
- 6-6 任务接口设计
- 6-7 历史数据接口设计

### 第7章 项目：可视化UML工作流引擎web系统：后端接口设计与实现

> 本章讲解PostMan接口调试工具，基于调试工具，编写首页、用户、流程定义、流程实例、任务、历史任务这5模块，超过40个功能点的开发。为我们的工作流系统打好坚实的后台基础，穿插分享编程心得，例如SwaggerUI如何与Activit7和安全框架整合等。 ...

- 7-1 返回值与配置工具类
- 7-2 登录接口
- 7-3 流程定义接口（上）
- 7-4 流程定义接口（中）
- 7-5 流程定义接口（下）
- 7-6 流程实例接口（上）
- 7-7 流程实例接口（下）
- 7-8 工作任务接口
- 7-9 历史查询接口
- 7-10 动态表单渲染方案
- 7-11 动态表单渲染接口
- 7-12 动态表单解析提交数据
- 7-13 动态表单提交入库
- 7-14 动态表单UEL表达式
- 7-15 动态表单读取历史数据接口
- 7-16 动态表单读取历史数据操作
- 7-17 高亮历史流程渲染接口（上）
- 7-18 高亮历史流程渲染接口（下）
- 7-19 自定义用户控件
- 7-20 统计查询语句

### 第8章 项目：可视化UML工作流引擎web系统：前端页面设计与实现

> 前端主要基于LayUI-mini+BPMN-JS，将掌握LayUI组件使用，ajax请求接口数据并渲染展示到页面上等。BPMN-JS首先基于官网中功能接口，并结合项目扩展官网接口、高亮显示接口可以绘制历史流程，元素点击事件可以绑定表单等操作。

- 8-1 BPMNJS扩展-BPMN下载(上)
- 8-2 BPMNJS扩展-BPMN下载（下）
- 8-3 BPMNJS扩展-BPMN在线部署
- 8-4 BPMNJS扩展-BPMN导入（上）
- 8-5 BPMNJS扩展-BPMN导入（下）
- 8-6 BPMNJS扩展-查看与高亮历史
- 8-7 layuimini部署
- 8-8 登录页面
- 8-9 列表页面
- 8-10 前端与接口
- 8-11 动态表格

### 第9章 部署上线【适用于简历项目演示】

> 将在Linux和Windows上部署，配置外网访问权限，方便小伙伴作为简历项目展示。

- 9-1 jar包与war包..3
- 9-2 Linux部署
- 9-3 Windows部署
- 9-4 达梦数据库支持

### 第10章 项目复盘：企业级常见业务流程设计最佳实践（通用）

> 针对工作中遇到复杂极端业务流程，给出使用Activiti解决流程的方案，包括但不限于样品与质量控制子流程，网格化系统汇总任务平级审核、上级交办、下级分派、越级上报等场景，让小伙伴们能根据复杂的业务场景举一反三，在日后的工作中无往不利。...

- 10-1 章节概述
- 10-2 通知与默认审核人-任务监听器
- 10-3 记录环节执行多久-执行监听器
- 10-4 超时提醒-定时事件(1)
- 10-5 预案启动多部门协同-信号事件
- 10-6 提交后取回任务-消息事件
- 10-7 付款失败请重试-错误事件
- 10-8 追加质控结果-补偿事件
- 10-9 手工任务、服务任务等
- 10-10 财务审核每次都一样-调用子流程
- 10-11 会签与多小组协作-多实例任务（上）
- 10-12 会签与多小组协作-多实例任务（下）

### 第11章 课程总结：他山之石，可以攻玉

> 对课程的总结，强调课程总最重要的知识点和最容易出错的地方，愿小伙伴们：所学皆有用，扬帆起航，升职加薪，为自己的锦绣前程做铺垫！

- 11-1 总结回顾