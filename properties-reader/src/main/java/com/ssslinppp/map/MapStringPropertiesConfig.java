package com.ssslinppp.map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/9 <br/>
 * Time: 17:52 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "map.all")
public class MapStringPropertiesConfig {
    private final Map<String, String> city = new HashMap<>();
}
