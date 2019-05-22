package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

/**
 * ApplicationStartedEvent：spring boot 启动监听类。
 * <p>
 * 可以在SpringApplication启动之前做一些手脚，比如修改SpringApplication实例对象中的属性值
 */
public class ApplicationStartingListener implements ApplicationListener<ApplicationStartingEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartingListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartingEvent applicationStartingEvent) {
        logger.info("监听器事件 【ApplicationStartingEvent】");
    }
}
