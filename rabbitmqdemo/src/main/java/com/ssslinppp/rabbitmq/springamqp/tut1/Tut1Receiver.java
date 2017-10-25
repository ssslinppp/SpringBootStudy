package com.ssslinppp.rabbitmq.springamqp.tut1;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/25 <br/>
 * Time: 16:24 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RabbitListener(queues = "hello")
public class Tut1Receiver {

    @RabbitHandler
    public void receive(String in) {
        System.out.println(" [Rev] Received '" + in + "'");
    }

}
