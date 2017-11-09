package com.ssslinppp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PropertiesReaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertiesReaderApplication.class, args);
    }
}
