package com.ssslinppp.map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "map.all")
public class MapStringPropertiesConfig {
    private final Map<String, String> city = new HashMap<>();
}
