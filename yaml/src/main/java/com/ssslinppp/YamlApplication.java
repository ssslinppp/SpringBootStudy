package com.ssslinppp;

import com.ssslinppp.collection.ListConfig;
import com.ssslinppp.collection.MapConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class YamlApplication implements CommandLineRunner {
    @Autowired
    private ListConfig listConfig;

    @Autowired
    private MapConfig mapConfig;

    public static void main(String[] args) {
        SpringApplication.run(YamlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("服务器列表List<String>：" + listConfig.getServers());
        
        System.out.println("personMap[ Map<String,Person> ]: " + mapConfig.getPersons());
    }
}
