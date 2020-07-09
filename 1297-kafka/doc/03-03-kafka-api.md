# 3-3 kafka急速入门与核心API解析

@[TOC]

### kafka环境安装

上一节课我们已经对kafka的基本概念、核心思想有了一定的了解和认知，并且掌握了kafka在实际工作中的一些主要的应用场景。那么接下来，我们就一起快速进入kafka的安装吧。

- kafka下载地址：http://kafka.apache.org/downloads.html

- kafka安装步骤：首先kafka安装需要依赖与zookeeper，所以小伙伴们先准备好zookeeper环境（三个节点即可），然后我们来一起构建kafka broker。

  ```shell
  brew install zookeeper # 安装zookeeper
  brew install kafka # 安装kafka
  zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties # 启动zookeeper
  kafka-server-start /usr/local/etc/kafka/server.properties # 启动kafka
  ```

### kafka常用命令

我们接下来一起了解几个非常重要的命令，通过这些命令我们对kafka topic partition 进行查看和操作。

- 常用命令：

  1. **创建topic主题命令**：（创建名为test的topic， 1个分区分别存放数据，数据备份总共1份）

     ```shell
     kafka-topics --zookeeper localhost:2181 --create --topic tuyrk-kafka-topic --partitions 1  --replication-factor 1
     ```

     >  --zookeeper 为zk服务列表
     >
     >  --create 命令后 --topic 为创建topic 并指定 topic name
     >
     >  --partitions 为指定分区数量
     >
     >  --replication-factor 为指定副本集数量
     
  2. **查看topic列表命令**：
  
     ```shell
     kafka-topics --zookeeper localhost:2181 --list
     ```
  
  3. kafka命令发送数据：（然后我们就可以编写数据发送出去了）
  
     ```shell
     kafka-console-producer --broker-list localhost:9092 --topic tuyrk-kafka-topic
     ```
  
  4. kafka命令接受数据：（然后我们就可以看到消费的信息了）
  
     ```shell
     kafka-console-consumer --bootstrap-server localhost:9092 --topic tuyrk-kafka-topic --from-beginning
     ```
  
  5. 删除topic命令：
  
     ```shell
     kafka-topics.sh --zookeeper localhost:2181 --delete --topic tuyrk-kafka-topic
     ```
  
  6. kafka查看消费进度：（当我们需要查看一个消费者组的消费进度时，则使用下面的命令）
  
     ```shell
     kafka-consumer-groups --bootstrap-server localhost:9092 --describe --group tuyrk-kafka-group
     kafka-topics --zookeeper localhost:2181 --describe --topic tuyrk-kafka-topic
     ```
     
     > `--describe --group` 为订阅组， 后面指定 group name

### 急速入门

下面我们一起使用kafka最基本的API来对kafka进行操作！

- kafka依赖包：

  ```xml
  <dependency>
      <groupId>org.springframework.kafka</groupId>
      <artifactId>spring-kafka</artifactId>
  </dependency>
  ```

- kafka生产者：

  ```java
  package com.tuyrk.kafka.introduce;
  import com.alibaba.fastjson.JSON;
  import lombok.extern.slf4j.Slf4j;
  import org.apache.kafka.clients.producer.KafkaProducer;
  import org.apache.kafka.clients.producer.ProducerRecord;
  import org.apache.kafka.common.serialization.StringSerializer;
  import java.util.Properties;
  
  @Slf4j
  public class CollectKafkaProducer {
      // 创建一个kafka生产者
      private final KafkaProducer<String, String> producer;
      // 定义一个成员变量为topic
      private final String topic;
  
      // 初始化kafka的配置文件和实例：Properties & KafkaProducer
      public CollectKafkaProducer(String topic) {
          Properties props = new Properties();
          // 配置broker地址
          props.put("bootstrap.servers", "192.168.11.51:9092");
          // 定义一个 client.id
          props.put("client.id", "demo-producer-test");
  
          /// 其他配置项：
          /*props.put("batch.size", 16384);            //16KB -> 满足16KB发送批量消息
          props.put("linger.ms", 10);            //10ms -> 满足10ms时间间隔发送批量消息
          props.put("buffer.memory", 33554432);     //32M -> 缓存提性能*/
  
          // kafka 序列化配置：
          props.put("key.serializer", StringSerializer.class.getName());
          props.put("value.serializer", StringSerializer.class.getName());
  
          // 创建 KafkaProducer 与 接收 topic
          this.producer = new KafkaProducer<>(props);
          this.topic = topic;
      }
  
      // 发送消息 （同步或者异步）
      public void send(Object message, boolean syncSend) throws InterruptedException {
          try {
              // 同步发送
              if (syncSend) {
                  producer.send(new ProducerRecord<>(topic, JSON.toJSONString(message)));
              }
              // 异步发送（callback实现回调监听）
              else {
                  producer.send(new ProducerRecord<>(topic, JSON.toJSONString(message)),
                          (recordMetadata, e) -> {
                              if (e != null) {
                                  log.error("Unable to write to Kafka in CollectKafkaProducer [{}] exception: {}", topic, e);
                              }
                          });
              }
          } catch (Exception e) {}
      }
  
      // 关闭producer
      public void close() {
          producer.close();
      }
  
      // 测试函数
      public static void main(String[] args) throws InterruptedException {
          String topic = "tuyrk-kafka-topic";
          CollectKafkaProducer collectKafkaProducer = new CollectKafkaProducer(topic);
          for (int i = 0; i < 10; i++) {
              User user = new User();
              user.setId(i + "");
              user.setName("张三");
              collectKafkaProducer.send(user, true);
          }
          Thread.sleep(Integer.MAX_VALUE);
      }
  }
  ```
  
- kafka消费者：

  ```java
  package com.tuyrk.kafka.introduce;
  import lombok.extern.slf4j.Slf4j;
  import org.apache.kafka.clients.consumer.*;
  import org.apache.kafka.common.TopicPartition;
  import org.apache.kafka.common.serialization.StringDeserializer;
  import java.util.Collections;
  import java.util.List;
  import java.util.Properties;
  
  @Slf4j
  public class CollectKafkaConsumer {
      // 定义消费者实例
      private final KafkaConsumer<String, String> consumer;
      // 定义消费主题
      private final String topic;
  
      // 消费者初始化
      public CollectKafkaConsumer(String topic) {
          Properties props = new Properties();
          // 消费者的zookeeper 地址配置
          props.put("zookeeper.connect", "192.168.11.111:2181");
          // 消费者的broker 地址配置
          props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.11.51:9092");
          // 消费者组定义
          props.put(ConsumerConfig.GROUP_ID_CONFIG, "demo-group-id");
          // 是否自动提交（auto commit，一般生产环境均设置为false，则为手工确认）
          props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
          // 自动提交配置项
          // props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
          // 消费进度（位置 offset）重要设置: latest,earliest
          props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
          // 超时时间配置
          props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
          // kafka序列化配置
          props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
          props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
  
          // 创建consumer对象 & 赋值topic
          consumer = new KafkaConsumer<>(props);
          this.topic = topic;
          // 订阅消费主题
          consumer.subscribe(Collections.singletonList(topic));
  
      }
  
      // 循环拉取消息并进行消费，手工ACK方式
      private void receive(KafkaConsumer<String, String> consumer) {
          while (true) {
              // 	拉取结果集(拉取超时时间为1秒)
              ConsumerRecords<String, String> records = consumer.poll(1000);
              //  拉取结果集后获取具体消息的主题名称 分区位置 消息数量
              for (TopicPartition partition : records.partitions()) {
                  List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
                  String topic = partition.topic();
                  int size = partitionRecords.size();
                  log.info("获取topic:{},分区位置:{},消息数为:{}", topic, partition.partition(), size);
                  // 分别对每个partition进行处理
                  for (ConsumerRecord<String, String> partitionRecord : partitionRecords) {
                      log.error("-----> value: {}", partitionRecord.value());
                      long offset = partitionRecord.offset() + 1;
                      // consumer.commitSync(); // 这种提交会自动获取partition 和 offset
                      // 这种是显示提交partition 和 offset 进度
                      consumer.commitSync(Collections.singletonMap(partition,
                              new OffsetAndMetadata(offset)));
                      log.info("同步成功, topic: {}, 提交的 offset: {} ", topic, offset);
                  }
              }
          }
      }
  
      // 测试函数
      public static void main(String[] args) {
          String topic = "tuyrk-kafka-topic";
          CollectKafkaConsumer collectKafkaConsumer = new CollectKafkaConsumer(topic);
          collectKafkaConsumer.receive(collectKafkaConsumer.consumer);
      }
  }
  ```

