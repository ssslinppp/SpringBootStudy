package com.ssslinppp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FailRetryApplicationTests {

    @Autowired
    private MyFailService myFailService;

    @Test
    public void contextLoads() {
        myFailService.query(12);
    }

}
