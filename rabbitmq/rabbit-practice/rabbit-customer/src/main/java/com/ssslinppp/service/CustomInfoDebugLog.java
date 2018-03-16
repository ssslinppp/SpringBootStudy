package com.ssslinppp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssslinppp.rabbit.model.ReceivedMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomInfoDebugLog {
    /**
     * 当设置为 `手动应答`时，该方法并没有对消息进行手动应答（channel.basicAck等），将会导致broker不再向该channel中发送消息（假设未应答的消息个数已经达到prefetch）；
     * 关于manual ack，可以参考 {@link CustomErrWarnLog}的实现
     *
     * @param msg
     * @throws InterruptedException
     * @throws IOException
     */
    @RabbitListener(queues = "#{queueOfInfoDebug.name}")
    public void receiveWithoutManualACK(Message msg) throws InterruptedException, IOException {
        handleMsg(msg);
        //手动应答：当设置消费端为 manual ack时，需要在处理完消息后进行 应答
    }

    /**
     * 直接获取Message中的Json字符串，然后使用第三方库进行json转换
     *
     * @param msg
     * @throws InterruptedException
     * @throws IOException
     */
    private void handleMsg(Message msg) throws InterruptedException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReceivedMessage message = objectMapper.readValue(msg.getBody(), ReceivedMessage.class);
        System.out.println("Deal [info/debug] log: " + message);
    }
}
