package com.ssslinppp.list;

import com.ssslinppp.PropertiesReaderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/9 <br/>
 * Time: 17:35 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PropertiesReaderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListPropertiesConfigTest {

    @Autowired
    private ListPropertiesConfig listPropertiesConfig;

    @Test
    public void testListConfig() {
        List<ListPropertiesConfig.Person> personList = listPropertiesConfig.getPersons();
        System.out.println("#######################");
        System.out.println(personList);
        System.out.println("#######################");
    }

}