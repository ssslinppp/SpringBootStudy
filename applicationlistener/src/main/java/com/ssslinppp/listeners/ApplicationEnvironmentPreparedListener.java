package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Iterator;

/**
 * ApplicationEnvironmentPreparedEvent：环境事先准备，spring boot中的环境已经准备ok
 * 可以通过ApplicationEnvironmentPreparedEvent获取到SpringApplication、ConfigurableEnvironment等等信息，
 * 可以通过ConfigurableEnvironment实例对象来修改以及获取默认的环境信息。
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationEnvironmentPreparedListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationEnvironmentPreparedListener.class);


    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        logger.info("监听器事件 【ApplicationEnvironmentPreparedEvent】");
        SpringApplication springApplication = event.getSpringApplication();
        ConfigurableEnvironment environment = event.getEnvironment();
        Object source = event.getSource();
        logger.info("### " + springApplication);
        logger.info("### " + environment);
        logger.info("### " + source);

        MutablePropertySources propertySources = environment.getPropertySources();
        if (propertySources != null) {
            Iterator<PropertySource<?>> iterator = propertySources.iterator();
            while (iterator.hasNext()) {
                PropertySource<?> propertySource = (PropertySource<?>) iterator.next();

                System.out.println("### :propertySource" + propertySource);
            }
        }
    }
}
