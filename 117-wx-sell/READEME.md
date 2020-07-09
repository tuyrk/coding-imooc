[Spring Boot企业微信点餐系统](https://coding.imooc.com/class/105.html)
===
第1章 课程介绍
---
坚持是一种品格
1. 买家端：手机端-微信公众号打开或输入网址直接打开  
选择商品-加入购物车-下单-微信支付-支付凭证
2. 卖家端：PC端-微信扫码登录  
后台管理系统  
    - 订单：取消订单、完结订单
    - 商品：查看列表、修改商品信息、新增商品
    - 类目：查看列表、新增类目、登出

- 卖家与买家是如何消息互通的？  
买家再在下单之后，卖家端在没有刷新的情况下弹出窗口提醒有新订单，同时还伴有音乐，此处使用了WebSocket
- 卖家端点击完结订单会怎样呢？  
点击完结之后买家端会立即收到订单状态通知，此处使用了微信模板消息，点击进入消息可查看订单详情页

技术要点：
>前后端采用RESTFul风格接口连接

前后端分离
- 前端：Vue---WebApp
- 后端：SpringBoot---BootStrap+Freemarker+JQuery

1. SpringBoot：
    - 数据库：SpringBoot+JPA---SpringBoot+MyBatis
    - 缓存：SpringBoot+Redis(分布式Session+分布式锁)
    - 消息推送：WebSock
2. 微信：
微信扫码登录、消息模板推送、微信支付和退款

课程安排
- 项目设计（角色划分、功能模块划分、部署架构、数据库设计）
- 开发环境搭建（提供虚拟机）
- 功能实现（买家卖家的Dao层、Service层、Controller层、单元测试）

|软件|版本|
|:---:|:---:|
|SpringBoot|1.5.2|
|Idea|2017.1.2|
|Maven|3.3.9|
|Linux|centos7.3|
|JDK|1.8.0_111|
|MySql|5.7.17|
|Nginx|1.11.7|
|Redis|3.2.8|

第2章 项目设计
---
### 2-1 项目设计
角色划分
买家（手机端）
卖家（PC端）

功能模块划分
商品：商品列表、商品管理
订单：订单创建、订单查询、订单取消、订单管理
类目：类目管理
买家：查询商品、创建/查询订单
卖家：管理商品、查询/接单订单
买家-消息-卖家

部署架构
Request(PC/WX)---Nginx---Tomcat---Redis/MySql

数据库设计
### 2-2 架构和基础框架
单一应用架构---垂直应用架构---分布式服务架构---流动计算架构
ORM---MVC---RPC---SOA
阿里系：Duboo---Zookeeper---SpringMVC/SpringBoot
SpringCloud栈：SpringCloud---Netflix Eureka---SpringBoot
### 2-3 数据库设计
表与表之间的关系：
- 商品表(product_info)：名称、单价、库存、描述、图片、类目编号
- 类目表(product_category)：名称、编号
- 订单主表(order_master)：买家名字、买家电话、买家地址、买家微信ID、总金额、订单状态、支付状态
- 订单详情表(order_detail)：订单id、商品id、商品名字、商品价格、商品数量、商品图片
- 卖家信息表(seller_info)：用户名、密码、微信openid

建表SQL：
```mysql
# Spring Boot企业微信点餐系统

CREATE DATABASE IF NOT EXISTS `coding_117_wx_sell`;
USE `coding_117_wx_sell`;

# 商品表
CREATE TABLE `product_info` (
  `product_id`          VARCHAR(32)   NOT NULL
  COMMENT '商品ID',
  `product_name`        VARCHAR(64)   NOT NULL
  COMMENT '商品名称',
  `product_price`       DECIMAL(8, 2) NOT NULL
  COMMENT '单价',
  `product_stock`       INT           NOT NULL
  COMMENT '库存',
  `product_description` VARCHAR(64) COMMENT '描述',
  `product_icon`        VARCHAR(512) COMMENT '小图',
  `product_status`      TINYINT(3)             DEFAULT '0'
  COMMENT '商品状态,0正常1下架',
  `category_type`       INT           NOT NULL
  COMMENT '类目编号',
  `create_time`         TIMESTAMP     NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`         TIMESTAMP     NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (`product_id`)
)
  COMMENT '商品表';

# 类目表
CREATE TABLE `product_category` (
  `category_id`   INT         NOT NULL AUTO_INCREMENT,
  `category_name` VARCHAR(64) NOT NULL
  COMMENT '类目名字',
  `category_type` INT         NOT NULL
  COMMENT '类目编号',
  `create_time`   TIMESTAMP   NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`   TIMESTAMP   NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uqe_category_type`(`category_type`)
)
  COMMENT '类目表';

# 订单表
CREATE TABLE `order_master` (
  `order_id`      VARCHAR(32)   NOT NULL,
  `buyer_name`    VARCHAR(32)   NOT NULL
  COMMENT '买家名字',
  `buyer_phone`   VARCHAR(32)   NOT NULL
  COMMENT '买家电话',
  `buyer_address` VARCHAR(128)  NOT NULL
  COMMENT '买家地址',
  `buyer_openid`  VARCHAR(64)   NOT NULL
  COMMENT '买家微信openid',
  `order_amount`  DECIMAL(8, 2) NOT NULL
  COMMENT '订单总金额',
  `order_status`  TINYINT(3)    NOT NULL DEFAULT '0'
  COMMENT '订单状态, 默认0新下单',
  `pay_status`    TINYINT(3)    NOT NULL DEFAULT '0'
  COMMENT '支付状态, 默认0未支付',
  `create_time`   TIMESTAMP     NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`   TIMESTAMP     NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_buyer_openid` (`buyer_openid`)
)
  COMMENT '订单表';

# 订单详情表
CREATE TABLE `order_detail` (
  `detail_id`        VARCHAR(32)   NOT NULL,
  `order_id`         VARCHAR(32)   NOT NULL,
  `product_id`       VARCHAR(32)   NOT NULL,
  `product_name`     VARCHAR(64)   NOT NULL
  COMMENT '商品名称',
  `product_price`    DECIMAL(8, 2) NOT NULL
  COMMENT '商品价格',
  `product_quantity` INT           NOT NULL
  COMMENT '商品数量',
  `product_icon`     VARCHAR(512) COMMENT '商品小图',
  `create_time`      TIMESTAMP     NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time`      TIMESTAMP     NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (`detail_id`),
  KEY `idx_order_id` (`order_id`)
)
  COMMENT '订单详情表';

# 卖家信息表(登录后台使用, 卖家登录之后可能直接采用微信扫码登录，不使用账号密码)
CREATE TABLE `seller_info` (
  `seller_id`          VARCHAR(32) NOT NULL,
  `username`    VARCHAR(32) NOT NULL,
  `password`    VARCHAR(32) NOT NULL,
  `openid`      VARCHAR(64) NOT NULL
  COMMENT '微信openid',
  `create_time` TIMESTAMP   NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  `update_time` TIMESTAMP   NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '修改时间',
  PRIMARY KEY (`seller_id`)
)
  COMMENT '卖家信息表';
```
注意事项：
`ON UPDATE`用来记录更新时间，更新数据时数据库自动设置修改时间

第3章 项目起步
---
### 3-1 开发环境搭建
虚拟机的使用：VirtualBox
    配置虚拟机网络与主机网络在同一个网段
JDK
Maven
Idea的下载与安装
### 3-2 日志的使用
什么是日志框架  
日志框架：是一套能够实现日志输出的工具包  
日志：能够描述系统运行状态的所有事件都可以算作日志，如用户下线、接口超时、数据库崩溃

日志框架能力：  
- 定制输出目标：控制台、文件、数据库、网络第三方服务、日志文件的滚动策略
- 定制输出格式：通过配置文件自由设置输出格式
- 携带上下文信息：时间戳、类路径、线程、调用堆栈
- 运行时选择性输出：选择只打印部分功能的日志
- 灵活的配置：
- 优异的性能：

[Java常用日志框架介绍](https://www.cnblogs.com/chenhongliang/p/5312517.html)
- JCL：Apache基金会所属的项目，是一套Java日志接口，之前叫Jakarta Commons Logging，后更名为Commons Logging。
- SLF4j：(Simple Logging Facade for Java)类似于Commons Logging，是一套简易Java日志门面，本身并无日志的实现。
- jboss-logging：Hibernate、没有服务大众
- JUL：(Java Util Logging)自Java1.4以来的官方日志实现。
- Log4j：Apache Log4j是一个基于Java的日志记录工具。它是由Ceki Gülcü首创的，现在则是Apache软件基金会的一个项目。 Log4j是几种Java日志框架之一。
- Log4j2：Apache Log4j 2是apache开发的一款Log4j的升级产品。
- Logback：一套日志组件的实现(Slf4j阵营)。

|日志门面 |日志实现|
|:---:|:---:|
|JCL|Log4j|
|SLF4j✔|Log4j2|
|jbos-logging|Logback✔|
| |JUL|
日志框架的选择：SLF4j+Logback

Logback的使用与配置

使用：  
1. 传统方式
    ```
    private final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
    logger.debug("debug...");
    logger.info("info...");
    logger.error("error...");
    ```
2. 简便方式  
    添加依赖：
    ```
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    ```
    添加类注解：`@Slf4j`  
    使用：
    ```
    log.info("name: {}, password: {}", name, password);
    ```
配置：
1. application.yml  
    配置简单、功能简单：日志文件路径、输出格式
    ```yaml
    logging:
      pattern:
        console: "%d - %msg%n" # %d-时间 %msg-输出的内容 %n-换行
    #  path: src/main/resources
      file: src/main/resources/sell.log # 输出日志的文件名称
      level:
        com.tuyrk.sell: debug # SpringBoot2.0不支持直接配置debug了，需指定包名
    ```
2. logback-spring.xml  
    功能强大  
    LevelFilter：
    - DENY：禁止
    - NEUTRAL：中立
    - ACCEPT：接受
    
    需求：
    - 区分info和error日志
    - 每天产生一个日志文件
Logback日志配置（分级别输出到不同文件）
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!--所有级别输出到控制台-->
    <appender name="consoleLog" class="ch.qos.logback.core.ConsoleAppender">
        <layout class="ch.qos.logback.classic.PatternLayout">
            <pattern>
                %d - %msg%n
            </pattern>
        </layout>
    </appender>
    <!--输出到info-->
    <appender name="fileInfoLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>
                %d - %msg%n
            </pattern>
        </encoder>
        <!--滚动策略-->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--路径-->
            <fileNamePattern>log/info.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <!--输出到warn-->
    <appender name="fileWarnLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>
                %d - %msg%n
            </pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/warn.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <!--输出到error-->
    <appender name="fileErrorLog" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <encoder>
            <pattern>
                %d - %msg%n
            </pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>log/error.%d.log</fileNamePattern>
        </rollingPolicy>
    </appender>
    <!--分别设置对应的日志输出节点-->
    <root level="info">
        <appender-ref ref="consoleLog"/>
        <appender-ref ref="fileInfoLog"/>
        <appender-ref ref="fileWarnLog"/>
        <appender-ref ref="fileErrorLog"/>
    </root>
</configuration>
```
### 3-3 源码的使用
Git的使用

第4章 买家端类目
---
### 4-1 买家类目-dao（上）
- Dao层设计与开发
- Service层设计与开发
- Controller层设计与开发

1. 如果表名和类名不一致。如表名：table_name  类名：class_name，可以通过`@Table(name = "table_name")`进行类注解
    ```java
    @Table(name = "table_name")
    public class class_name {
        //...
    }
    ```
2. 主键的字段需要添加`@javax.persistence.Id`属性注解
3. SpringBoot+JPA插入包含自增字段的对象，需要为`@GeneratedValue`字段注解添加属性`strategy = GenerationType.IDENTITY`
    ```
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    ```
### 4-2 买家类目-dao（下）
3. 在做数据库自动更新时间时，如数据库有`update_time`字段，实体类有`updateTime`属性，依次进行查找-修改-保存，此时则不会保存，需要添加`@DynamicUpdate`类注解
4. `@org.springframework.transaction.annotation.Transactional`使用在单元测试和Service层中有些许不同
Service层：如果方法中抛出异常则回滚
单元测试：完全回滚
5. 在`JpaRepository`的继承类中想要自定义查询方法，则必须按照JPA的语法来规范书写方法名
    ```
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
    ```
6. SpringBoot+JPA的查询操作实体类必须要有一个默认的无参构造函数（在使用Mybatis时也需要无参构造函数），所以为了避免出现问题还是为每个实体类都添加一个无参构造函数吧。
### 4-3 买家类目-service

Assert.assertEquals()  
Assert.assertNotNull()

第5章 买家端商品
---
### 5-1 买家商品-dao
```
List<ProductInfo> findByProductStatus(Integer productStatus);
```
### 5-2 买家商品-service
查询分页：
```
public Page<ProductInfo> findAll(Pageable pageable) {
    return repository.findAll(pageable);
}
```
商品状态枚举类
```java
@Getter
public enum ProductStatusEnum {
    UP(0,"在架"),
    DOWN(1,"下架");
    private Integer code;
    private String message;
    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
```
### 5-3 买家商品-api（上）
vo：view object  
ProductInfoVO、ProductVO、ResultVO
```java
public class ResultVO<T> {
    /*错误码*/
    private Integer code;
    /*提示信息*/
    @JsonProperty("msg")
    private String message;
    /*具体内容*/
    private T data;
}
```
`@com.fasterxml.jackson.annotation.JsonProperty`字段注解修改传向前端的JSON数据的字段名称
### 5-4 买家商品-api（下）
1. 查询所有的上架商品  
Java8 lambda的使用
2. 查询类目(一次性查询)  
数据库查询不要放在循环中，这样时间开销很大很大的
3. 数据拼装  
将source的属性值复制到target：
`BeanUtils.copyProperties(source, target);`
【查看源码】多研究这个类`org.springframework.beans.BeanUtils`。
【查看源码】`org.springframework.util.StringUtils`

浏览器查看cookie：开发者工具-Application-Storage-Cookies-相应站点  
浏览器设置cookie：开发者工具-Console-输入`document.cookie='openid=abc123'`-回车

修改nginx配置：`vim /usr/local/nginx/conf/nginx.conf`  
重启Nginx服务器：`nginx -s reload`

修改不用使用IP地址访问，而使用域名：
1. 设置nginx服务器：`server-server_name: sell.com`
2. 设置Windows本机hosts文件：`c:\windows\system32\drivers\etc`

第6章 买家端订单
---
### 6-1 买家订单-dao(上)
...
### 6-2 买家订单-dao(下)
...
### 6-3 买家订单-service创建
步骤：
1. 查询商品（数量，价格）
2. 计算订单总价
3. 写入订单数据库（order_master和order_detail）
4. 扣库存（别忘了）
    注意扣库存时可能发生商品数目不足，扣除失败的情况
注意事项：
1. 实体类中多余了数据表的字段需要添加`@javax.persistence.Transient`字段注解，数据库对应的时候则会忽略这个字段
    ```
    @Transient
    private List<OrderDetail> orderDetailList;
    ```
2. 但是经常不像1中那样写，常常都是创建dto
dto：data transfer object(数据传输对象)
3. 订单总金额需要Service层计算，商品单价不能从前端传输，只能从数据库取出来。
4. 注意添加事务操作`org.springframework.transaction.annotation.Transactional`
### 6-7 买家订单-service查询
...
### 6-8 买家订单-service取消
1. 判断订单状态
2. 修改订单状态
3. 反还库存
    返还库存没有不足一说，直接加就是了
4. 如果已支付，需要退款
### 6-9 买家订单-service finish和paid
完结订单：
1. 判断订单状态
2. 修改订单状态

支付订单：
1. 判断订单状态
2. 判断支付状态
3. 修改支付状态

注：
`BeanUtils.copyProperties(source, target);`source中没有的属性target的值不会改变
### 6-10 买家订单-api
前台传递的信息可用ObjectForm对象接收
使用Gson将JSON字符串转换为List<Object>
```
gson.fromJson(listStr, new TypeToken<List<Object>>() {}.getType());
```
数据绑定：
`@Valid Object object, BindingResult bindingResult`
请求参数：
`@RequestParam(value = "page", defaultValue = "0") Integer page`
SpringBoot+JPA在进行分页时，返回对象最好是Page<Object>。因为有时候前台不一定只需要当前页的内容，有可能还需要当前页码、总页数等一系列信息

API接口返回时间戳:
1. 继承`JsonSerializer`抽象类
    ```java
    public class Date2LongSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeNumber(date.getTime() / 1000);
        }
    }
    ```
2. 在属性上添加注解`@JsonSerialize`
    ```
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date time;
    ```
【查看源码】了解`com.fasterxml.jackson.annotation.*`下的注解

API接口返回的值为NULL时默认不返回此字段：在实体类上做注解。
`@JsonInclude(JsonInclude.Include.NON_NULL)`
如果多个类都希望不返回值为NULL的字段，则采用yml文件配置：
```yaml
spring:
  jackson:
    default-property-inclusion: non_null
```

若某字段返回值为NULL，但是又必须返回该字段，规定此时返回空的中括号"[]"。
在实体类中赋一个初始值：
`private List<Object> objectList = new ArrayList<>();`

第7章 微信授权
---
微信授权
微信支付
微信退款
1. 微信网页授权
    官方文档：[https://mp.weixin.qq.com/wiki](https://mp.weixin.qq.com/wiki)
    调试：[https://natapp.cn/](https://natapp.cn/)
    第三方SDK：[https://github.com/Wechat-Group/WxJava](https://github.com/Wechat-Group/WxJava)
2. 获取openid
    手工方式
        1. 设置域名
        微信公众平台-微信公众账号-设置-公众号设置-功能设置-网页授权域名
        校验文件-放在网站的根目录下
        2. 获取code
        scope=snsapi_base/snsapi_userinfo
        3. 换取access_token 
    利用第三方SDK
        Mp：微信公众账号


NETAPP：内网穿透
[NATAPP1分钟快速新手图文教程](https://natapp.cn/article/natapp_newbie)
[用Natapp(ngrok)进行微信本地开发调试](https://natapp.cn/article/wechat_local_debug)


[微信测试号，微信公众号开发中token验证的解决办法，即接口配置信息中的url和token怎么设置的方法](https://blog.csdn.net/weixin_38306434/article/details/81384480)
### 7-6 微信网页授权前端调试
修改前端代码请求链接
vim /opt/code/sell_fe_buyer/config/index.js
sellUrl: "http://sell.com"
openidUrl: "http://tyk.nat300.top/sell/wechat/authorize"
重新构建代码
cd /opt/code/sell_fe_buyer
npm run build
ls -al dist/
\cp -r dist/* /opt/data/wwwroot/sell/

[Charles手机抓包实用教程](https://www.cnblogs.com/Clairewang/p/Charles.html)
默认端口：8888
微信开发者工具：公众号网页调试、小程序调试

第8章 微信支付和退款
---
[微信支付开发指南（内含微信账号借用）](http://www.imooc.com/article/31607)  
官方文档：[微信支付开发文档](https://pay.weixin.qq.com/wiki/doc/api/index.html)  
第三方SDK：[Pay-Group/best-pay-sdk](https://github.com/Pay-Group/best-pay-sdk)  
业务流程时序图
![](https://pay.weixin.qq.com/wiki/doc/api/img/chapter7_4_1.png)
### 8-1 发起微信支付-后端
1. 查询订单
2. 发起支付  
    1. 引入依赖
        ```xml
        <dependency>
            <groupId>cn.springboot</groupId>
            <artifactId>best-pay-sdk</artifactId>
            <version>1.2.0</version>
        </dependency>
        ```
    2. 配置账号信息、`WxPayH5Config`、`BestPayServiceImpl`
        - 支付公众号appId
        - 支付商户号
        - 支付商户密钥
        - 支付商户证书路径
        - 支付微信支付异步通知地址
    3. 使用SDK
        ```
        PayResponse payResponse = bestPayService.pay(payRequest);
        ```
    注：
    - 异步通知非常重要！
    - 格式化JSON数据工具类`JsonUtil`
### 8-3 在网页发起支付
1. 创建freemarker模板
    ```javascript
    function onBridgeReady() {
        WeixinJSBridge.invoke(
                'getBrandWCPayRequest', {
                    "appId": "${payResponse.appId}",     //公众号名称，由商户传入
                    "timeStamp": "${payResponse.timeStamp}",         //时间戳，自1970年以来的秒数
                    "nonceStr": "${payResponse.nonceStr}", //随机串
                    "package": "${payResponse.packAge}",
                    "signType": "MD5",         //微信签名方式：
                    "paySign": "${payResponse.paySign}" //微信签名
                },
                function (res) {
                    // if (res.err_msg === "get_brand_wcpay_request:ok") {
                    // 使用以上方式判断前端返回,微信团队郑重提示：
                    //res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
                    // }
                    location.href = "${returnUrl}";
                });
    }
    
    if (typeof WeixinJSBridge === "undefined") {
        if (document.addEventListener) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
        } else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
        }
    } else {
        onBridgeReady();
    }
    ```
2. 使用`ModelAndView`传递`PayResponse`、`returnUrl`信息
3. 支付成功，设置跳转路径
4. 修改前端配置：
   ```
   vim /opt/code/sell_fe_buyer/config/index.js
   wechatPayUrl: "http://tyk.nat300.top/sell/pay/create"
   ```
注：
1. "调用支付JSAPI缺少参数:total_fee"是指"prepay_id不正确"
2. "当前页面的URL未注册:http://tyk.com/pay.html"  
    微信公众平台-微信支付-开发配置-公众号支付-支付授权目录  
    查看URL配置是否正确
3. 创建订单生成预支付信息prepay_id，同一订单号不能重复创建预支付信息
### 8-5 微信异步通知
1. 配置异步通知notifyUrl
2. 验证签名、支付状态
    ```
    PayResponse payResponse = bestPayService.asyncNotify(notifyData);
    ```
3. 支付金额、支付人（下单人 == 支付人）
    注意验证金额是否相等时候的精度问题，可采用两数相减取精度
4. 修改订单的支付状态
5. 给微信反馈处理结果
    ```xml
    <xml>
        <return_code><![CDATA[SUCCESS]]></return_code>
        <return_msg><![CDATA[OK]]></return_msg>
    </xml>
    ```
### 8-7 微信退款
1. 下载证书：
    微信支付商户平台-账户设置-API安全-API证书-下载证书
2. 配置证书路径：payKeyPath
3. 调用SDK
    ```
    RefundResponse refundResponse = bestPayService.refund(refundRequest);
    ```
### 8-8 补充：使用测试号实现授权
授权
1. [申请测试号](https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login)
2. 获取appID和appsecret
3. [修改网页授权获取用户基本信息](https://www.cnblogs.com/tyk766564616/p/10256159.html)

支付
1. 区分测试号和借用号的openid。即同一个用户在不同公众号的openid不同。
2. 如果是借用支付账号，则要注意openid的匹配，支付的时候需要使用借用账号的openid
3. 请求示意图：支付授权目录 -> 用户外网 -> 用户电脑本机
    支付授权目录：http://sell.springboot.cn/sell/pay?openid=xxxxxx  
    用户外网：http://tyk.nat300.top/pay?openid=xxxxxx  
    用户电脑本机：http://127.0.0.1:8080/pay?openid=xxxxxx
4. 注意URL中的context-path
5. 代码`com.tuyrk.sell.controller.PayController.index();`

授权+支付
1. 注意URL中的context-path
    - 修改nginx配置：
    ```
    vim /usr/local/nginx/conf/nginx.conf
    server{
        location /sell/{
            proxy_pass http://主机地址:8080/;
        }
    }
    nginx -s reload
    ```

    - 修改前端配置：
    ```
    vim /opt/code/sell_fe_buyer/config/index.js
    wechatPayUrl: "http://sell.springboot.cn/sell/pay"
    npm run build
    \cp -r dist/* /opt/data/wwwroot/sell/
    ```
2. 微信网页授权使用API
    ```
    wechatPayUrl: "http://sell.springboot.cn/sell/wechat/authorize"
    ```
3. 异步通知地址notifyUrl可随意设置自己的外网地址

第9章 卖家端订单
---
### 9-1 卖家订单-service
查询所有订单接口：`public Page<OrderDTO> findList(Pageable pageable);`  
单元测试测试方法：`Assert.assertTrue(String message, boolean condition);`
### 9-2 卖家-订单-controller
freemarker传递参数的使用：
方法：`#{orderDTOPage.getTotalElements()}`
属性：`#{orderDTOPage.size}`
遍历List：
```
<#list orderDTOPage.content as orderODT>
    ${orderODT.orderId}<br>
</#list>
```
[BootStrap可视化布局](http://www.bootcss.com/p/layoutit/#)

根据枚举类型的code，获得枚举类型的实例：
枚举类型应实现下边的接口
```java
public interface CodeEnum {
    Integer getCode();
}
```
```java
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each : enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
```
```
@JsonIgnore
public OrderStatusEnum getOrderStatusEnum() {
    return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
}
```
```
${orderODT.getOrderStatusEnum().message}
```
### 9-4 卖家订单-controller-翻页
...
### 9-5 卖家订单-controller-取消订单
...
### 9-6 卖家订单-controller-订单详情
...
### 9-7 卖家订单-controller-完结订单
...

第10章 卖家端通用功能和上下架
---
### 10-1 关于模版的小技巧
使用freemarker修改前端页面内容时，可点击Build-Build Project(快捷键:Ctrl+F9)快速更新内容。  
切记：只适合更改模板文件的内容
### 10-2 实现边栏
...
### 10-3 实现商品列表
...
### 10-4 商品上下架-service
...
### 10-5 商品上下架-controller
freemarker字段默认值设置为空：`${msg!""}`、`${(object.field)!""}`

第11章 卖家端新增商品和类目
---
### 11-1 卖家商品-新增修改页面
freemarker判断字段是否存在：`(productInfo.categoryType)??`
### 11-2 卖家商品-修改表单提交
...
### 11-3 卖家商品-新增功能
...
### 11-4 卖家类目功能开发
...
###第12章 买家和卖家端联通
---
### 12-1 分布式session理论
|||
|:---|:---|
|登录|登出|
|验证身份，存储信息|失效浏览状态|

什么是分布式系统？  
旨在支持应用程序和服务的开发，可以利用物理架构由多个自治的处理元素，不共享主内存，但通过网络发送消息合作。

三个特点：多节点、消息通信、不共享内存

三个概念：分布式系统(distributed system)、集群(cluster)、分布式计算(distributed computing)

### 12-3 卖家信息表-dao开发
...
### 12-4 卖家扫码登录service开发
...
### 12-5 卖家扫码登录获取openid
微信开放平台

### 12-6 登录成功
1. openid去和数据库里的数据匹配
2. 设置token至redis
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```
```
@Autowired
private StringRedisTemplate redisTemplate;

redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openid, expire, TimeUnit.SECONDS);
```
3. 设置token至cookie
```
Cookie cookie = new Cookie(name, value);
cookie.setPath("/");
cookie.setMaxAge(maxAge);
response.addCookie(cookie);
```
### 12-7 登出成功
1. 从Cookie里查询
2. 从Redis数据库中清除信息
```
redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
```
3. 清除Cookie
### 12-8 AOP实现身份验证
```java
@Slf4j
@Aspect
@Component
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.tuyrk.sell.controller.Seller*.*(..))" +
            " && !execution(public * com.tuyrk.sell.controller.SellerUserController.login())")
    public void verify() {
    }

    @Before("verify()")
    public void doVerify() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        //查询Cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie == null) {
            log.warn("【登录校验】Cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        //去Redis里查询
        String tokenValue = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (StringUtils.isEmpty(tokenValue)) {
            log.warn("【登录校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    }
}
```
```java
@ControllerAdvice
public class SellExceptionHandler {
    @Autowired
    private ProjectUrlConfig projectUrlConfig;
    //拦截登录异常
    @ExceptionHandler(SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException() {
        return new ModelAndView("http://tyk.nat300.top/sell/wechat/qrAuthorize?returnUrl=http://tyk.nat300.top/sell/seller/login");
    }
}
```
### 12-9 微信模版消息推送
```
@Autowired
private WxMpService wxMpService;

WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
templateMessage.setTemplateId("8C7fzoKYap778H015J8zPAtkvMPRNkKn088RNgGeb2k");
templateMessage.setToUser(orderDTO.getBuyerOpenid());
List<WxMpTemplateData> data = Arrays.asList(
    new WxMpTemplateData("first", "亲，请记得收货。"),
    new WxMpTemplateData("keyword1", "微信点餐"),
    new WxMpTemplateData("keyword2", orderDTO.getBuyerPhone()),
    new WxMpTemplateData("keyword3", orderDTO.getOrderId()),
    new WxMpTemplateData("keyword4", orderDTO.getOrderStatusEnum().getMessage()),
    new WxMpTemplateData("keyword5", "￥" + orderDTO.getOrderAmount()),
    new WxMpTemplateData("remark", "欢迎再次光临！")
);
templateMessage.setData(data);
wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);
```
### 12-10 webSocket消息推送
前端：
```javascript
var websocket = null;
if ("WebSocket" in window) {
    websocket = new WebSocket("ws://tyk.nat300.top/sell/webSocket");
} else {
    alert("该浏览器不支持WebSocket！");
}
websocket.onopen = function (event) {
    console.log("WebSocket建立连接");
};
websocket.onclose = function (event) {
    console.log("WebSocket关闭连接");
};
websocket.onmessage = function (event) {
    console.log("收到消息：" + event.data);
    //弹窗提醒，播放音乐
};
websocket.onerror = function (event) {
    alert("WebSocket通信发生错误！");
};
window.onbeforeunload = function (event) {
    websocket.close();
};
```
后端：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```
```java
@Component
@ServerEndpoint("/webSocket")
public class WebSocket {
    private Session session;
    private static CopyOnWriteArraySet<WebSocket> webSocketSet = new CopyOnWriteArraySet<>();
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
    }
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }
    @OnMessage
    public void onMessage(String message) {
    }
    public void sendMessage(String message) {
        for (WebSocket websocket : webSocketSet) {
            websocket.session.getBasicRemote().sendText(message);
        }
    }
}
```
注：引入WebSocket之后，测试方法将会报错。在SpringBootTest增加webEnvironment参数
```
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTests {
    @Test
    public void contextLoads() {
    }
}
```
第13章 项目优化
---
### 13-1 异常捕获
```
@ControllerAdvice
public class SellExceptionHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SellException.class)
    public ResultVO handlerSellerException(SellException e) {
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
```
### 13-2 mybatis注解方式使用
1. 引入依赖
```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.2</version>
</dependency>
```
2. 书写Mapper接口
```
public interface ProductCategoryMapper {
    @Insert("INSERT INTO product_category(category_name, category_type) VALUES (#{categoryName, jdbcType=VARCHAR}, #{categoryType, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);
}
```
3. 添加`@Mapper`类注解或者`@MapperScan(basePackages = "com.tuyrk.mapper")`启动类注解
4. 建立dao层进行封装，然后在server层中使用（也可在server层中直接引用mapper，但不推荐）

在控制台查看MyBatis输出的SQL语句：
```
logging:
  level:
    com.tuyrk.sell.dataobject.mapper: trace # 查看MyBatis的SQL语句
```
### 13-4 mybatis xml方式使用
```
@Mapper
public interface ProductCategoryMapper {
    ProductCategory selectByCategoryType(Integer categoryType);
}
```
```
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tuyrk.ProductCategoryMapper">
    <resultMap id="baseResultMap" type="com.tuyrk.ProductCategory">
        <id column="category_id" property="categoryId" jdbcType="INTEGER"/>
        <id column="category_name" property="categoryName" jdbcType="VARCHAR"/>
        <id column="category_type" property="categoryType" jdbcType="INTEGER"/>
    </resultMap>
    <select id="selectByCategoryType" resultMap="baseResultMap" parameterType="java.lang.Integer">
        SELECT category_id, category_name, category_type
        FROM product_category
        WHERE category_type = #{categoryType, jdbcType=INTEGER};
    </select>
</mapper>
```
```
mybatis:
  mapper-locations: classpath:mapper/*.xml
```
### 13-5 jpa和mybatis的选择
- 喜欢什么就用什么。
- 建表用SQL，不用JPA建表
- 表与表之间的关系采用程序逻辑控制，慎用@OneToMany和@ManyToOne
### 13-6 ab压测介绍
使用建议工具Apache ab、JMeter、loadrunner
```
ab -n 100 -c 100 http://www.imooc.com
ab -t 100 -c 100 http://www.imooc.com
```
### 13-7 synchronized处理并发
- synchronized关键字是一种解决方法
- 无法做到细粒度的控制
- 只适合单点的情况
### 13-8 redis分布式锁
[Redis中文网](www.redis.cn)
```
setnx key value
getset key value
```
加锁操作
```
public boolean lock(String key, String value) {
    if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
        return true;
    }
    String currentValue = redisTemplate.opsForValue().get(key);
    //如果锁过期
    if (!StringUtils.isEmpty(currentValue)
            && Long.valueOf(currentValue) < System.currentTimeMillis()) {
        //获取上一个锁的时间
        String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
        return !StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue);
    }
    return false;
}
```
解锁操作
```
public void unlock(String key, String value) {
    String currentValue = redisTemplate.opsForValue().get(key);
    if (!StringUtils.isEmpty(currentValue) && currentValue.equals(value)) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }
}
```
- 支持分布式
- 可以更细粒度的控制
- 多台机器上多个进程对一个数据进行操作的互斥
### 13-9 redis缓存的使用
数据缓存层：命中、失效、更新
1. 启动类注解@EnableCaching
2. 序列化返回的实体类(GenerateSerialVersionUID插件的使用)
3. 
- 查找：`@Cacheable(cacheNames = "product",key = "productId")`
- 更新：`@CachePut(cacheNames = "product",key = "productId")`
- 删除：`@CacheEvict(cacheNames = "product",key = "productId")`

注：cacheNames相同的类可以使用类注解@CacheConfig(cacheNames = "product")

SPEL表达式的使用：  
```
@Cacheable(cacheNames = "product", key = "#sellerId", condition = "#sellerId.length() > 3", unless = "#result.code != 0")
```
第14章 项目部署
---
pwd：查看当前目录
ps -ef|grep sell.jar：查看进程
kill -9 端口号：杀死进程

- tomcat

- java -jar
1. 使用mvn打包：`mvn clean package`
    重命名打包名称：
    ```
    <build>
        <finalName>sell</finalName>
    </build>
    ```
    打包时跳过测试：
    ```
    <properties>
        <skipTests>true</skipTests>
    </properties>
    ``` 
    ```
    mvn clean package -Dmaven.test.skip=true
    ```
    ```
    mvn clean package -DskipTests
    ```
2. 上传至服务器
    ```
    scp target/sell.jar root@127.0.0.2:/opt/javaapps
    ```
3. 启动：`java -jar /opt/javaapps/sell.jar`
修改启动端口
```
java -jar -Dserver.port=8090 sell.jar
```
指定启动环境：
```
java -jar -Dspring.profiles.active=prod sell.jar
```
后台启动：
```
nohup java -jar sell.jar > /dev/null 2>&1 &
```
4. 脚本启动
```
vim start.sh
```
```
# !/bin/sh
nohup java -jar sell.jar > /dev/null 2>&1 &
```
```
bash start.sh
```
5. CentOS7推荐用法：Service
```
vim /etc/systemd/system/sell.service
```
```
[Unit]
Description=sell #服务描述
After=syslog.target network.target #服务启动依赖

[Service]
Type=simple #服务启动模式

ExecStart=/usr/bin/java -jar -Dspring.profiles.active=prod /opt/javaapps/sell.jar #启动命令
ExecStop=/bin/kill 15 $MAINPID

User=root #可注释
Group=root

[Install]
WantedBy=mmulti-user.target
```
```
systemctl daemon-reload #重新加载

systemctl start sell #启动
或
systemctl start sell.service

systemctl stop sell #停止

systemctl enable sell #设置开机启动

systemctl disable sell #取消开机启动
```
6. Docker容器编排

第15章 课程总结
---
人生三重境界 --王国维  
第一境界：昨夜西风凋碧树。独上高楼，望尽天涯路。  
第二境界：衣带渐宽终不悔，为伊消得人憔悴。  
第三境界：众里寻他千百度。蓦然回首，那人却在，灯火阑珊处。

回顾：
- 项目分析设计
- 微信特性
- 微信支付推荐
- Token认证
- WebSocket消息
- Redis缓存+分布式锁

总结：
- 设计开发了一个完整的项目
- 没有做到应用独立
- 部署不够便捷

Spring Cloud构建微服务
- 服务发现--Netflix Eureka
- 负载均衡--Netflix Ribbon
- 断路器--Netflix Hystrix
- 服务网关--Netflix Zuul
- 分布式配置--String Cloud Config
- 消息总线--Spring Cloud Bus

RabbitMQ的使用与实战  
Docker+Rancher