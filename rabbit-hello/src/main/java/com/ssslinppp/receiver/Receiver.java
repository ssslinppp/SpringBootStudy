package com.ssslinppp.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: liulin
 * Date: 2017/7/2
 * Time: 18:30
 * Description：对应的测试类,SenderTest.java
 * To change this template use File | Settings | File Templates.
 */
@Component
@RabbitListener(queues = "hello")
public class Receiver {
    @RabbitHandler
    public void process(String hello) {
        System.out.println("Receiver : " + hello);
    }
}













