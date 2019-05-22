package com.ssslinppp.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

/**
 * <pre>
 * 上下文context已经准备完毕 ，可以通过ApplicationPreparedEvent获取到ConfigurableApplicationContext实例对象。
 * ConfigurableApplicationContext类继承ApplicationContext类，
 * 但需要注意这个时候spring容器中的bean还没有被完全的加载，因此如果通过ConfigurableApplicationContext获取bean会报错的。
 * 比如报错：
 * Exception in thread "main" java.lang.IllegalStateException: org.springframework.boot.context.embedded
 * .AnnotationConfigEmbeddedWebApplicationContext@69b0fd6f has not been refreshed yet
 * </pre>
 * 获取到上下文之后，可以将其注入到其他类中，毕竟ConfigurableApplicationContext为引用类型
 */
public class ApplicationPreparedListener implements ApplicationListener<ApplicationPreparedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationPreparedListener.class);

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
        logger.info("监听器事件 【ApplicationPreparedEvent】");
    }

}
