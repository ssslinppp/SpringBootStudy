package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;

/**
 */
public class ApplicationFailedListener implements ApplicationListener<ApplicationFailedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationFailedListener.class);

    @Override
    public void onApplicationEvent(ApplicationFailedEvent applicationFailedEvent) {
        logger.info("监听器事件 【ApplicationFailedEvent】");
//        Throwable e = applicationFailedEvent.getException();
//        logger.info("Springboot启动异常:" + e.getMessage());
    }
}
