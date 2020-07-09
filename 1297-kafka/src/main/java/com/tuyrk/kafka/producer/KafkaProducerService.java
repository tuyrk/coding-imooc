package com.tuyrk.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;

/**
 * Kafka 生产者
 *
 * @author tuyrk
 */
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
