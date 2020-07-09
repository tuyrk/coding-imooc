# 第4章 Day03~Day04 Kafka海量日志收集系统架构设计

title: 04-Kafka海量日志收集系统架构设计
date: 2020-03-28 15:21:49
categories: [分布式消息队列-Kafka]
tags: [kafka]

---

@[TOC]

## 4-1 Kafka海量日志收集实战_架构设计讲解

- ELK技术栈的架构示意图

  <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd9oxf1cp9j312y0hf75w.jpg" alt="ELK技术栈的架构示意图" style="zoom:80%;" />

  > Kafka做缓冲，为Broker；Filebeat为Producer；Logstash为Comsumer

- 海量日志收集实战架构设计图

  <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd9oyc8rbkj318g0al0uf.jpg" alt="海量日志收集实战架构设计图" style="zoom:80%;" />

## 4-2 Kafka海量日志收集实战_log4j2日志输出实战-1

Log4j2：日志输出、日志分级、日志过滤、MDC线程变量

1. Maven配置

   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-test</artifactId>
       <scope>test</scope>
       <exclusions>
           <exclusion>
               <groupId>org.junit.vintage</groupId>
               <artifactId>junit-vintage-engine</artifactId>
           </exclusion>
       </exclusions>
   </dependency>
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-log4j2</artifactId>
   </dependency>
   <dependency>
       <groupId>com.lmax</groupId>
       <artifactId>disruptor</artifactId>
       <version>3.3.4</version>
   </dependency>
   ```

2. application.yml

   ```yaml
   spring:
     application:
       name: kafka=log-collect
     http:
       encoding:
         charset: utf-8
     jackson:
       date-format: yyyy-MM-dd HH:mm:ss
       time-zone: GMT+8
       default-property-inclusion: non_null
   ```

3. IndexController

   ```java
   @Slf4j
   @RestController("index")
   public class IndexController {
       @GetMapping("")
       public String index(){
           log.info("我是一条info日志.");
           log.warn("我是一条warn日志.");
           log.error("我是一条error日志.");
           return "idx";
       }
   }
   ```

4. log4j2.xml

   ```xml
   <?xml version="1.0" encoding="utf-8" ?>
   <Configuration status="INFO" schema="Log4J-V2.0.xsd" monitorInterval="600">
       <Properties>
           <Property name="LOG_HOME">logs</Property>
           <Property name="FILE_NAME">kafka-log-collect</Property>
           <Property name="patternLayout">[%d{yyyy-MM-dd'T'HH:mm:ss.SSSZZ}] [%level{length=5}] [%thread-%tid] [%logger] [%X{hostName}] [%X{ip}] [%X{applicationName}] [%F, %L, %C, %M] [%m] ## '%ex'%n</Property>
       </Properties>
       <Appenders>
           <Console name="CONSOLE" target="SYSTEM_OUT">
               <PatternLayout pattern="${patternLayout}"/>
           </Console>
           <!--全量日志-->
           <RollingRandomAccessFile name="appAppender" fileName="${LOG_HOME}/app-${FILE_NAME}.log" filePattern="${LOG_HOME}/app-${FILE_NAME}-%d{yyyy-MM-dd}-%i.log">
               <PatternLayout pattern="${patternLayout}"/>
               <Policies>
                   <TimeBasedTriggeringPolicy interval="1"/>
                   <SizeBasedTriggeringPolicy size="500M"/>
               </Policies>
               <DefaultRolloverStrategy max="20"/>
           </RollingRandomAccessFile>
           <!--错误日志,warn级别以上-->
           <RollingRandomAccessFile name="errorAppender" fileName="${LOG_HOME}/error-${FILE_NAME}.log" filePattern="${LOG_HOME}/error-${FILE_NAME}-%d{yyyy-MM-dd}-%i.log">
               <PatternLayout pattern="${patternLayout}"/>
               <Filters>
                   <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
               </Filters>
               <Policies>
                   <TimeBasedTriggeringPolicy interval="1"/>
                   <SizeBasedTriggeringPolicy size="500M"/>
               </Policies>
               <DefaultRolloverStrategy max="20"/>
           </RollingRandomAccessFile>
       </Appenders>
       <Loggers>
           <!--业务相关 异步logger-->
           <AsyncLogger name="com.tuyrk.*" level="info" includeLocation="true">
               <AppenderRef ref="appAppender"/>
           </AsyncLogger>
           <AsyncLogger name="com.tuyrk.*" level="info" includeLocation="true">
               <AppenderRef ref="errorAppender"/>
           </AsyncLogger>
           <Root level="info">
               <Appender-ref ref="CONSOLE"/>
               <Appender-ref ref="appAppender"/>
               <Appender-ref ref="errorAppender"/>
           </Root>
       </Loggers>
   </Configuration>
   ```

## 4-3 Kafka海量日志收集实战_log4j2日志输出实战-2

```java
@Component
public class InputMDC implements EnvironmentAware {
    private static Environment environment;
    @Override
    public void setEnvironment(Environment environment) {
        InputMDC.environment = environment;
    }

    public static void putMDC() {
        MDC.put("hostName", NetUtils.getLocalHostname());
        MDC.put("ip", NetUtils.getMacAddressString());
        MDC.put("applicationName", environment.getProperty("spring.application.name"));
    }
}
```

```java
@Slf4j
@RestController
@RequestMapping("index")
public class IndexController {
    @GetMapping("err")
    public String err() {
        InputMDC.putMDC();
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            log.error("算术异常", e);
        }
        return "err";
    }
}
```

```
http://localhost:8081/kafka/index
http://localhost:8081/kafka/index/err
```

## 4-4 Kafka海量日志收集实战_filebeat日志收集实战-1

FileBeat：安装入门、配置文件、对接Kafka、实战应用

1. filebeat安装

   ```shell
   brew install filebeat
   ```

2. 配置filebeat，可以参考filebeat.full.yml（指定配置文件位置来启动filebeat）

   ```shell
   vim ~/Develop/1297-kafka/src/main/resources/filebeat.yml
   ```

   ```yaml
   filebeat.prospectors:
   - input_type: log
     paths: # 日志路径
       ## app-服务名称.log，为什么写死？防止发生轮转抓取历史数据
       - /Users/tuyuankun/Develop/1297-kafka/logs/app-kafka-log-collect.log
     # 定义写入ES时的_type值
     document_type: "app-log"
     multiline:
       # pattern: '^\s*(\d{4}|\d{2})\-(\d{2}|[a-zA-Z]{3})\-(\d{2}|\d{4})' # 指定匹配的表达式（配置）
       pattern: '^\['      # 指定配置的表达式（匹配以"{开头的字符串）
       nagate: true        # 是否匹配到
       match: after        # 合并到上一行的末尾
       max_lines: 2000     # 最大的行数
       timeout: 2s         # 如果在规定时间没有新的日志事件就不等待后面的日志，把已收集到的就推送到其他地方
     fields:
       logbiz: kafka-log-collect   # 应用名称
       logtopic: app-kafka-log-collect # 按服务划分用作kafka topic
       evn: dev
   
   - input_type: log
     paths: # 日志路径
       - /Users/tuyuankun/Develop/1297-kafka/logs/error-kafka-log-collect.log
     document_type: "error-log"
     multiline:
       # pattern: '^\s*(\d{4}|\d{2})\-(\d{2}|[a-zA-Z]{3})\-(\d{2}|\d{4})' # 指定匹配的表达式（配置）
       pattern: '^\['      # 指定配置的表达式（匹配以"{开头的字符串）
       nagate: true        # 是否匹配到
       match: after        # 合并到上一行的末尾
       max_lines: 2000     # 最大的行数
       timeout: 2s         # 如果在规定时间没有新的日志事件就不等待后面的日志，把已收集到的就推送到其他地方
     fields:
       logbiz: kafka-log-collect   # 应用名称
       logtopic: error-kafka-log-collect # 按服务划分用作kafka topic
       evn: dev
   
   output.kafka:
     enabled: true
     hosts: ["localhost:9092"]
     topic: '%{[fields.logtopic]}'
     partition.hash:
       reachable_only: true
     compression: gzip
     max_message_bytes: 1000000
     required_acks: 1
   logging.to_files: true
   ```

3. 检查配置是否正确

   ```shell
   filebeat test config ~/Develop/1297-kafka/src/main/resources/filebeat.yml
   # Config OK
   ```

4. 启动filebeat

   ```shell
   filebeat -c ~/Develop/1297-kafka/src/main/resources/filebeat.yml
   ps -ef | grep filebeat
   ```

> **注**：
>
> 1. 在启动filebeat前先启动项目、zookeeper、kafka、创建topic
>
> 2. filebeat启动日志：`cat /usr/local/var/log/filebeat/filebeat  `
>
> 3. 错误：ERROR	fileset/modules.go:95	Not loading modules. Module directory not found: /usr/local/Cellar/filebeat/6.2.4/module
>
>    解决方案：将`/usr/local/etc/filebeat/module`复制到`/usr/local/Cellar/filebeat/6.2.4/module`。（**brew安装真是坑**）
>
> 4. 日志路径`paths`不能写作`~/Develop/1297-kafka/logs/app-kafka-log-collect.log`，须写作`/Users/tuyuankun/Develop/1297-kafka/logs/app-kafka-log-collect.log`

## 4-5 Kafka海量日志收集实战_filebeat日志收集实战-2

1. 查看topic情况

   ```shell
   kafka-topics --zookeeper localhost:2181 --describe --topic app-kafka-log-collect
   ```

2. 查看kafka是否有数据

   ```shell
   # 查看kafka配置文件的log.dirs属性，即获取kafka日志文件夹路径
   cat /usr/local/etc/kafka/server.properties 
   # log.dirs=/usr/local/var/lib/kafka-logs
   # 进入partition对应的文件夹下查看
   cd /usr/local/var/lib/kafka-logs/app-kafka-log-collect-0/
   ```

## 4-6 附：[logstash基础语法与使用](04-06-logstash-base.md)

安装ElasticSearch、Kibana、Logstash（官网安装方法）

```shell
brew tap elastic/tap
brew install elastic/tap/elasticsearch-full
brew install elastic/tap/kibana-full
brew install elastic/tap/logstash-full
```

注：

1. 不要使用`brew install elasticsearch`安装，因为此方法安装了oss版，而不能使用oss版

2. 错误：`Error opening log file 'logs/gc.log': No such file or directory`

   解决方案：手动在`/usr/local/var/homebrew/linked/elasticsearch-full/libexec`创建`logs`目录

## 4-7 Kafka海量日志收集实战_logstash日志过滤实战-1

logstash：安装入门、ELK环境搭建、基础语法、实战应用

```shell
## logstash-script.conf
## multiline 插件也可以用于其他类似的堆栈式信息，比如，Linux的内核日志
input {
  kafka {
    ## app-log-服务名称
    topics_pattern => "app-kafka-log-.*" ## 接收哪些topic下的消息
    bootstrap_servers => "localhost:9092" ## kafka server
    codec => json
    consumer_threads => 1 ## 增加consumer的并行消费线程数，一个partition对应一个consumer_thread
    decorate_events => true
    # auto_offset_rest => "latest"
    group_id => "app-kafka-log-group" ## kafka组
  }

  kafka {
    ## error-log-服务名称
    topics_pattern => "error-kafka-log-.*"
    bootstrap_servers => "localhost:9092"
    codec => json
    consumer_threads => 4
    decorate_events => true
    # auto_offset_rest => "latest"
    group_id => "error-kafka-log-group"
  }
}

filter {
  ## 时区转换
  ruby {
    code => "event.set('index_time', event.timestamp.time.localtime.strftime('%Y.%m.%d'))"
  }

  if "app-kafka-log-collect" in [fields][logtopic] { ## `[fields][logtopic]`为filebeat配置文件`filebeat.yml`的属性
    grok {
      ## 表达式
      match => ["message", "\[%{NOTSPACE:currentDateTime}\] \[%{NOTSPACE:level}\] \[%{NOTSPACE:thread-id}\] \[%{NOTSPACE:class}\] \[%{DATA:hostName}\] \[%{DATA:ip}\] \[%{DATA:applicationName}\] \[%{DATA:location}\] \[%{DATA:messageInfo}\] ## (\'\'|%{QUOTEDSTRING:throwable})"]
    }
  }

  if "error-kafka-log-collect" in [fields][logtopic] {
    grok {
      ## 表达式
      match => ["message", "\[%{NOTSPACE:currentDateTime}\] \[%{NOTSPACE:level}\] \[%{NOTSPACE:thread-id}\] \[%{NOTSPACE:class}\] \[%{DATA:hostName}\] \[%{DATA:ip}\] \[%{DATA:applicationName}\] \[%{DATA:location}\] \[%{DATA:messageInfo}\] ## (\'\'|%{QUOTEDSTRING:throwable})"]
    }
  }
}

## 测试输出到控制台
output {
  stdout {codec => rubydebug}
}
```

## 4-8 Kafka海量日志收集实战_logstash日志过滤实战-2

启动logstash

```shell
logstash -f ~/Develop/1297-kafka/src/main/resources/logstash-script.conf
```

## 4-9 Kafka海量日志收集实战_elasticsearch&kibana存储可视化实战

1. Kafka高吞吐量核心实战-日志持久化、可视化

   > - Elasticsearch索引创建周期、命名规范选择
   > - Kibana控制台应用、可视化日志

2. Kafka高吞吐量核心实战-监控告警

   > Watcher作用介绍基本使用=>Watcher API详解=>Watcher实战应用监控告警

### ElasticSearch设置密码

> 7.3版本已经可以免费使用x-pack设置账号和密码，且无需安装x-pack

1. 生成证书

   ```shell
   cd /usr/local/var/homebrew/linked/elasticsearch-full/libexec
   elasticsearch-certutil cert -out config/elastic-certificates.p12 -pass ""
   ```

2. 添加xpack配置

   ```shell
   vim /usr/local/Cellar/elasticsearch-full/7.6.2/libexec/config/elasticsearch.yml
   ```

   ```yaml
   xpack.security.enabled: true
   xpack.security.transport.ssl.enabled: true
   xpack.security.transport.ssl.verification_mode: certificate
   xpack.security.transport.ssl.keystore.path: elastic-certificates.p12
   xpack.security.transport.ssl.truststore.path: elastic-certificates.p12
   ```

3. 启动ElasticSearch，并新开一个终端生成账号密码

   ```shell
   elasticsearch
   
   # 自动生成默认用户和密码
   elasticsearch-setup-passwords auto
   # 手动生成密码,elastic用户是超级管理员
   elasticsearch-setup-passwords interactive
   ```

4. 验证。打开主页,输入账号密码

   ```
   http://127.0.0.1:9200/
   ```

5. 修改密码

   ```shell
   curl -H "Content-Type: application/json" -XPUT -u elastic:rpZaWNQcXSdjnpQqAtUE 'http://localhost:9200/_xpack/security/user/elastic/_password' -d '{"password" : "123456"}'
   ```

   - `elastic:rpZaWNQcXSdjnpQqAtUE`：用户名:旧密码
   - `123456`：新密码



- 修改Kibana配置文件

  ```shell
  vim /usr/local/etc/kibana/kibana.yml
  ```

  ```yaml
  elasticsearch.username: "elastic"
  elasticsearch.password: "123456"
  ```

- 启动ElasticSearch、Kibana

  ```shell
  elasticsearch
  kibana
  ```

- 打开地址访问Kibana

  ```
  http://127.0.0.1:5601/
  ```

  

```shell
## logstash-script.conf
## elasticsearch
output {
  if "app-kafka-log-collect" in [fields][logtopic] {
    ## es插件
    elasticsearch {
      # es服务地址
      hosts => ["localhost:9200"]
      # 用户名密码
      user => "elastic"
      password => "123456"
      ## 索引名，+号开头的，就会自动认为后面是时间格式：
      ## javalog-app-server-2019.01.23
      index => "app-log-%{[fields][logbiz]}-%{index_time}"
      ## 是否嗅探集群ip：一般设置true；http://localhost:9200/_nodes/http?pretty
      ## 通过嗅探机制进行es集群负载均衡发日志消息
      sniffing => true
      ## logstash默认自带一个mapping模板，进行模板覆盖
      template_overwrite => true
    }
  }
  if "error-kafka-log-collect" in [fields][logtopic] {
    elasticsearch {
      hosts => ["localhost:9200"]
      user => "elastic"
      password => "123456"
      index => "error-log-%{[fields][logbiz]}-%{index_time}"
      sniffing => true
      template_overwrite => true
    }
  }
}
```

## 4-10 Kafka海量日志收集实战_watcher监控告警实战-1

```java
@Data
public class AccurateWatcherMessage {
    private String title;
    private String application;
    private String level;
    private String body;
    private String executionTime;
}
```

```java
@PostMapping("accurateWatch")
public String accurateWatch(AccurateWatcherMessage accurateWatcherMessage) {
    System.out.println("-------警告内容-------" + accurateWatcherMessage);
    return "is watched" + accurateWatcherMessage;
}
```

## 4-11 Kafka海量日志收集实战_watcher监控告警实战-2

1. 创建Watcher之前手动指定创建模板

   ```json
   // PUT _template/error-log-
   {
     "template": "error-log-*",
     "order": 0,
     "settings": {
       "index": {
         "refresh_interval": "5s"
       }
     },
     "mappings": {
       "dynamic_templates": [
         {
           "message_field": {
             "match_mapping_type": "string",
             "path_match": "message",
             "mapping": {
               "norms": false,
               "type": "text",
               "analyzer": "ik_max_word",
               "search_analyzer": "ik_max_word"
             }
           }
         },
         {
           "throwable_field": {
             "match_mapping_type": "string",
             "path_match": "throwable",
             "mapping": {
               "norms": false,
               "type": "text",
               "analyzer": "ik_max_word",
               "search_analyzer": "ik_max_word"
             }
           }
         },
         {
           "string_field": {
             "match_mapping_type": "string",
             "path_match": "*",
             "mapping": {
               "norms": false,
               "type": "text",
               "analyzer": "ik_max_word",
               "search_analyzer": "ik_max_word",
               "fields": {
                 "keyword": {
                   "type": "keyword"
                 }
               }
             }
           }
         }
       ],
       "properties": {
         "hostName": {
           "type": "keyword"
         },
         "ip": {
           "type": "ip"
         },
         "level": {
           "type": "keyword"
         },
         "currentDateTime": {
           "type": "date"
         }
       }
     }
   }
   ```

2. 创建一个Watcher，比如定义一个trigger，每5秒钟看一下input里的数据

   ```json
   // PUT _xpack/watcher/watch/error_log_collector_watcher
   {
     "trigger": {
       "schedule": {
         "interval": "5s"
       }
     },
     "input": {
       "search": {
         "request": {
           "indices": ["<error-log-{now+8h/d}>"],
           "body": {
             "size": 0,
             "query": {
               "bool": {
                 "must": [
                   {
                     "term": {"level": "ERROR"}
                   }
                 ],
                 "filter": {
                   "range": {
                     "currentDateTime": {
                       "gt": "now-30s", "lt": "now"
                     }
                   }
                 }
               }
             }
           }
         }
       }
     },
     "condition": {
       "compare": {
         "ctx.payload.hits.total": {
           "gt": 0
         }
       }
     },
     "transform": {
       "search": {
         "request": {
           "indices": ["error-log-{now+8h/d}"],
           "body": {
             "size": 1,
             "query": {
               "bool": {
                 "must": [
                   {
                     "term": {"level": "ERROR"}
                   }
                 ],
                 "filter": {
                   "range": {
                     "currentDateTime": {
                       "gt": "now-30s", "lt": "now"
                     }
                   }
                 }
               }
             },
             "sort": [
               {
                 "currentDateTime": {
                   "order": "desc"
                 }
               }
             ]
           }
         }
       }
     },
     "actions": {
       "test_error": {
         "webhook": {
           "method": "POST",
           "url": "http://localhost:8081/kafka/index/accurateWatch",
           "body": "{\"title\": \"异常错误告警\", \"applicationName\": \"{{#ctx.payload.hits.hits}}{{_source.applicationName}}{{/ctx.payload.hits.hits}}\", \"level\": \"告警级别P1\", \"body\": \"{{#ctx.payload.hits.hits}}{{_source.messageInfo}}{{/ctx.payload.hits.hits}}\", \"executionTime\": \"{{#ctx.payload.hits.hits}}{{_source.currentDateTime}}{{/ctx.payload.hits.hits}}\"}"
         }
       }
     }
   }
   ```

3. 创建index pattern

## 4-12 [附：watcher 基础语法与使用](04-12-watcher-base.md)

## 4-13 总结与复习

> HI ，小伙伴们！本章节我们已经对Kafka技术有了一个全面的认识，通过学习Kafka的基础API使用，到与SpringBoot整合应用，再延伸到ELK技术栈实现日志输出（Log4j2）、数据抓取（Filebeat）、数据转储（Kafka Broker）；再到Logstash消费，然后Sink到Elasticsearch平台，通过Kibana进行展示，最后在使用Xpack-Watcher进行对日志的监控告警。全链路的讲解了Kafka 与 ELK之间的关系与海量日志实战应用，接下来我们来进行复习！

- Kafka基础知识总结

  <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gdgpvphseej30vi0jm0v6.jpg" alt="Kafka基础知识总结" style="zoom:100%;" />

- ELK技术栈架构图

  <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gdgpvo51fuj30oz0avwfh.jpg" alt="ELK技术栈架构图" style="zoom:100%;" />

- ELK实战流程图

  <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gdgpvoziiij30q207dgmq.jpg" alt="ELK实战流程图" style="zoom:100%;" />
  
  启动命令
  
  ```shell
  zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties # 启动zookeeper
  kafka-server-start /usr/local/etc/kafka/server.properties # 启动kafka
  filebeat -c ~/Develop/1297-kafka/src/main/resources/filebeat.yml # 启动filebeat
  elasticsearch # 启动ElasticSearch
  kibana # 启动Kibana
  logstash -f ~/Develop/1297-kafka/src/main/resources/logstash-script.conf # 启动logstash
  ```

## 4-14 作业练习

> 在这里，我们历经千辛万苦终于搞定了复杂的MQ架构设计与落地；无论是RabbitMQ还是Kafka，都是业界非常主流的MQ技术（消息中间件），接下来就是作业时间！

- **作业一：** 请对比一下RabbitMQ 和 Kafka 各自的特点与优势，列出重要的区别点。

- **作业二：** 对于ELK海量日志收集，还有一个非常关键的问题，就是我们收集上来的日志应该如何处理、分析？请列出几点你觉得日志可以做的事情，充分发挥出想象力！（提示：比如做QPS的接口统计）