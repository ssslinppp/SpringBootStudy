package com.ssslinppp.rabbit.model;

import lombok.Data;

import java.util.Date;
import java.util.Random;

/**
 * Desc:
 * 1. 实现Serializable接口；
 * 2. 提供默认构造器：
 * <p>
 */
@Data
public class SendedMessage {
    private int id;
    private String logContext;
    private Date time;

    public static SendedMessage getDemoMessage(String logContext) {
        SendedMessage message = new SendedMessage();
        message.setId(new Random().nextInt(50));
        message.setLogContext(logContext);
        message.setTime(new Date());
        return message;
    }
}
