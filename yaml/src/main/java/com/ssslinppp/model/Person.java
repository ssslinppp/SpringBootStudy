package com.ssslinppp.model;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Person {
    private String name;
    private int age;
    private Sex sex;
    private double salary;

    /**
     * Lombok会将setter函数设置为：setOld()或setold(),此时yaml配置文件应该使用Old或old进行配置
     */
    private boolean isOld;

    private List<String> address; //演示了3种不同的yaml配置方式
    private Person father; //TODO 通过引用的方式（未完成）
    private List<Book> books;

    private Map<String, String> maps;

    /**
     * 使用自定义的类型转换器进行转换：StringDateConverter.java
     * http://www.logicbig.com/tutorials/spring-framework/spring-boot/custom-configuration-properties-binding/
     */
    private Date birthDay;

    @Getter
    @ToString
    public enum Sex {
        man("Good Man"),
        woman("Good Woman");

        private String desc;

        Sex(String desc) {
            this.desc = desc;
        }
    }
}




