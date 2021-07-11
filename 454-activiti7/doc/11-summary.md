# 第11章 课程总结：他山之石，可以攻玉

> 对课程的总结，强调课程总最重要的知识点和最容易出错的地方，愿小伙伴们：所学皆有用，扬帆起航，升职加薪，为自己的锦绣前程做铺垫！

### 11-1 总结回顾

#### 流程变量覆盖注意事项

> Task旧版不能覆盖，新版可以覆盖

旧版：

```java
public void completeTaskWithArgs() {
  Map<String, Object> variables = new HashMap<>();
  variables.put("pay", "101");
  taskService.complete("a2cbdb8c-8b22-11eb-98a9-3edce3e71d32", variables);
  System.out.println("完成任务");
}
```

新版：

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

```java
taskRuntime.complete(TaskPayloadBuilder.complete().withTaskId(taskId)
                     .withVariables(variables)
                     .build()); // 带参数完成任务
```

#### Activiti中的用户

> 如果要添加用户，需要给密码进行编码。SpringSecurity

```java
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder();
}

String password = passwordEncoder.encode("111");
```

#### 候选者已被遗弃

#### 数据库部署关系

> 部署表：ACT_RE_DEPLOYMENT
>
> 流程定义表：ACT_RE_PROCDEF
>
> 活动实例节点表：ACT_RU_EXECUTION
