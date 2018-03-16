package com.ssslinppp.service;

import com.rabbitmq.client.Channel;
import com.ssslinppp.rabbit.model.ReceivedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
public class CustomErrWarnLog {

    /**
     * 消费端消息的 Manual ACK
     *
     * @param msg
     * @param channel
     * @param deliveryTag
     * @throws IOException
     */
    @RabbitListener(queues = "#{queueOfErrWarn.name}")
    public void receiveWithManualACK(ReceivedMessage msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag)
            throws Exception {
        /// 设置prefetch：用于控制流量，避免消费端负载过高
        /// 当未确认Message个数达到prefetch count时，broker将不再向channel中发送消息
        channel.basicQos(50, false);  // Per consumer limit
        channel.basicQos(100, true); // Per channel limit

        try {
            handleMsg(msg, deliveryTag);
        } catch (Exception e) {
            System.out.println("tag: 【" + deliveryTag + "】 fail, requeue,  msg: " + msg);

            ////// 当消息处理失败后，重新入队列，传给下一个消费者
            channel.basicNack(deliveryTag, false, true);
//            throw new Exception(e);
            return;
        }
        channel.basicAck(deliveryTag, false);  // 消息应答
    }

    private void handleMsg(ReceivedMessage msg, long deliveryTag) throws InterruptedException, IOException {
        System.out.println("DeliveryTag [" + deliveryTag + "], Deal [Err/Warn] log: " + msg);
        Random random = new Random();
        if (random.nextBoolean()) {
            throw new RuntimeException("handle msg fail");
        }

    }
}
