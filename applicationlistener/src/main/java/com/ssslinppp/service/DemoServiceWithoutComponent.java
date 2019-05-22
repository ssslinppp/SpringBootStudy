package com.ssslinppp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description：该类没有{@link Component @Component} 注解，通过ApplicationPreparedListener进行初始化<br/>
 */
public class DemoServiceWithoutComponent {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceWithoutComponent.class);

    @PostConstruct
    private void init() {
        logger.info("初始化：DemoServiceWithoutComponent");
    }

    public void print(String msg) {
        logger.info("DemoServiceWithoutComponent print：" + msg);
    }

}
