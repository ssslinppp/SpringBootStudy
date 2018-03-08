package com.ssslinppp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssslinppp.rabbit.model.ReceivedMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 */
@Component
public class CustomInfoDebugLog {
    @RabbitListener(queues = "#{queueOfInfoDebug.name}")
    public void receive(Message msg) throws InterruptedException, IOException {
        handleMsg(msg);
    }

    private void handleMsg(Message msg) throws InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReceivedMessage message = objectMapper.readValue(msg.getBody(), ReceivedMessage.class);
        System.out.println("Deal [info/debug] log: " + message);
    }
}
