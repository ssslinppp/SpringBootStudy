package com.ssslinppp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Description：<br/>
 * User: liulin <br/>
 * Date: 2017/11/8 <br/>
 * Time: 15:22 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public class User implements Serializable {
    private String name;
    private int age;
    private String address;

    public User(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public static User getDemo() {
        List<String> names = new ArrayList<>();
        names.add("Tom");
        names.add("Judil");
        names.add("Tulesr");
        names.add("Judidd");
        List<String> addresses = new ArrayList<>();
        addresses.add("Beijing");
        addresses.add("Shanghai");
        addresses.add("Suzhou");
        addresses.add("Xuzhou");
        int age = new Random().nextInt(100);
        String name = names.get(new Random().nextInt(3));
        String address = addresses.get(new Random().nextInt(3));
        return new User(name, age, address);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
