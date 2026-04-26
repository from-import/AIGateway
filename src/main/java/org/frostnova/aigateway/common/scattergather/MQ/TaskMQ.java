package org.frostnova.aigateway.common.scattergather.MQ;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Slf4j
@Component
public class TaskMQ {

    /**
     * 生产数据：支持任意泛型和延迟时间
     *
     * @param topic     队列/主题名称
     * @param payload   消息体 (泛型 T)
     * @param delayedMs 延迟执行时间(毫秒)，0 表示立即执行
     */
    public <T> void produce(String topic, T payload, long delayedMs) {
        if (delayedMs > 0) {
            log.info("【MQ 发送延迟消息】Topic: {}, Delay: {}ms, Data: {}", topic, delayedMs, payload);
            // TODO: 调用具体 MQ 的延迟发送 API (如 RabbitMQ delayed exchange)
        } else {
            log.info("【MQ 发送实时消息】Topic: {}, Data: {}", topic, payload);
            // TODO: 调用具体 MQ 的实时发送 API
        }
    }

    /**
     * 消费数据：模拟注册消费者回调
     *
     * @param topic     队列/主题名称
     * @param clazz     反序列化的目标类型
     * @param processor 具体的消费逻辑函数
     */
    public <T> void consume(String topic, Class<T> clazz, Consumer<T> processor) {
        log.info("【MQ 注册消费者】监听 Topic: {}", topic);
        // TODO: @RabbitListener 或 KafkaListener 替代
        // 这里提供泛型设计是为了展示如果做编程式消费该如何抽象
    }
}
