package com.ssslinppp.service;

import com.ssslinppp.rabbit.model.SendedMessage;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 1. 使用TopExchange；
 * 2. routringKey：4种不同的routingKey，分别表示日志的4种级别；
 */
@Component
public class RabbitSender {
    private static final List<String> LOG_ROUTING_KEY_LIST = new ArrayList<>();

    static {
        LOG_ROUTING_KEY_LIST.add("log.err.msg");
        LOG_ROUTING_KEY_LIST.add("log.warn.msg");
        LOG_ROUTING_KEY_LIST.add("log.info.msg");
        LOG_ROUTING_KEY_LIST.add("log.debug.msg");
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private TopicExchange topicExchange;

    @Scheduled(fixedDelay = 1_000, initialDelay = 2_000)
    public void sendMsg() throws InterruptedException {
        int index = new Random().nextInt(4);
        SendedMessage message = null;
        switch (index) {
            case 0:
                message = SendedMessage.getDemoMessage("log-err-err-err");
                break;
            case 1:
                message = SendedMessage.getDemoMessage("log-warn-warn-warn");
                break;
            case 2:
                message = SendedMessage.getDemoMessage("log-info-info-info");
                break;
            case 3:
                message = SendedMessage.getDemoMessage("log-debug-debug-debug");
                break;
            default:
                message = SendedMessage.getDemoMessage("log-err-err-err");
        }

        //////  发送message: 有Queue接收
        rabbitTemplate.convertAndSend(topicExchange.getName(), LOG_ROUTING_KEY_LIST.get(index), message,
                new CorrelationData(message.getId() + ""));
        System.out.println("send [" + message + "] to with routingKey: " + LOG_ROUTING_KEY_LIST.get(index));

        ////////// 发送Message，没有任何的Queue接收，会触发：returnCallback
        String msgWithoutQueueRouted = "Message(without any queue routed)";
        String routingKeyWithoutQueueRouted = "routingKey_no_queue_bind";
        rabbitTemplate.convertAndSend(topicExchange.getName(), routingKeyWithoutQueueRouted, msgWithoutQueueRouted,
                new CorrelationData(new Random().nextInt() + ""));
        System.out.println("send [" + msgWithoutQueueRouted + "] to with routingKey: " + routingKeyWithoutQueueRouted);
    }

}
