package com.ssslinppp.collection;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "my")
//@PropertySource("classpath:list.yaml") // 不起作用
@Component
public class ListConfig {
    private List<String> servers = new ArrayList<String>();
}
