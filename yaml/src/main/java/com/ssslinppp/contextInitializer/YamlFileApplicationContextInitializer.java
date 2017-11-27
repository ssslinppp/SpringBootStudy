package com.ssslinppp.contextInitializer;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class YamlFileApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
//            Resource resource = applicationContext.getResource("classpath:/config/myConfig.yml"); //方式1
            Resource resource = new ClassPathResource("/config/myConfig.yml");  //方式2

            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            PropertySource<?> yamlTestProperties = sourceLoader.load("yaml", resource, null);

            //添加到环境变量中
            applicationContext.getEnvironment().getPropertySources().addFirst(yamlTestProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
