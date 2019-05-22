package com.ssslinppp.aop;

import com.ssslinppp.annotation.TimeElapsed;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 */
@Component
public class MyService {

    @TimeElapsed(expectMaxMillseconds = 10, needPrintParams = true)
    public String print(String msg, Map<String, String> map) {
        System.out.println("MyService - printParamsMsg: " + msg);
        System.out.println("MyService - printParamsMap: " + map);
        return msg + "-Ok";
    }

    @TimeElapsed(needPrintParams = true)
    public void print() {
        System.out.println("MyService - print: Hello, myservie");

    }
}
