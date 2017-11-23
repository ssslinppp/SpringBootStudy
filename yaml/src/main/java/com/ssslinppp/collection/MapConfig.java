package com.ssslinppp.collection;

import com.google.common.collect.Maps;
import com.ssslinppp.model.Person;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "personMap")
@Configuration
public class MapConfig {
    private Map<String, Person> persons = Maps.newHashMap();

}
