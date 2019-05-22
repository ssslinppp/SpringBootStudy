package com.ssslinppp.rabbit.model;

import lombok.Data;

import java.util.Date;

@Data
public class ReceivedMessage {
    private int id;
    private String logContext;
    private Date time;
}
