package com.ssslinppp.utils;

import org.springframework.context.ApplicationListener;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;

/**
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
