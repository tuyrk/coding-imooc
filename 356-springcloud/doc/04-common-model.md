# 第4章 通用模块的实现【抽离通用代码，减少程序耦合】

## 4-1 微服务通用模块的实现

1. 添加依赖

   ```xml
   <dependency>
     <groupId>com.alibaba</groupId>
     <artifactId>fastjson</artifactId>
     <version>1.2.68</version>
   </dependency>
   <dependency>
     <groupId>commons-codec</groupId>
     <artifactId>commons-codec</artifactId>
     <version>1.14</version>
   </dependency>
   ```

2. 公用对象

   ```java
   // 基本的用户信息
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class UserInfo {
       private Long id;
       private String username;
       private String email;
   
       public static UserInfo invalid() {
           return new UserInfo(-1L, "", "");
       }
   }
   ```

   ```java
   // 课程信息
   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public class CourseInfo {
       private Long id;
       private String courseName;
       private String courseType;
       private String courseIcon;
       private String courseIntro;
   
       public static CourseInfo invalid() {
           return new CourseInfo(-1L, "", "", "", "");
       }
   }
   ```

   ```java
   // 课程信息请求对象
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public class CourseInfosRequest {
       private List<Long> ids;
   }
   ```

