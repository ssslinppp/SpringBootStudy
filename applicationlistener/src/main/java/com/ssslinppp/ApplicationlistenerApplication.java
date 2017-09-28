package com.ssslinppp;

import com.ssslinppp.listeners.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationlistenerApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ApplicationlistenerApplication.class);
        springApplication.addListeners(new ApplicationPreparedListener());
        springApplication.addListeners(new ApplicationFailedListener());
        springApplication.addListeners(new ApplicationReadyListener());
        springApplication.addListeners(new ApplicationStartingListener());
        springApplication.addListeners(new SpringApplicationListener());
        springApplication.addListeners(new ApplicationEnvironmentPreparedListener());
        springApplication.run(args);
    }
}
