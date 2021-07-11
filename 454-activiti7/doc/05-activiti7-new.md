# 第5章 Activiti 7 新特性尝鲜 试看

> 本章开始学点“硬”技能，有难度，希望小伙伴们秉承不抛弃不放弃原则，死磕到底，顺利通关。由于Activiti7集成了SpringSecurity安全框架，因此本章先讲解安全框如何集成、配置，访问权限如何写，请求处理，用户与权限分配等。 ...

### 5-1 API新特性ProcessRuntime（上）

```java
public interface ProcessRuntime {
  ProcessRuntimeConfiguration configuration();
  ProcessDefinition processDefinition(String processDefinitionId);
  Page processDefinitions(Pageable pageable);
  Page processDefinitions(Pageable pageable, GetProcessDefinitionsPayload payload);
  ProcessInstance start(StartProcessPayload payload);
  Page processInstances(Pageable pageable);
  Page processInstances(Pageable pageable, GetProcessInstancesPayload payload);
  ProcessInstance processInstance(String processInstanceId);
  ProcessInstance suspend(SuspendProcessPayload payload);
  ProcessInstance resume(ResumeProcessPayload payload);
  ProcessInstance delete(DeleteProcessPayload payload);
  void signal(SignalPayload payload);
  ...
}
```

> 为了与ProcessRuntime API进行交互，当前登录的用户必须具有角色“ ACTIVITI_USER”。

```java
@Slf4j
@Component
public class SecurityUtil {
  @Autowired
  private UserDetailsService userDetailsService;

  public void logInAs(String username) {
    UserDetails user = userDetailsService.loadUserByUsername(username);
    if (user == null) {
      throw new IllegalStateException("User " + username + " doesn't exist, please provide a valid user");
    }
    log.info("> Logged in as: " + username);
    SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() { return user.getAuthorities(); }
      @Override
      public Object getCredentials() { return user.getPassword(); }
      @Override
      public Object getDetails() { return user; }
      @Override
      public Object getPrincipal() { return user; }
      @Override
      public boolean isAuthenticated() { return true; }
      @Override
      public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
      @Override
      public String getName() { return user.getUsername(); }
    }));
    org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(username);
  }
}
```

```java
@Slf4j
@Configuration
public class DemoApplicationConfiguration {
  @Bean
  public UserDetailsService myUserDetailsService() {
    InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();

    String[][] usersGroupsAndRoles = {
      {"salaboy", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
      {"bajie", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
      {"wukong", "password", "ROLE_ACTIVITI_USER", "GROUP_activitiTeam"},
      {"other", "password", "ROLE_ACTIVITI_USER", "GROUP_otherTeam"},
      {"admin", "password", "ROLE_ACTIVITI_ADMIN"},
    };
    for (String[] user : usersGroupsAndRoles) {
      List<String> authoritiesStrings = Arrays.asList(Arrays.copyOfRange(user, 2, user.length));
      log.info("> Registering new user: " + user[0] + " with the following Authorities[" + authoritiesStrings + "]");
      inMemoryUserDetailsManager.createUser(
        new User(
          user[0],
          passwordEncoder().encode(user[1]), 
          authoritiesStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        )
      );
    }

    return inMemoryUserDetailsManager;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
```

### 5-2 API新特性ProcessRuntime（中）

获取流程实例

```java
securityUtil.logInAs("bajie");
Page<ProcessInstance> processInstancePage = processRuntime.processInstances(Pageable.of(0, 100));
System.out.println("流程实例数量：" + processInstancePage.getTotalItems());
List<ProcessInstance> list = processInstancePage.getContent();
for (ProcessInstance pi : list) {
  System.out.println("getId：" + pi.getId());
  System.out.println("getName：" + pi.getName());
  System.out.println("getStartDate：" + pi.getStartDate());
  System.out.println("getStatus：" + pi.getStatus());
  System.out.println("getProcessDefinitionId：" + pi.getProcessDefinitionId());
  System.out.println("getProcessDefinitionKey：" + pi.getProcessDefinitionKey());
}
```

启动流程实例

```java
ProcessInstance processInstance = processRuntime.start(
  ProcessPayloadBuilder
  .start()
  .withProcessDefinitionKey("myProcess_ProcessRuntime")
  .withName("第一个流程实例名称")
  // .withVariable("", "")
  .withBusinessKey("自定义bKey")
  .build()
);
```

删除流程实例

```java
ProcessInstance processInstance = processRuntime.delete(
  ProcessPayloadBuilder
  .delete()
  .withProcessInstanceId("6207553a-9301-11eb-8035-6617e358527a")
  .withReason("测试删除原因")
  .build()
);
```

挂起流程实例

```java
ProcessInstance processInstance = processRuntime.suspend(
  ProcessPayloadBuilder
  .suspend()
  .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
  .build()
);
```

激活流程实例

```java
ProcessInstance processInstance = processRuntime.resume(
  ProcessPayloadBuilder
  .resume()
  .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
  .build()
);
```

流程实例参数

```java
List<VariableInstance> list = processRuntime.variables(
  ProcessPayloadBuilder
  .variables()
  .withProcessInstanceId("4fb3b105-8b21-11eb-824e-3edce3e71d32")
  .build()
);
for (VariableInstance vi : list) {
  System.out.println("getName：" + vi.getName());
  System.out.println("getValue：" + vi.getValue());
  System.out.println("getTaskId：" + vi.getTaskId());
  System.out.println("getProcessInstanceId：" + vi.getProcessInstanceId());
  System.out.println("getType：" + vi.getType());
  System.out.println("isTaskVariable：" + vi.isTaskVariable());
}
```

### 5-3 API新特性ProcessRuntime（下）

### 5-4 API新特性TaskRuntime

```java
public interface TaskRuntime {
  TaskRuntimeConfiguration configuration();
  Task task(String taskId);
  Page tasks(Pageable pageable);
  Page tasks(Pageable pageable, GetTasksPayload payload);
  Task create(CreateTaskPayload payload);
  Task claim(ClaimTaskPayload payload);
  Task release(ReleaseTaskPayload payload);
  Task complete(CompleteTaskPayload payload);
  Task update(UpdateTaskPayload payload);
  Task delete(DeleteTaskPayload payload);
}
```

> 这里需要注意：为了以用户身份与TaskRuntime API进行交互，需要具有角色：ACTIVITI_USER

获取当前登录用户任务

```java
Page<Task> tasks = taskRuntime.tasks(Pageable.of(0, 100),
                                     TaskPayloadBuilder.tasks().build());
List<Task> list = tasks.getContent();
for (Task tk : list) {
  System.out.println("getId：" + tk.getId());
  System.out.println("getName：" + tk.getName());
  System.out.println("getStatus：" + tk.getStatus());
  System.out.println("getCreatedDate：" + tk.getCreatedDate());
  // 候选人为当前登录用户，null的时候需要前端拾取
  System.out.println("Assignee：" + StringUtils.defaultString(tk.getAssignee(), "待拾取任务"));
}
```

完成任务

```java
Task task = taskRuntime.task("194476ea-8b25-11eb-a453-3edce3e71d32");
if (task.getAssignee() == null) {
  taskRuntime.claim(TaskPayloadBuilder.claim()
                    .withTaskId(task.getId())
                    .build());
}
taskRuntime.complete(TaskPayloadBuilder
                     .complete()
                     .withTaskId(task.getId())
                     // .withVariable("", "")
                     .build());
```

### 5-5 SpringSecurity用户登录（上）

Spring Security的主要功能：认证、鉴权/授权

用户的三种来源：

- application.properties配置用户
- 代码中配置内存用户
- 从数据库中加载用户

本课程知识点：

- 内存登录改为数据库登录

- Spring Security配置文件详解

  页面是否需要登陆才能访问、登陆成功或失败的请求处理、返回值

- 登录响应处理方案

```java
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    String password = passwordEncoder.encode("111");
    // 没有做任何数据库校验
    return new User(username, password,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ACTIVITI_USER"));
  }

  @Bean
  public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
```

### 5-6 SpringSecurity用户登录（下）

```java
@Data
public class UserInfoBean implements UserDetails {
  private static final long serialVersionUID = -8308266372513594348L;
  private Long id;
  public String name;
  private String address;
  private String username;
  private String password;
  private String authorities;

  private boolean accountNonExpired;
  private boolean accountNonLocked;
  private boolean credentialsNonExpired;
  private boolean enabled;

  // 从数据库中取出authorities字符串后，进行分解，构成一个GrantedAuthority的List返回
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities.split(","))
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());
  }
}
```

```java
@Mapper
public interface UserInfoBeanMapper {
  // 根据用户名从数据库中查询用户信息
  @Select("SELECT * FROM user WHERE username = #{username}")
  UserInfoBean selectByUsername(@Param("username") String username);
}
```

```java
@Slf4j
@Component
public class MyUserDetailsService implements UserDetailsService {
  @Autowired
    private UserInfoBeanMapper userInfoBeanMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // 读取数据库判断用户。如果用户是null抛出异常，返回用户信息
    UserInfoBean userInfoBean = userInfoBeanMapper.selectByUsername(username);
    if (userInfoBean == null) {
      throw new UsernameNotFoundException("数据库中无此用户！");
    }
    return userInfoBean;
  }

  @Bean
  public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}
```

### 5-7 SpringSecurity配置

```java
@Configuration
public class ActivitiSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private LoginSuccessHandler loginSuccessHandler;
  @Autowired
  private LoginFailureHandler loginFailureHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .formLogin(configurer -> configurer
                 // 登陆方法
                 .loginPage("/login")
                 // 在springSecurity登录验证之后，对未登录用户跳转行为进行的判断。
                 .loginProcessingUrl("/login")
                 .successHandler(loginSuccessHandler)
                 .failureHandler(loginFailureHandler)
                )
      .authorizeRequests(registry -> registry
                         .anyRequest().permitAll()
                        )
      .logout(LogoutConfigurer::permitAll)
      // 使ajax可使用
      .csrf(AbstractHttpConfigurer::disable)
      .headers(headersConfigurer -> headersConfigurer
               // 让frame页面可以正常使用
               .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
              );
  }
}
```

登录成功处理类

```java
@Slf4j
@Component("loginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    log.info("登录成功1");
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("登录成功2");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(
      objectMapper.writeValueAsString(AjaxResponse.success(authentication.getName()))
    );
  }
}
```

登录失败处理类

```java
@Slf4j
@Component("loginFailureHandler")
public class LoginFailureHandler implements AuthenticationFailureHandler {
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
    log.info("登录失败");
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.getWriter().write(
      objectMapper.writeValueAsString(AjaxResponse.error("登录失败", e.getLocalizedMessage()))
    );
  }
}
```

### 5-8 SpringSecurity登录响应

```java
@RestController
public class ActivitiSecurityController {
  private RequestCache requestCache = new HttpSessionRequestCache();
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  // .loginProcessingUrl("/login")
  @RequestMapping("/login")
  @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
  public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
    /*SavedRequest savedRequest = requestCache.getRequest(request, response);
    if (savedRequest != null) {
      String targetUrl = savedRequest.getRedirectUrl();
      log.info("引发跳转的请求是：" + targetUrl);
      if (StringUtils.endsWithIgnoreCase(targetUrl, ".html")) {
        try {
          redirectStrategy.sendRedirect(request, response, "/demo-login.html");
        } catch (Exception ignored) {
        }
      }
    }*/

    return new SimpleResponse("需要登录，请使用login.html或发起POST登录请求");
  }
}
```

### 5-9 BPMN-JS整合

```shell
git clone https://github.com.cnpmjs.org/bpmn-io/bpmn-js-examples.git
cd bpmn-js-examples/properties-panel
npm install --save bpmn-js-properties-panel
npm install activiti-bpmn-moddle
npm install bpmn-js-properties-panel-activiti-cus
```

```javascript
// import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda';
// import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';
import propertiesProviderModule from 'bpmn-js-properties-panel-activiti-cus/lib/provider/activiti';
import activitiModdleDescriptor from 'activiti-bpmn-moddle/resources/activiti.json';

moddleExtensions: {
  // camunda: camundaModdleDescriptor
  activiti: activitiModdleDescriptor
}
```

基于有属性面板的例子去修改

- 属性面板：https://github.com/bpmn-io/bpmn-js-examples/tree/master/properties-panel

- 汉化说明：https://github.com/bpmn-io/bpmn-js-examples/tree/master/i18n


增加BPMNJS可执行选框默认勾选，打开`bpmn-js-examples/properties-panel-extension/resources/newDiagram.bpmn`

> 属性修改为isExecutable="true"

```xml
<bpmn2:process id="Process_1" isExecutable="true">
```




