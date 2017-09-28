package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * SpringApplicationEvent：获取SpringApplication
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/9/28 <br/>
 * Time: 16:35 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class SpringApplicationListener implements ApplicationListener<SpringApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SpringApplicationListener.class);

    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent) {
        logger.info("监听器事件 【SpringApplicationEvent】");
    }
}
