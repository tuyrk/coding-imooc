# 第5章 实现课程微服务【注意微服务构建的细节】

## 5-1 搭建微服务及数据表操作相关实现

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
   @EnableEurekaClient
   @SpringBootApplication
   ```

3. 项目配置

   ```yaml
   server:
     port: 7001
     servlet:
       context-path: /homepage-course
   spring:
     application:
       name: eureka-client-homepage-course
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
   ```

4. 用户信息表homepage_course映射实体表

   ```java
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   @Entity // 实体类
   @EntityListeners(AuditingEntityListener.class) // 捕获监听，持久化和更新操作
   @Table(name = "homepage_course") // 表名
   public class HomepageCourse {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY) // 生成主键策略：自增
     @Column(name = "id", nullable = false)
     private Long id;
     // 课程名称
     @Basic // 缺省
     @Column(name = "course_name", nullable = false) // 字段名，不可为空
     private String courseName;
     // 课程类型：0（免费课），1（实战课）
     @Column(name = "course_type", nullable = false)
     private Integer courseType;
     // 课程图标
     @Column(name = "course_icon", nullable = false)
     private String courseIcon;
     // 课程介绍
     @Column(name = "course_intro", nullable = false)
     private String courseIntro;
     // 创建时间
     @Column(name = "create_time", nullable = false)
     @CreatedDate // 创建则自动生成时间
     private Date createTime;
     // 更新时间
     @Column(name = "update_time", nullable = false)
     @LastModifiedDate // 改变则自动更新时间
     private Date updateTime;
   
     public HomepageCourse(String courseName, Integer courseType, String courseIcon, String courseIntro) {
       this.courseName = courseName;
       this.courseType = courseType;
       this.courseIcon = courseIcon;
       this.courseIntro = courseIntro;
     }
   
     // 返回一个无效的课程
     public static HomepageCourse invalid() {
       HomepageCourse homePageCourse = new HomepageCourse("", 0, "", "");
       homePageCourse.setId(-1L);
       return homePageCourse;
     }
   }
   ```

5. 增删改查dao

   ```java
   public interface HomepageCourseDao extends JpaRepository<HomepageCourse, Long> {
   }
   ```

## 5-2 微服务功能实现

1. 课程相关服务接口定义

   ```java
   public interface ICourseService {
     // 通过id获取课程信息
     CourseInfo getCourseInfo(Long id);
     // 通过ids获取课程信息
     List<CourseInfo> getCourseInfos(CourseInfosRequest request);
   }
   ```

2. 课程服务功能实现

   ```java
   @Slf4j
   @Service
   public class CourseServiceImpl implements ICourseService {
       @Autowired
       private HomepageCourseDao homepageCourseDao;
   
       // 通过id获取课程信息
       @Override
       public CourseInfo getCourseInfo(Long id) {
           Optional<HomepageCourse> course = homepageCourseDao.findById(id);
           return buildCourseInfo(course.orElse(HomepageCourse.invalid()));
       }
   
       // 通过ids获取课程信息
       @Override
       public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
           if (CollectionUtils.isEmpty(request.getIds())) {
               return Collections.emptyList();
           }
           List<HomepageCourse> courses = homepageCourseDao.findAllById(request.getIds());
           return courses.stream().map(this::buildCourseInfo).collect(Collectors.toList());
       }
   
       // 根据数据记录构造对象信息
       private CourseInfo buildCourseInfo(HomepageCourse course) {
           return CourseInfo.builder()
                   .id(course.getId())
                   .courseName(course.getCourseName())
                   .courseType(course.getCourseType() == 0 ? "免费课程" : "实战课程")
                   .courseIcon(course.getCourseIcon())
                   .courseIntro(course.getCourseIntro())
                   .build();
       }
   }
   ```

3. 课程对外服务接口

   ```java
   @Slf4j
   @RestController
   public class HomepageCourseController {
   	@Autowired
     private final ICourseService courseService;
   
     @GetMapping("/get/course")
     public CourseInfo getCourseInfo(Long id) {
       log.info("<homepage-course>:get course -> {}", id);
       return courseService.getCourseInfo(id);
     }
   
     @PostMapping("/get/courses")
     public List<CourseInfo> getCourseInfos(@RequestBody CourseInfosRequest request) {
       log.info("<homepage-course>:get courses -> {}", JSON.toJSONString(request));
       return courseService.getCourseInfos(request);
     }
   }
   ```

## 5-3 功能接口测试用例的实现

1. 测试启动类

   ```java
   @RunWith(SpringRunner.class)
   @SpringBootTest
   public class HomepageCourseApplicationTests {
   }
   ```
   
2. 创建课程信息

   ```java
   @Test
   @Transactional
   public void testCreateCourseInfo() {
     HomepageCourse course1 = new HomepageCourse("JDK11&12 新特性解读", 0, "https://www.imooc.com", "解读 JDK11 和 JDK12 的新版本特性");
     HomepageCourse course2 = new HomepageCourse("基于 SpringCloud 微服务架构 广告系统设计与实现", 1, "https://www.imooc.com", "广告系统的设计与实现");
     List<HomepageCourse> homepageCourses = homepageCourseDao.saveAll(Arrays.asList(course1, course2));
     assertEquals(2, homepageCourses.size());
   }
   ```
   
3. 获取课程信息

   ```java
   @Test
   void getCourseInfo() {
     CourseInfo courseInfo1 = courseService.getCourseInfo(1L);
     assertEquals(1, courseInfo1.getId());
     CourseInfo courseInfo2 = courseService.getCourseInfo(2L);
     assertEquals(2, courseInfo2.getId());
   }
   ```
   
   ```java
   @Test
   void getCourseInfos() {
     List<CourseInfo> courseInfos = courseService.getCourseInfos(new CourseInfosRequest(Arrays.asList(1L, 2L)));
     assertEquals(2, courseInfos.size());
   }
   ```
   
