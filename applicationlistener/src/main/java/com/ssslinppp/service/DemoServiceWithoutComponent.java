package com.ssslinppp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Description：该类没有{@link Component @Component} 注解，通过ApplicationPreparedListener进行初始化<br/>
 * User: liulin <br/>
 * Date: 2017/9/28 <br/>
 * Time: 15:50 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
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
