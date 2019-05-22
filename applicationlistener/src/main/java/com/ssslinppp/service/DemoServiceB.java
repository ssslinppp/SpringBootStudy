package com.ssslinppp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 */
@Component
public class DemoServiceB {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceB.class);

    @PostConstruct
    private void init() {
        logger.info("初始化实例：DemoServiceB");
    }

}
