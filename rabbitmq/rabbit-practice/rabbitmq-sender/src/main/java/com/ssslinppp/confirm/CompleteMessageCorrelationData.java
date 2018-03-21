package com.ssslinppp.confirm;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.support.CorrelationData;

/**
 * publisher confirm：需要使用到CorrelationData
 */
public class CompleteMessageCorrelationData extends CorrelationData {
    private final Message message;

    public CompleteMessageCorrelationData(String id, Message message) {
        super(id);
        this.message = message;
    }

    public Message getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return "CompleteMessageCorrelationData [id=" + getId() + ", message=" + this.message + "]";
    }

}
