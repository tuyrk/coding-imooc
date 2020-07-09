# 第6章 实现用户微服务【得心应手，从容开发】

##  6-1 搭建微服务

1. 引入依赖

   ```xml
   <!-- 引入 Web 功能 -->
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
   </dependency>
   <!-- Eureka 客户端 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   <!-- 引入 Feign, 可以以声明的方式调用微服务 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-openfeign</artifactId>
   </dependency>
   <!-- 引入服务容错 Hystrix 的依赖 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
   </dependency>
   <!-- Java Persistence API, ORM 规范 -->
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-data-jpa</artifactId>
   </dependency>
   <!-- MySQL 驱动, 注意, 这个需要与 MySQL 版本对应 -->
   <dependency>
     <groupId>mysql</groupId>
     <artifactId>mysql-connector-java</artifactId>
     <version>8.0.19</version>
     <scope>runtime</scope>
   </dependency>
   <!-- 通用模块 -->
   <dependency>
     <groupId>com.tuyrk</groupId>
     <artifactId>homepage-common</artifactId>
     <version>0.0.1-SNAPSHOT</version>
   </dependency>
   ```

2. 启动类注解

   ```java
   @EnableJpaAuditing
   @EnableFeignClients
   @EnableCircuitBreaker
   @EnableEurekaClient
   @SpringBootApplication
   ```
   
3. 项目配置
   
   ```yaml
   server:
     port: 7000
     servlet:
       context-path: /homepage-user
   
   spring:
     application:
       name: eureka-client-homepage-user
     main:
       allow-bean-definition-overriding: true
     jpa:
       show-sql: true
       hibernate:
         ddl-auto: none
       properties: 
         hibernate.format_sql: true
       open-in-view: false
     datasource:
       url: jdbc:mysql://127.0.0.1:3306/imooc_356?autoReconnect=true&useUnicode=true&characterEncoding=utf8&useSSL=false
       username: root
       password: 123456
       driver-class-name: com.mysql.cj.jdbc.Driver
       tomcat:
         max-active: 4
         min-idle: 2
         initial-size: 2
   eureka:
     client:
       service-url:
         defaultZone: http://server1:8000/eureka/
   feign:
     hystrix:
       enabled: true
   ```

##  6-2 数据表实体类及Dao接口的定义

1. 用户信息表homepage_user映射实体表

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Entity
   @EntityListeners(AuditingEntityListener.class)
   @Table(name = "homepage_user")
   public class HomepageUser {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id", nullable = false)
     private Long id;
     // 用户名
     @Column(name = "username", nullable = false)
     private String username;
     // 用户邮箱
     @Column(name = "email", nullable = false)
     private String email;
     // 创建时间
     @Column(name = "create_time", nullable = false)
     @CreatedDate
     private Date createTime;
     // 更新时间
     @Column(name = "update_time", nullable = false)
     @LastModifiedDate
     private Date updateTime;
   
     public HomepageUser(String username, String email) {
       this.username = username;
       this.email = email;
     }
   
     public static HomepageUser invalid() {
       HomepageUser homepageUser = new HomepageUser("", "");
       homepageUser.setId(-1L);
       return homepageUser;
     }
   }
   ```

2. 用户课程表homepage_user_course映射实体表

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Entity
   @EntityListeners(AuditingEntityListener.class)
   @Table(name = "homepage_user_course")
   public class HomepageUserCourse {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id", nullable = false)
     private Long id;
     // 用户id
     @Column(name = "user_id", nullable = false)
     private Long userId;
     // 课程id
     @Column(name = "course_id", nullable = false)
     private Long courseId;
     // 创建时间
     @Column(name = "create_time", nullable = false)
     @CreatedDate
     private Date createTime;
     // 更新时间
     @Column(name = "update_time", nullable = false)
     @LastModifiedDate
     private Date updateTime;
   }
   ```

3. HomepageUser数据表访问接口

   ```java
   public interface HomepageUserDao extends JpaRepository<HomepageUser, Long> {
     // 通过用户名寻找用户记录
     HomepageUser findByUsername(String username);
   }
   ```

4. HomepageUserCourse数据表访问接口

   ```java
   public interface HomepageUserCourseDao extends JpaRepository<HomepageUserCourse, Long> {
     // 通过用户id寻找数据记录
     List<HomepageUserCourse> findAllByUserId(Long userId);
   }
   ```

##  6-3 Feign 接口及值对象的定义

1. 通过Feign访问课程微服务

   ```java
   @FeignClient(value = "eureka-client-homepage-course", fallback = CourseClientHystrix.class)
   public interface CourseClient {
     // 通过id获取课程信息
     @GetMapping("/homepage-course/get/course")
     CourseInfo getCourseInfo(Long id);
   
     // 通过ids获取课程信息
     @PostMapping("/homepage-course/get/courses")
     List<CourseInfo> getCourseInfos(CourseInfosRequest request);
   }
   ```

2. 熔断降级策略

   ```java
   @Component
   public class CourseClientHystrix implements CourseClient {
     @Override
     public CourseInfo getCourseInfo(Long id) {
       return CourseInfo.invalid();
     }
   
     @Override
     public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
       return Collections.emptyList();
     }
   }
   ```

3. 创建用户请求对象

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class CreateUserRequest {
     private String username;
     private String email;
   
     // 请求有效性验证
     public boolean validate() {
       return StringUtils.isNotBlank(username) && StringUtils.isNotBlank(email);
     }
   }
   ```

4. 用户课程对象

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class UserCourseInfo {
     private UserInfo userInfo;
     private List<CourseInfo> courseInfos;
   
     public static UserCourseInfo invalid() {
       return new UserCourseInfo(new UserInfo(), Collections.emptyList());
     }
   }
   ```

##  6-4 微服务功能实现

1. 用户相关服务接口

   ```java
   public interface IUserService {
       // 创建用户
       UserInfo createUser(CreateUserRequest request);
       // 根据ID获取用户信息
       UserInfo getUserInfo(Long id);
       // 获取用户和课程信息
       UserCourseInfo getUserCourseInfo(Long id);
   }
   ```

2. 用户相关实现

   ```java
   @Slf4j
   @Service
   public class UserServiceImpl implements IUserService {
     @Autowired
     private HomepageUserDao homepageUserDao;
     @Autowired
     private HomepageUserCourseDao homepageUserCourseDao;
     @Autowired
     private CourseClient courseClient;
   
     // 创建用户
     @Override
     public UserInfo createUser(CreateUserRequest request) {
       if (!request.validate()) {
         return UserInfo.invalid();
       }
       HomepageUser oldUser = homepageUserDao.findByUsername(request.getUsername());
       if (!Objects.isNull(oldUser)) {
         return UserInfo.invalid();
       }
       HomepageUser newUser = homepageUserDao.save(new HomepageUser(request.getUsername(), request.getEmail()));
       return new UserInfo(newUser.getId(), newUser.getUsername(), newUser.getEmail());
     }
   
     // 根据ID获取用户信息
     @Override
     public UserInfo getUserInfo(Long id) {
       Optional<HomepageUser> user = homepageUserDao.findById(id);
       HomepageUser curUser = user.orElse(HomepageUser.invalid());
       return new UserInfo(curUser.getId(), curUser.getUsername(), curUser.getEmail());
     }
   
     // 获取用户和课程信息
     @Override
     public UserCourseInfo getUserCourseInfo(Long id) {
       Optional<HomepageUser> user = homepageUserDao.findById(id);
       HomepageUser curUser = user.orElse(null);
       if (Objects.isNull(curUser)) {
         return UserCourseInfo.invalid();
       }
       UserInfo userInfo = new UserInfo(curUser.getId(), curUser.getUsername(), curUser.getEmail());
       List<HomepageUserCourse> userCourses = homepageUserCourseDao.findAllByUserId(curUser.getId());
       if (CollectionUtils.isEmpty(userCourses)) {
         return new UserCourseInfo(userInfo, Collections.emptyList());
       }
       List<Long> courseIds = userCourses.stream().map(HomepageUserCourse::getCourseId).collect(Collectors.toList());
       List<CourseInfo> courseInfos = courseClient.getCourseInfos(new CourseInfosRequest(courseIds));
       return new UserCourseInfo(userInfo, courseInfos);
     }
   }
   ```

3. 用户服务对外接口

   ```java
   @Slf4j
   @RestController
   public class HomepageUserController {
     @Autowired
     private IUserService userService;
   
     @PostMapping("/create/user")
     public UserInfo createUser(@RequestBody CreateUserRequest request) {
       log.info("<homepage-user>: create user -> {}", JSON.toJSONString(request));
       return userService.createUser(request);
     }
   
     @GetMapping("/get/user")
     public UserInfo getUserInfo(Long id) {
       log.info("<homepage-user>: get user -> {}", id);
       return userService.getUserInfo(id);
     }
   
     @GetMapping("/get/user/course")
     public UserCourseInfo getUserCourseInfo(Long id) {
       log.info("<homepage-user>: get user course -> {}", id);
       return userService.getUserCourseInfo(id);
     }
   }
   ```

##  6-5 功能接口测试用例的实现

1. 创建用户

   ```java
   @Test
   @Transactional
   void createUser() {
     CreateUserRequest request = new CreateUserRequest("tuyrk", "tuyrk@qq.com");
     UserInfo user = userService.createUser(request);
     assertEquals("tuyrk", user.getUsername());
   }
   ```

2. 获取用户信息

   ```java
   @Test
   void getUserInfo() {
     UserInfo userInfo = userService.getUserInfo(1L);
     assertEquals("tuyrk", userInfo.getUsername());
   }
   ```

3. 创建用户课程对应关系

   ```java
   @Test
   @Transactional
   void findAllByUserId() {
     HomepageUserCourse userCourse1 = new HomepageUserCourse();
     userCourse1.setUserId(1L);
     userCourse1.setCourseId(1L);
     HomepageUserCourse userCourse2 = new HomepageUserCourse();
     userCourse2.setUserId(1L);
     userCourse2.setCourseId(2L);
     java.util.List<HomepageUserCourse> userCourses = homepageUserCourseDao.saveAll(List.of(userCourse1, userCourse2));
     assertEquals(2, userCourses.size());
   }
   ```
