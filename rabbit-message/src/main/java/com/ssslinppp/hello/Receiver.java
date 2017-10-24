package com.ssslinppp.hello;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/24 <br/>
 * Time: 15:31 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Component
public class Receiver {
    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
