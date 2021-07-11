# 第2章 开发前准备：环境搭建篇【选修】

> 所谓“君欲善其事，必先利其器”，因此本章带着小伙伴们将课程所需环境一一构建起来，小伙伴们可以结合自己情况，有选择有重点的去学习。注意：MySQL安装时讲解了如何开启远程访问、设置电脑防火墙；Maven国内镜像仓库修改等。...

### 2-1 JDK安装

### 2-2 Maven安装

Activiti7的POM坐标

- 修改默认仓库

  ```shell
  vim ~/.m2/settings.xml
  ```

  ```xml
  <mirror>
    <id>nexus-aliyun</id>
    <mirrorOf>central</mirrorOf>
    <name>Nexus aliyun</name>
    <url>http://maven.aliyun.com/nexus/content/groups/public</url>
  </mirror>
  ```

- 添加pom依赖

  > https://github.com/Activiti/Activiti
  >
  > https://activiti.gitbook.io/activiti-7-developers-guide/

  ```xml
  <dependency>
    <groupId>org.activiti</groupId>
    <artifactId>activiti-spring-boot-starter</artifactId>
  </dependency>
  ```

  ```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.activiti.dependencies</groupId>
        <artifactId>activiti-dependencies</artifactId>
        <version>7.1.0.M5</version>
        <scope>import</scope>
        <type>pom</type>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

### 2-3 MySQL安装

- 修改MySQL大小写敏感（Windows可忽略）

  ```shell
  vim /etc/my.cnf # 进入MySQL配置
  systemctl restart mysqld # 修改配置后需重启MySQL
  ```

  ```
  lower_case_table_names = 1 # 0大小写敏感，1大小写不敏感
  ```

  注：Activiti建表语句是小写，操作表代码是大写

- 确保数据库没有Activiti以前的库

- 补充Activiti7-M5版本缺失的字段

### 2-4 Linux中安装MySQL

### 2-5 SpringBoot项目创建

#### SpringBoot环境准备

- 添加yml配置文件：数据库配置
- SpringBoot的HelloWorld

### 2-6 BPMN插件

#### IDEA中BPMN绘制插件与请假流程制作

- IDEA绘制BPMN2.0插件actiBPM安装

  https://plugins.jetbrains.com/plugin/7429-actibpm

- 绘制请假流程业务图

#### IDEA中BPMN解决乱码

- 修改vmoptions文件

  Help->Edit Custom VM Option...

- 添加`-Dfile.encoding=UTF-8`



# UML建模教程

#### 本课程能学到什么

- UML基础概念与使用场景

- 各类UML流程图绘制

  时序图、活动图

- UML从业务流程图到流程开发

  BPMN业务图、在IDEA下绘制BPMN业务图

- Activiti7流程开发

  核心API、编码请假流程流转

#### 需要准备的工具

UML绘图工具：IBM Rose、StarUML、Visio、**PowerDesigner**

编码工具：IDEA

数据库：MySQL5.x以上

#### UML图的种类

- 结构图：类图、组合结构图、构件图、部署图、对象图、包图
- 行为图：活动图、**时序图**、用例图、状态图

### 一、时序图

> 时序图通过描述对象之间发送消息的时间顺序来显示多个对象之间的动态协作
>
> 时序图能清晰的表达出有哪些参与者、提供什么样的服务、参与者之间发送请求时传递什么样的信息载体，能够看出每个参与者有什么职能

- 描述对象之间的动态状态

- 参与者、服务、传递信息载体

- 使用场景

  需求分析：业务流程

  程序设计：程序之间调用逻辑关系

#### 时序图类型

业务流程时序图、功能流程时序图、页面流程时序图、数据流程时序图

#### 时序图要素

<img src="https://tva1.sinaimg.cn/large/008eGmZEgy1goqk2kjtu2j30os0l4acd.jpg" alt="时序图要素" style="zoom:30%;" />

- 角色Actor：可以是人或其他系统，只能有一个角色
- 对象Object：交互主体所扮演的身份
- 消息Message：实体之间传递信息，允许实体请求其他服务，对象通过发送或接收信息进行通讯
- 控制焦点Activation：对象执行的一项操作
- 生命线Lifeline：对象在一段时期内的存在

### 二、活动图

> 活动图强调活动到活动的流程控制，时序图强调对象到对象的流程控制
>
> 活动图可以进行过程、实例、工程流建模

#### 活动图工具

起始节点、结束节点、活动/行为、网关/决策、流程箭头、角色泳道