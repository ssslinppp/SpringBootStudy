package com.ssslinppp.rabbitmq.java.demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/25 <br/>
 * Time: 14:54 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class Send {
    private final static String QUEUE_NAME = "hello-demo1";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        //  create a connection to the server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建一个queue，并发送message
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        // 关闭
        channel.close();
        connection.close();
    }
}
