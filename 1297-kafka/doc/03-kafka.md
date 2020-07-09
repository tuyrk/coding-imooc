# 第3章 Day02 Kafka急速入门与实战

## 3-1 本章导航

> - Kafka核心概念&设计&应用场景
> - Kafka环境搭建&急速入门
> - 与SpringBoot整合实战
> - Kafka高吞吐量核心实战-日志收集设计
> - Kafka高吞吐量核心实战-日志输出（日志组件输出log4j2）
> - Kafka高吞吐量核心实战-日志收集（FileBeat）
> - Kafka高吞吐量核心实战-日志过滤（logstash）
> - Kafka高吞吐量核心实战-日志持久化（ElasticSearch）
> - Kafka高吞吐量核心实战-日志可视化（Kibana）
> - 分布式日志收集、链路跟踪、监控告警平台架构讲解

## 3-2 [kafka核心概念与应用场景](03-02-kafka-concept.md)

## 3-3 [kafka急速入门与核心API解析](03-03-kafka-api.md)

## 3-4 Kafka与springboot整合_生产者讲解

SpringBoot2.x整合Kafka步骤

> 1. Maven配置
> 2. application.properties
> 3. 创建KafkaTemplate对象
> 4. @KafkaListener监听消息

### Maven配置

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### application.properties

```yml
server:
  servlet:
    context-path: /kafka
  port: 8080

## SpringBoot 整合 Kafka
spring:
  kafka:
    bootstrap-servers: localhost:9002
    producer:
      retries: 0 # kafka producer发送消息失败时的重试次数
      batch-size: 16384 # 批量发送数据的配置
      # kafka消息的序列化配置
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      acks: 1 # kafka生产端最重要的选项，可靠性投递的配置项
```

> - acks=0：生产者在成功写入消息之前，不会等待任何来自服务器的相应
>
> - acks=1：只要集群的首领节点收到消息，生产者就会收到一个来自服务器的成功响应
>
> - acks=-1/ALL：分区leader必须等待消息被成功写入到所有的ISR副本（同步副本）中,才认为producer请求成功。这种方案提供最高的消息持久性保证，但是理论上吞吐率也是最差的
>
>   <img src="https://tva1.sinaimg.cn/large/00831rSTgy1gd8xjbkqfcj30ju06fdfy.jpg" alt="Acks请求" style="zoom:80%;" />

### 创建KafkaTemplate对象

```java
@Slf4j
@Component
public class KafkaProducerService {
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("发送消息失败：{}", throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> stringObjectSendResult) {
                log.info("发送消息成功：{}", stringObjectSendResult.toString());
            }
        });
    }
}
```

## 3-5 Kafka与springboot整合_消费者讲解

### application.properties

```yaml
## SpringBoot 整合 Kafka
spring:
  kafka:
    bootstrap-servers: localhost:9002
    consumer:
      enable-auto-commit: false # consumer消息的签收机制：手工签收
      auto-offset-reset: earliest # 在偏移量无效的情况下，消费者将从起始位置读取分区的记录
      # kafka消息的序列化配置
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    listener:
      ack-mode: manual # 手工签收
      concurrency: 5 # 并行度
```

> auto-offset-reset属性指定了消费者在读取一个没有偏移量的分区或者偏移量无效的情况下该做何处理：
>
> - latest（默认值）：在偏移量无效的情况下，消费者将从最新的记录开始读取数据（在消费者启动之后生成的记录）
> - earliest：在偏移量无效的情况下，消费者将从起始位置读取分区的记录

### @KafkaListener监听消息

```java
@Slf4j
@Component
public class KafkaConsumerService {
    @KafkaListener(groupId = "tuyrk-kafka-group", topics = {"tuyrk-kafka-topic"})
    public void onMessage(ConsumerRecord<String, String> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
        log.info("消费端接收消息：{}", record.value());
        // 手工签收机制
        acknowledgment.acknowledge();
    }
}
```

### 测试

```java
@Autowired
private KafkaProducerService kafkaProducerService;

@Test
public void send() throws InterruptedException {
  String topic = "tuyrk-kafka-topic";
  for (int i = 1; i <= 1000; i++) {
    kafkaProducerService.sendMessage(topic, "hello kafka" + i);
    Thread.sleep(5);
  }
  Thread.sleep(Integer.MAX_VALUE);
}
```

> Partition与Consumer一对一关系。如果Partition有10个，而Consumer有20个，则有10个Consumer是没有用的
