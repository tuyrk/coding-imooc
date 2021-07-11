# 第10章 项目复盘：企业级常见业务流程设计最佳实践（通用）

> 针对工作中遇到复杂极端业务流程，给出使用Activiti解决流程的方案，包括但不限于样品与质量控制子流程，网格化系统汇总任务平级审核、上级交办、下级分派、越级上报等场景，让小伙伴们能根据复杂的业务场景举一反三，在日后的工作中无往不利。...

### 10-1 章节概述

监听器：执行监听器、任务监听器

> 功能：提醒、指定执行人

事件：开始与结束事件、中间事件、边界事件

事件类型

- 开始事件：消息开始事件、**定时开始事件**、条件开始事件、信号开始事件
- 结束事件：结束事件、定时结束事件、错误结束事件、补偿结束事件、信号结束事件、终止结束事件

- 中间事件：消息中间抛出事件、定时中间捕获事件、升级中间抛出事件、条件中间捕获事件、链接中间捕获事件、链接中间抛出事件、补偿中间抛出事件、信号中间捕获事件、信号中间抛出事件
- 边界事件：消息边界事件、升级边界事件、条件边界事件、错误边界事件、取消边界事件、信号边界事件、补偿边界事件、消息边界事件(非中断)、定时边界事件(非中断)、升级边界事件(非中断)、条件边界事件(非中断)、信号边界事件(非中断)

任务

- 任务类型：任务、发送任务、接收任务、手工任务
- 多实例任务：业务规则任务、服务任务、脚本任务、调用活动
- 子流程：子流程(折叠的)、子流程(展开的)

### 10-2 通知与默认审核人-任务监听器

监听器：执行监听器、任务监听器

- 相同点：都是在流程执行到某个环节，进行监听并调用一个Java类，运行监听后需要完成的功能

- 不同点：
  1. 执行监听器可以获取到**流程实例**的相关数据(流程实例ID)；任务监听器可以获取到**任务**的相关数据(任务ID、任务名称)
  2. 作为监听器的运行类，执行监听器与任务监听器需要**实现不同的接口**
  3. 监听器的主要作用可以用来**指定执行人**，如果是指定本环节的执行人就需要使用任务监听器；以及**发送通知**，可以可以是短信通知等通知形式
  4. 还可以统计任务执行时长，给任务加两个监听器(开始监听、结束监听)，并记录时间点

任务监听器流程：Process_TaskListener.bpmn

任务监听器1-bajie

```java
public class TkListener1 implements TaskListener {
  @Override
  public void notify(DelegateTask delegateTask) {
    System.out.println("执行人：" + delegateTask.getAssignee());
    // TODO 根据用户名查询用户电话并调用发送短信接口
    delegateTask.setVariable("delegateAssignee", delegateTask.getAssignee());
  }
}
```

任务监听器2-wukong

```java
public class TkListener2 implements TaskListener {
  @Override
  public void notify(DelegateTask delegateTask) {
    System.out.println("执行人2：" + delegateTask.getVariable("delegateAssignee"));
    // TODO 根据执行人username获取组织机构代码，加工后得到领导是wukong
    delegateTask.setAssignee("wukong");
  }
}
```

### 10-3 记录环节执行多久-执行监听器

> 执行监听器作用：存储读取变量、处理业务信息
>
> 常使用的功能：统计处理时长

### 执行监听器属性官方解释

```
getId: 唯一的句柄
△ getProcessInstanceId: 流程实例
getRootProcessInstanceId: 最顶层的流程实例
getEventName: 事件的名称
△ getProcessInstanceBusinessKey: 关联流程实例的业务Key
△ getProcessDefinitionId: 流程定义标识
getParentId: 父级ID
getSuperExecutionId: 调用执行的ID
△ getCurrentActivityId: 当前ActivitiID
getTenantId: 租户ID，多个系统共享数据库使用
getCurrentFlowElement: 当前所在的BPMN元素
```

统计处理任务的时长，并且监听器在调用类时可以传递参数

执行监听器流程：Process_ExecutionListener.bpmn

```java
@Slf4j
public class PiListener implements ExecutionListener {
  @Autowired
  private Expression sendType;

  @Override
  public void notify(DelegateExecution execution) {
    log.info("事件名称:{}", execution.getEventName());
    log.info("流程定义ID:{}", execution.getProcessDefinitionId());
    if ("start".equals(execution.getEventName())) {
      log.info("记录节点开始时间");
    } else if ("end".equals(execution.getEventName())) {
      log.info("记录节点结束时间");
    }
    log.info("sendType:{}", sendType.getValue(execution).toString());
  }
}
```

### 10-4 超时提醒-定时事件(1)

> 定时边界事件、定时边界事件(非中断)

BPMN定时事件使用场景：超时提醒

- 指定日期开启流程实例
- 24小时任务未办理短信提醒
- 3天未审核则主管领导介入

定时事件类型：

- Time date：日期，什么时间触发

  > Process_time_date.bpmn

  ```
  2021-08-30T15:23:59
  ```

- Time duration：持续延时多长时间后触发

  > Process_time_duration.bpmn

  ```
  P1DT1M：一天一分钟执行一次
  P1W：一周执行一次
  PT1H：一小时执行一次
  PT10S：十秒执行一次
  ```

  说明：

  | 标识 | 描述     | 标识 | 描述                 |
  | ---- | -------- | ---- | -------------------- |
  | P    | 开始标记 | T    | 日期和时间的分割标记 |
  | 1Y   | 一年     | 2H   | 两个小时             |
  | 2M   | 两个月   | 30M  | 三十分钟             |
  | 10D  | 十天     | 15S  | 十五秒               |

- Time Cycle：循环，循环规则

  > 

  ```
  R3/2021-07-30T19:12:00/PT1M：循环3次/开始循环时间/每次间隔
  R2/PT1M：执行2次/1分钟执行一次
  R/PT1M/2021-01-01：无限循环/时间间隔/结束时间
  <timeCycle>R/PT1H/${EndTime}</timeCycle>：变量无限循环
  ```

#### 典型场景

- 任务阻塞到时间满足条件

- 非中断会一直产生任务，使用服务任务可以发短信

- 3天未审核则主管领导介入

- 3天未审核则主管领导办理

```properties
# 关闭定时边界事件异步执行，默认为true
spring.activiti.async-executor-activate=false
```

### 10-5 预案启动多部门协同-信号事件

#### 信号事件的捕获与抛出

- Catching捕获事件：Process_signal_catching.bpmn

  任务、流程实例流转到捕获事件后，一般会阻塞，等待别人触发，触发方式有：抛出事件(将消息抛出出去)、通过代码将消息抛出，捕获事件拿到信号之后再执行

- Throwing抛出事件：Process_signal_throwing.bpmn

信号事件是广播；消息事件是一对一通知

信号事件可以放在开始事件、边界事件、中间事件、还支持非中断，非中断一般放一个短信发送服务

#### 信号事件代码触发

```java
runtimeService.signalEventReceived("Signal_16ip2jv")
```

### 10-6 提交后取回任务-消息事件

信号事件是广播，执行时只有一个参数：信号名称；

消息事件有两个参数：消息名称、流程实例ID

#### 消息事件典型应用

- 跨实例进行触发
- 跨应用进行触发

消息事件类型：消息启动、消息中间、消息边界、消息非中断、消息结束

ACT_HI_TASKINST：任务历史表

ACT_RU_EVENT_SUBSCR：事件订阅



消息撤回-撤回任务：Process_message.bpmn

```java
Execution exec = runtimeService.createExecutionQuery()
  .messageEventSubscriptionName("Message_0gu091p")
  .processInstanceId("94797440-dcde-11eb-8e0f-acde48001122")
  .singleResult();
runtimeService.messageEventReceived("Message_0gu091p", exec.getId());
```

消息启动：

```java
runtimeService.startProcessInstanceByMessage("Message_0gu091p");
```

```java
ProcessInstance pi = processRuntime.start(StartMessagePayloadBuilder
                                          .start("Message_0gu091p")
                                          .build()
                                         );
```

### 10-7 付款失败请重试-错误事件

错误事件类型：错误开始、错误边界、错误结束

错误开始事件只能放在事件子流程里，不能单独在开始事件里设置

错误服务任务：Process_error.bpmn

```java
public class ErrServiceTaskListener implements JavaDelegate {
  @Override
  public void execute(DelegateExecution execution) {
    throw new BpmnError("Error_13205hq");
  }
}
```

事件子流程-错误开始事件：Process_error_sub_process.bpmn

### 10-8 追加质控结果-补偿事件

> 补偿事件、取消事件

#### 补偿事件

<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gs6gpd1iztj316t0fygoi.jpg" alt="补偿事件" style="zoom:50%;" />

- 定时事件或消息事件需要任务走到当前节点，事件被触发，表ACT_RU_EVENT_SUBSCR会新增数据，环节执行之后，对应的事件失效，表ACT_RU_EVENT_SUBSCR里的事件也会被删除

- 补偿事件走到了环节会触发，环节执行过后，补偿事件还会在表ACT_RU_EVENT_SUBSCR里，还是激活状态，一直到整个流程实例结束才会结束

**补偿事件的作用**：走到当前的环节并不需要做任何事情，但是在后面的某个时刻发现当前做的事情不足，需要补偿，此时便会执行补偿事件

补偿事件的特点：

1. 任务到达时激活
2. 流程实例结束时挂起（其他事件任务结束时挂起）
3. 子流程需要任务到达补偿节点才能激活，而非子流程开始就激活

#### 取消事件

<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gs6h1vn2fyj30qv0btdgt.jpg" alt="取消事件" style="zoom:80%;" />

事务子流程抛出异常则会触发取消事件

#### BPMN-JS额外事件

> https://www.activiti.org/userguide/

- escalationEventDefinition升级事件
- linkEventDefinition连接事件
- conditionalEventDefinition条件事件

### 10-9 手工任务、服务任务等

> 手工任务、脚本任务scriptTask、业务规则任务businessRuleTask、接收任务receiveTask、发送任务sendTask、服务任务serviceTask

#### 手工任务

<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gs6hfvh1hvj31be0970uj.jpg" alt="手工任务" style="zoom:50%;" />

作用：在系统中留痕迹

#### 脚本任务scriptTask

在不重新发布项目的情况下，进行一些操作。不常用，可使用任务监听、执行监听、服务任务、边界任务代替

#### 业务规则任务businessRuleTask

和脚本任务类似，旨在不重新发布项目的情况下，通过BPMN的相关配置改变业务逻辑。会使用到其他的规则引擎

#### 接收任务receiveTask

类似消息中间事件，任务走到接收任务环节会发生阻塞，可使用代码进行触发

触发语句：runtimeService.trigger(execution.getId());

#### 发送任务sendTask

#### 服务任务serviceTask

> 服务任务用来触发Java脚本类，自动执行某些功能，和监听非常类似

服务任务**使用场景**：发短信、发邮件、调用其他接口

服务任务**注意事项**：自动执行、需要执行类、执行类必须继承JavaDelegate接口

Process_serviceTask.bpmn

```java
public class ServiceTaskListener1 implements JavaDelegate {
  @Override
  public void execute(DelegateExecution execution) {
    // 输出服务任务的一些属性
    System.out.println(execution.getEventName());
    System.out.println(execution.getProcessDefinitionId());
    System.out.println(execution.getProcessInstanceId());
    // 设置流程变量
    execution.setVariable("aa", "bb");
  }
}
```

```java
public class ServiceTaskListener2 implements JavaDelegate {
  @Override
  public void execute(DelegateExecution execution) {
    // 获取流程变量的值
    System.out.println(execution.getVariable("aa"));
  }
}
```

### 10-10 财务审核每次都一样-调用子流程

子流程类型：嵌入子流程、调用子流程

- 主流程和嵌入子流程在同一个BPMN文件中绘制，其作用用来区分功能块。局部通用处理逻辑

- 主流程和被调用的子流程在不同的BPMN文件中，使用场景是被调用的子流程可以复用。可动态加载不同的子流程

<img src="https://tva1.sinaimg.cn/large/008i3skNgy1gs8s33rms2j30yk0ja76c.jpg" alt="子流程类型" style="zoom:50%;" />

调用子流程Process_sub.bpmn、主流程Process_sub_process.bpmn

### 10-11 会签与多小组协作-多实例任务（上）

使用场景：会签(并行)、多子流程业务(并行)、动态顺序审批(串行)

多实例任务完成条件

- 实例总数：nrOfInstances
- 当前还没完成的实例：nrOfActiveInstances
- 已经完成的实例个数：nrOfCompletedInstances

### 10-12 会签与多小组协作-多实例任务（下）

多实例任务Process_multi_instance.bpmn

多实例任务的开始监听

```java
public class MultiInstancesStartListener implements ExecutionListener {
  @Override
  public void notify(DelegateExecution execution) {
    ArrayList<String> assigneeList = new ArrayList<>();
    assigneeList.add("bajie");
    assigneeList.add("wukong");
    assigneeList.add("salaboy");
    execution.setVariable("assigneeList", assigneeList);

    // execution.setVariable("isPass",0);
  }
}
```

```
${isPass ==1 || nrOfCompletedInstances/nrOfInstances>=0.6}
```

多实例并行子流程Process_multi_instance_parallel.bpmn

多实例并行子流程任务监听器

```java
public class MultiInstancesTKListener implements TaskListener {
  @Override
  public void notify(DelegateTask delegateTask) {
    System.out.println("执行人：" + delegateTask.getAssignee());
    // 根据任务节点逻辑查询实际需要的执行人是谁
    delegateTask.setAssignee("wukong");
  }
}
```

