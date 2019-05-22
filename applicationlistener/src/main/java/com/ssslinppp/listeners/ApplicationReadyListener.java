package com.ssslinppp.listeners;

import com.ssslinppp.service.DemoServiceWithoutComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * ApplicationReadyEvent：上下文已经准备ok。
 * 这个时候就可以通过ApplicationReadyEvent获取ConfigurableApplicationContext，然后通过ConfigurableApplicationContext 获取bean的信息。
 */
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationReadyListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("监听器事件 【ApplicationReadyEvent】 ： ApplicationReadyListener");
//        ConfigurableApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();
//        logger.info("开始注入bean：demoServiceWithoutComponent");
//        registerBean(applicationContext, "demoServiceWithoutComponent", new DemoServiceWithoutComponent());
//        logger.info("完成注入bean：demoServiceWithoutComponent");
//
//        DemoServiceWithoutComponent demoServiceWithoutComponent = (DemoServiceWithoutComponent) applicationContext.getBean
//                ("demoServiceWithoutComponent");
//        demoServiceWithoutComponent.print("AAAAAAAAA");
    }

    /**
     * 将对象注入到Spring应用上下文
     *
     * @param name
     * @param obj
     */
    public void registerBean(ApplicationContext ctx, String name, Object obj) {
        // 获取BeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ctx
                .getAutowireCapableBeanFactory();

        // 动态注册bean.
        defaultListableBeanFactory.registerSingleton(name, obj);
    }
}
