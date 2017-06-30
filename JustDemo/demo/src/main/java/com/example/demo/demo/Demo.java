package com.example.demo.demo;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: liulin
 * Date: 2017/7/1
 * Time: 0:12
 * Description：
 * To change this template use File | Settings | File Templates.
 */
@Component
public class Demo {

    @PostConstruct
    public void printInfo(){
        System.out.println("This is just a demo");
    }

}
