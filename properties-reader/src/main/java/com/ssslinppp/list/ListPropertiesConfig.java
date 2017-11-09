package com.ssslinppp.list;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/9 <br/>
 * Time: 17:22 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "list.all")
public class ListPropertiesConfig {
    private List<Person> persons;

    @AllArgsConstructor
    @Getter
    @ToString
    public enum SEX {
        man("This a man"),
        woman("This a woman");

        private String desc;

    }

    @Data
    public static class Person {
        private String name;
        private int age;
        private SEX sex;
        private boolean isStudent = true;  //默认值

        /**
         * 特别注意：
         * 当boolean类型的变量名称为：isXXXX, @Setter会将setter方法设置为 setXXXX, 此时配置文件使用isStudent进行配置时，是注入不进来的；
         * 需要单独声明为 setIsXXXX()
         *
         * @param student
         */
        public void setIsStudent(boolean student) {
            isStudent = student;
        }
    }
}
