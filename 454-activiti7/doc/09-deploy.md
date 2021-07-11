# 第9章 部署上线【适用于简历项目演示】

> 将在Linux和Windows上部署，配置外网访问权限，方便小伙伴作为简历项目展示。

### 9-1 jar包与war包..3

Jar包改为War包注意事项：

- pom.xml里jar改为war
- pom.xml搜索没有tomcat依赖包
- 开发与发布jdk版本一致

1. 继承SpringBootServletInitializer并重写configure方法

   ```java
   @SpringBootApplication
   public class ActivitiwebApplication extends SpringBootServletInitializer {
     @Override
     protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
       return builder.sources(ActivitiwebApplication.class);
     }
   
     public static void main(String[] args) {
       SpringApplication.run(ActivitiwebApplication.class, args);
     }
   }
   ```

2. pom.xml里jar改为war

   ```xml
   <packaging>war</packaging>
   ```

### 9-2 Linux部署

检查端口是否打开：22、8080

查看Linux网络状态：netstat -lntp

启动命令：nohup java -jar activiti7.jar >/dev/null &

关闭项目：kill pid

**国内开源镜像站**

### 9-3 Windows部署

Windows下使用批处理可使jar包部署后台运行

### 9-4 达梦数据库支持

1. 引入达梦数据库连接驱动包

   驱动jar见lib/DmJdbcDriver18.jar，可将该jar包上传至阿里云私有maven仓库，并通过maven下载引入。

   阿里云私有maven仓库：https://maven.aliyun.com/mvn/guide

2. 配置数据库连接

   在application.yml中添加如下内容：

   ```yaml
   spring:
     datasource:
       username: ACTIVITI
       password: 123456789
       jdbc-url: jdbc:dm://192.168.1.39:5236:DAMENG
       driver-class-name: dm.jdbc.driver.DmDriver
   ```

3. 更改Activiti 默认数据源连接方式

   因 Activiti 包中未预设达梦数据库，所以需要手动修改默认数据源连接方式

   ```java
   @Slf4j
   @Configuration
   public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {
   
     @Bean
     @Primary
     @ConfigurationProperties(prefix = "spring.datasource")
     public DataSource activitiDataSource() {
       return DataSourceBuilder.create().build();
     }
   
     @Resource
     private PlatformTransactionManager activitiTransactionManager;
   
     @Bean
     public SpringProcessEngineConfiguration springProcessEngineConfiguration() {
       log.info("注入activiti的config");
       SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
       DataSource dataSource = activitiDataSource();
       configuration.setDataSource(dataSource);
       configuration.setDatabaseType("oracle");
       configuration.setTransactionManager(activitiTransactionManager);
       configuration.setDatabaseSchemaUpdate("none");
       return configuration;
     }
   }
   ```

4. 初始化Activiti 所需的表

   activiti-engine依赖下有数据库初始化的SQL语句

   ```xml
   <dependency>
     <groupId>org.activiti</groupId>
     <artifactId>activiti-engine</artifactId>
   </dependency>
   ```

5. 修正缺失字段

   官方初始化SQL中缺少ACT_RE_DEPLOYMENT表的`PROJECT_RELEASE_VERSION_`和`VERSION_`字段，可执行下列sql语句进行增加。

   ```mysql
   alter table ACT_RE_DEPLOYMENT add column PROJECT_RELEASE_VERSION_ varchar(255) DEFAULT NULL;
   alter table ACT_RE_DEPLOYMENT add column VERSION_ varchar(255) DEFAULT NULL;
   ```

   