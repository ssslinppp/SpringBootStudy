package com.ssslinppp.utils;

import org.springframework.context.ApplicationListener;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/9/28 <br/>
 * Time: 17:20 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class SpringFactoriesLoaderTest {
    public static void main(String[] args) throws IOException {
        List<ApplicationListener> loadFactories = SpringFactoriesLoader.loadFactories(ApplicationListener.class, null);
        System.out.println(loadFactories.size());
        for (ApplicationListener applicationListener : loadFactories) {
            System.out.println(applicationListener.getClass().getName());
        }
    }
}
