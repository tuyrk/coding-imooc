package com.tuyrk.kafka.introduce;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * 生产者
 *
 * @author tuyrk
 */
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
        props.put("bootstrap.servers", "localhost:9092");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            User user = new User(i + "", "张三");
            collectKafkaProducer.send(user, true);
        }
        Thread.sleep(Integer.MAX_VALUE);
    }
}
