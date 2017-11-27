# Springboot中使用yaml配置文件
## 使用YAML代替Properties 
YAML是JSON的一个超集，也是一种方便的定义层次配置数据的格式。   
无论何时将`SnakeYAML`库放到classpath下，SpringApplication类都会自动支持YAML作为properties的替换。        
`注`：如果使用'starter POMs'，spring-boot-starter会自动提供SnakeYAML。

## 相关注解
添加注解：
```
@EnableConfigurationProperties 
@ConfigurationProperties(prefix = "my")  //可以指定配置文件的prefix
@Component 或 @Configuration //配置文件类需要添加该注解，两个都可以
```
无效注解：
```
// 特别说明：springboot中，yaml配置文件，不支持@PropertySource("classpath:list.yaml")注解
// 但是 properties 配置文件支持注解 @PropertySource("classpath:list.properties")
@PropertySource("classpath:list.yaml")
```

---

# 常用类型的配置
## List<String>
java
```
 private List<String> servers = new ArrayList<String>();
```
yaml
```
servers:
 - dev.bar.com
 - foo.bar.com
```

## List<POJO>
java
```
private List<Book> books;

@Data
public class Book {
    private String name;
    private String auther;
    private BookCategory category;

    public enum BookCategory {
        java,
        mysql;
    }
}
```
yaml:
```
books: # list<Book> books;
 - name: Java并发编程实战
   auther: AutherJava
   category: java
 - name: 深入理解Mysql
   auther: AutherMysql
   category: mysql
```
## Map<String, String>
java
```
private Map<String, String> maps;
```
yaml:
```
//注意：":"后面必须跟空格
maps: {key1: value1, key2: value2, key3: value3}  # Map<String, String>
```

## Map<String, POJO>
在此示例中，包括了日常编码中，大部分使用到的数据类型：
- String
- int
- double
- enum
- boolean
- List<String>
- POJO
- List<POJO>
- Map<String, String>
- Map<String, POJO>
- Date (自定义类型转换)

java
```
private Map<String, Person> persons = Maps.newHashMap();

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
```
yaml
```
# config for Map<String, Person>
personMap: # prefix
 persons: # POJO's field
  Tom: # Map's key
   name: Tom
   age: 12
   sex: man
   salary: 1000.25
   # 不要设置为isOld，因为Person的setter函数为：setOld()/setold(),如果要使用isOld，需要显示的声明 setIsOld
   old: false
   birthDay: 1995-10-11 12:45:15
   address: # list方式1，每个元素前面都添加“-”
    - Beijing
    - Nanjing
   books: # list<Book> books;
    - name: Java并发编程实战
      auther: AutherJava
      category: java
    - name: 深入理解Mysql
      auther: AutherMysql
      category: mysql
   maps: {key1: value1, key2: value2, key3: value3}  # Map<String, String>
  July: # Map's key
   name: July
   age: 15
   sex: woman
   salary: 120.25
   address: # list方式2，仅在第一个元素前面都添加“-”
    - Shanghai
      Nanjing
  Jame:  # Map's key
   name: jame
   age: 35
   sex: man
   Old: true
   address: Suzhou,Xuzhou # list方式3：元素以逗号分隔
   birthDay: 1975-7-8 08:45:15
```

## 自定义类型转换器
添加注解：
```
@Component
@ConfigurationPropertiesBinding
```
实现接口：
```
public class StringDateConverter implements Converter<S, T> {}
```
完整示例：
```java
/**
 * 转化yaml配置文件的String为Date；
 * yaml中： birthDay: 1995-10-11 12:45:15
 * 转化为POJO中的：private Date birthDay
 */
@Component
@ConfigurationPropertiesBinding
public class StringDateConverter implements Converter<String, Date> {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Date convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }

        try {
            return sdf.parse(source);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
```
---

## @ConfigurationProperties Validation
`@ConfigurationProperties` 支持 JSR-303 bean validation – javax.validation
示例：
```
package com.mkyong;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Component
@ConfigurationProperties
public class GlobalProperties {
    @Max(5)
    @Min(0)
    private int threadPool;

    @NotEmpty
    private String email;

    //getters and setters
}
```

## 自定义yaml源
springboot中，yaml文件不支持注解：`@PropertySource("classpath:xxx.yaml")`，默认的yaml文件为：`application.yml`;    
如果想要配置使用其他yaml文件，一种方式是：在所有bean加载之前定义好yaml源，并将yaml文件属性配置到环境变量中，代码如下：
```java
@Configuration
public class YamlFileApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
//            Resource resource = applicationContext.getResource("classpath:/config/myConfig.yml"); //方式1
            Resource resource = new ClassPathResource("/config/myConfig.yml");  //方式2

            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            PropertySource<?> yamlTestProperties = sourceLoader.load("yaml", resource, null);

            //添加到环境变量中
            applicationContext.getEnvironment().getPropertySources().addFirst(yamlTestProperties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
参考：    
[【ApplicationContextInitializer】springboot如何在所有bean初始化之前加载一个自定义类](https://ssslinppp.github.io/2017/11/27/%E3%80%90ApplicationContextInitializer%E3%80%91springboot%E5%A6%82%E4%BD%95%E5%9C%A8%E6%89%80%E6%9C%89bean%E5%88%9D%E5%A7%8B%E5%8C%96%E4%B9%8B%E5%89%8D%E5%8A%A0%E8%BD%BD%E4%B8%80%E4%B8%AA%E8%87%AA%E5%AE%9A%E4%B9%89%E7%B1%BB/)



# 参考
https://en.wikipedia.org/wiki/YAML   
http://www.yaml.org/spec/1.2/spec.html  
http://www.ruanyifeng.com/blog/2016/07/yaml.html
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html  


