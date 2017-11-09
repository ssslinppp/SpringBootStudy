package com.ssslinppp.map;

import com.ssslinppp.PropertiesReaderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/9 <br/>
 * Time: 17:57 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = PropertiesReaderApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MapPropertiesConfigTest {

    @Autowired
    private MapStringPropertiesConfig mapStringPropertiesConfig;

    @Autowired
    private MapPOJOPropertiesConfig mapPOJOPropertiesConfig;

    @Test
    public void testMapStringConfig() {
        System.out.println("#######################");
        System.out.println(mapStringPropertiesConfig.getCity());
        System.out.println("#######################");
    }

    @Test
    public void testMapPojoConfig() {
        System.out.println("#######################");
        System.out.println(mapPOJOPropertiesConfig.getCity());
        System.out.println("#######################");
    }

}