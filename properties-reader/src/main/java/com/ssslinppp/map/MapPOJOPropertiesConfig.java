package com.ssslinppp.map;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/9 <br/>
 * Time: 17:59 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "map.pojo")
public class MapPOJOPropertiesConfig {
    private final Map<String, City> city = new HashMap<>();

    @AllArgsConstructor
    @ToString
    public enum Country {
        CHINA("chinese"),
        USA("American");
        private String desc;
    }

    @Data
    public static class City {
        private String name;
        private String desc;
        private boolean isChina;
        private Country country;

        /**
         * 特别注意：
         * 当boolean类型的变量名称为：isXXXX, @Setter会将setter方法设置为 setXXXX, 此时配置文件使用isStudent进行配置时，是注入不进来的；
         * 需要单独声明为 setIsXXXX()
         *
         * @param china
         */
        public void setIsChina(boolean china) {
            isChina = china;
        }
    }
}
