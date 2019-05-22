package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * SpringApplicationEvent：获取SpringApplication
 */
public class SpringApplicationListener implements ApplicationListener<SpringApplicationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SpringApplicationListener.class);

    @Override
    public void onApplicationEvent(SpringApplicationEvent springApplicationEvent) {
        logger.info("监听器事件 【SpringApplicationEvent】");
    }
}
