package com.ssslinppp.aop;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeElapsedAopTest {
    @Autowired
    private MyService myService;

    @Test
    public void timeElapsedAround() throws Exception {
        Map<String, String> map = Maps.newHashMap();
        map.put("AAA", "1111");
        map.put("BBB", "2222");
        map.put("CCC", "3333");
        System.out.println("### finish: " + myService.print("mmmmmsssssgggg", map));

        System.out.println();

        myService.print();
    }

}











