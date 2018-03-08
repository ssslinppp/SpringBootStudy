package com.ssslinppp.service;

import com.ssslinppp.rabbit.model.ReceivedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *
 */
@Component
public class CustomErrWarnLog {

    @RabbitListener(queues = "#{queueOfErrWarn.name}")
    public void receive(ReceivedMessage msg) throws InterruptedException, IOException {
        handleMsg(msg);
    }

    private void handleMsg(ReceivedMessage msg) throws InterruptedException, IOException {
        System.out.println("Deal [Err/Warn] log: " + msg);
    }
}
