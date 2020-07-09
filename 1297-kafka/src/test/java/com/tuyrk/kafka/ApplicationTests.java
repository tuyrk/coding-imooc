package com.tuyrk.kafka;

import com.tuyrk.kafka.introduce.User;
import com.tuyrk.kafka.producer.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;


@SpringBootTest
class ApplicationTests {

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
}
