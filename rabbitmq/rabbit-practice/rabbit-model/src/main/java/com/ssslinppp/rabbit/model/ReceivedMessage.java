package com.ssslinppp.rabbit.model;

import lombok.Data;

import java.util.Date;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2018/3/8 , Time: 11:05 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Data
public class ReceivedMessage {
    private int id;
    private String logContext;
    private Date time;
}
