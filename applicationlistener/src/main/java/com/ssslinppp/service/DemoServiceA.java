package com.ssslinppp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/9/28 <br/>
 * Time: 15:48 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DemoServiceA {
    private static final Logger logger = LoggerFactory.getLogger(DemoServiceA.class);

    @PostConstruct
    private void init() {
        logger.info("初始化实例：DemoServiceA");
    }

}
