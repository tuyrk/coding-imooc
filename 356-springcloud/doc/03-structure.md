# 第3章 构建工程结构与基础设施【完善工程环境】

## 3-1 搭建工程结构目录

```xml
<!-- 项目的打包类型, 即项目的发布形式, 默认为 jar. 对于聚合项目的父模块来说, 必须指定为 pom -->
<packaging>pom</packaging>
<!-- 标识 SpringCloud 的版本 -->
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-dependencies</artifactId>
      <version>Hoxton.SR4</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## 3-2 单节点 Eureka Server 的开发

1. 添加依赖：eureka server提供服务发现与服务注册

   ```xml
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
   </dependency>
   ```

2. 启动类添加注解

   ```java
   @EnableEurekaServer
   ```

3. 项目配置

   ```yaml
spring:
     application:
       name: homepage-eureka
   server:
     port: 8000
   eureka:
     instance:
       hostname: localhost # 实例主机名
     client:
       # 表示是否从Eureka server中获取注册信息，默认是 true.
       # 单点的Eureka server不需要同步其他节点的数据，所以设置为false
       fetch-registry: false
       # 表示是否将自己注册到Eureka Server，默认是 true.
       register-with-eureka: false #
       service-url:
         defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
   ```

## 3-3 多节点 Eureka Server 的搭建

1. 添加主机名

   ```
   127.0.0.1 server1
   127.0.0.1 server2
   127.0.0.1 server3
   ```

2. 项目配置

   SpringBoot应用启动时，会先加载bootstrap.yml，然后才是application.yml

   ```yaml
   spring:
     application:
       name: homepage-eureka
     profiles: server1
   server:
     port: 8000
   eureka:
     instance:
       hostname: server1
       prefer-ip-address: false
     client:
       service-url:
         defaultZone: http://server2:8001/eureka/,http://server3:8002/eureka/
   
   ---
   spring:
     application:
       name: homepage-eureka
     profiles: server2
   server:
     port: 8001
   eureka:
     instance:
       hostname: server2
       prefer-ip-address: false
     client:
       service-url:
         defaultZone: http://server1:8000/eureka,http://server3:8002/eureka/
   
   ---
   spring:
     application:
       name: homepage-eureka
     profiles: server3
   server:
     port: 8002
   eureka:
     instance:
       hostname: server3
       prefer-ip-address: false
     client:
       service-url:
         defaultZone: http://server1:8000/eureka,http://server2:8001/eureka/
   ```

3. 打包

   ```shell
   mvn clean package -Dmaven.test.skip=true -U
   ```

4. 启动

   ```shell
   java -jar homepage-eureka/target/homepage-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=server1 
   java -jar homepage-eureka/target/homepage-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=server2
   java -jar homepage-eureka/target/homepage-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=server3
   ```

## 3-4 服务网关模块的开发

1. 添加依赖

   Eureka Client 向 Eureka Server 注册的时候会提供一系列的元数据信息, 例如: 主机, 端口, 健康检查url等
   Eureka Server 接受每个客户端发送的心跳信息, 如果在某个配置的超时时间内未接收到心跳信息, 实例会被从注册列表中移除

   ```xml
   <!-- Eureka 客户端 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
   </dependency>
   <!-- 服务网关 -->
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-netflix-zuul</artifactId>
   </dependency>
   <!-- apache 工具类 -->
   <dependency>
     <groupId>commons-io</groupId>
     <artifactId>commons-io</artifactId>
     <version>2.6</version>
   </dependency>
   ```

2. 启动类添加注解：网关应用程序

   ```java
   @EnableZuulProxy
   @SpringCloudApplication
   ```

3. 自定义过滤器：计算请求响应耗时

   ```java
   @Component
   public class PreRequestFilter extends ZuulFilter {
       @Override
       public String filterType() {
           // 过滤器的类型。发起请求前
           return FilterConstants.PRE_TYPE;
       }
   
       @Override
       public int filterOrder() {
           // 过滤器优先级。数值越小，级别越高
           return 0;
       }
   
       @Override
       public boolean shouldFilter() {
           // 是否启用当前过滤器
           return true;
       }
   
       @Override
       public Object run() throws ZuulException {
           // 用于在过滤器之间传递消息（路由信息、错误、Request、Response）
           RequestContext ctx = RequestContext.getCurrentContext();
           ctx.set("startTime", System.currentTimeMillis());
           return null;
       }
   }
   ```

   ```java
   @Slf4j
   @Component
   public class AccessLogFilter extends ZuulFilter {
       @Override
       public String filterType() {
           return FilterConstants.POST_TYPE;
       }
   
       @Override
       public int filterOrder() {
           return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
       }
   
       @Override
       public boolean shouldFilter() {
           return true;
       }
   
       @Override
       public Object run() throws ZuulException {
           RequestContext ctx = RequestContext.getCurrentContext();
           long startTime = Long.parseLong((String) ctx.get("startTime"));
           HttpServletRequest request = ctx.getRequest();
           String uri = request.getRequestURI();
           long duration = System.currentTimeMillis() - startTime;
           log.info("uri: {}, duration: {} ms", uri, duration / 1000);
           return null;
       }
   }
   ```

4. 项目配置

   ```yaml
   spring:
     application:
       name: homepage-zuul
   server:
     port: 9000
   eureka:
     client:
       service-url: 
         defaultZone: http://server1:8000/eureka/
   ```

## 3-5 数据表的设计与创建

- 课程表：home_page

  | 字段名       | 类型         | 字段含义           |
  | ------------ | ------------ | ------------------ |
  | id           | bigint       | 自增主键           |
  | course_name  | varchar(128) | 课程名称，唯一索引 |
  | course_type  | varchar(128) | 课程类型           |
  | course_icon  | varchar(128) | 课程图标           |
  | course_intro | varchar(128) | 课程介绍           |
  | create_time  | datetime     | 创建时间           |
  | upadte_time  | datetime     | 更新时间           |

- 用户信息表和用户课程表：homepage_user、homepage_user_course

  | 字段名      | 类型         | 字段含义         |
  | ----------- | ------------ | ---------------- |
  | id          | bigint       | 自增主键         |
  | username    | varchar(128) | 用户名，唯一索引 |
  | email       | varchar(128) | 用户邮箱         |
  | create_time | datetime     | 创建时间         |
  | update_time | datetime     | 更新时间         |

  | 字段名      | 类型     | 字段含义 |
  | ----------- | -------- | -------- |
  | id          | bigint   | 自增主键 |
  | user_id     | bigint   | 用户id   |
  | course_id   | bigint   | 课程id   |
  | create_time | datetime | 创建时间 |
  | update_time | datetime | 更新时间 |

  备注：user_id与course_id构成联合唯一索引

  ```sql
  -- 数据库
  create database imooc_356;
  
  -- 用户信息表
  create table if not exists `imooc_356`.`homepage_user` (
    `id` bigint(20) not null auto_increment comment '自增ID',
    `username` varchar(128) not null default '' comment '用户名',
    `email` varchar(128) not null default '' comment '用户邮箱',
    `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
    `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
    primary key (`id`),
    unique key `key_username` (`username`)
  ) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='用户信息表';
  
  -- 用户课程表
  create table if not exists `imooc_356`.`homepage_user_course` (
    `id` bigint(20) not null auto_increment comment '自增ID',
    `user_id` bigint(20) not null default 0 comment '用户ID',
    `course_id` bigint(20) not null default 0 comment '课程ID',
    `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
    `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
    primary key (`id`),
    unique key `key_user_course` (`user_id`, `course_id`)
  ) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='用户课程表';
  
  -- 课程信息表
  create table if not exists `imooc_356`.`homepage_course` (
    `id` bigint(20) not null auto_increment comment '自增ID',
    `course_name` varchar(128) not null default '' comment '课程名称',
    `course_type` varchar(128) not null default '' comment '课程类型',
    `course_icon` varchar(128) not null default '' comment '课程图片',
    `course_intro` varchar(128) not null default '' comment '课程介绍',
    `create_time` datetime not null default '1970-01-01 08:00:00' comment '创建时间',
    `update_time` datetime not null default '1970-01-01 08:00:00' comment '更新时间',
    primary key (`id`),
    unique key `key_course_name` (`course_name`)
  ) engine=InnoDB auto_increment = 1 default charset=utf8 row_format=compact comment='课程信息表';
  
INSERT INTO imooc_356.homepage_course (id, course_name, course_type, course_icon, course_intro, create_time, update_time) VALUES (1, 'JDK11&12 新特性解读', '0', 'https://www.imooc.com', '解读 JDK11 和 JDK12 的新版本特性', '2020-04-27 15:07:32', '2020-04-27 15:07:32');
  INSERT INTO imooc_356.homepage_course (id, course_name, course_type, course_icon, course_intro, create_time, update_time) VALUES (2, '基于 SpringCloud 微服务架构 广告系统设计与实现', '1', 'https://www.imooc.com', '广告系统的设计与实现', '2020-04-27 15:07:32', '2020-04-27 15:07:32');
  
  INSERT INTO imooc_356.homepage_user (id, username, email, create_time, update_time) VALUES (1, 'tuyrk', 'tuyrk@qq.com', '2020-04-28 14:51:39', '2020-04-28 14:51:39');
  
  INSERT INTO imooc_356.homepage_user_course (id, user_id, course_id, create_time, update_time) VALUES (1, 1, 1, '2020-04-28 15:02:50', '2020-04-28 15:02:50');
  INSERT INTO imooc_356.homepage_user_course (id, user_id, course_id, create_time, update_time) VALUES (2, 1, 2, '2020-04-28 15:02:50', '2020-04-28 15:02:50');
  ```
  
