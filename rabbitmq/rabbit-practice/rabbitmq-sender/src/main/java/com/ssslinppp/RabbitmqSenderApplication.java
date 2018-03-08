package com.ssslinppp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RabbitmqSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqSenderApplication.class, args);
    }
}
