package com.tuyrk.kafka.introduce;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * 消费者
 *
 * @author tuyrk
 */
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
        props.put("zookeeper.connect", "localhost:2181");
        // 消费者的broker 地址配置
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
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
        // String topic = "tuyrk-kafka-topic";
        String topic = "error-kafka-log-collect";
        CollectKafkaConsumer collectKafkaConsumer = new CollectKafkaConsumer(topic);
        collectKafkaConsumer.receive(collectKafkaConsumer.consumer);
    }
}
