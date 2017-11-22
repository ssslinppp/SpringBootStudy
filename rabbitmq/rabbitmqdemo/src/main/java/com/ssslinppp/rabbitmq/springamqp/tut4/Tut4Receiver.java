package com.ssslinppp.rabbitmq.springamqp.tut4;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.util.StopWatch;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/10/26 <br/>
 * Time: 17:02 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("Duplicates")
public class Tut4Receiver {
    @RabbitListener(queues = "#{autoDeleteQueue1.name}")
    public void receive1(String in) throws InterruptedException {
        receive(in, "orange-black");
    }

    @RabbitListener(queues = "#{autoDeleteQueue2.name}")
    public void receive2(String in) throws InterruptedException {
        receive(in, "green-black");
    }

    public void receive(String in, String receiver) throws InterruptedException {
        StopWatch watch = new StopWatch();
        watch.start();
        System.out.println("instance[ " + receiver + " ] Received '" + in + "'");
        doWork(in);
        watch.stop();
        System.out.println("instance[ " + receiver + " ] Done in " + watch.getTotalTimeSeconds() + "s");
    }

    private void doWork(String in) throws InterruptedException {
        for (char ch : in.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
