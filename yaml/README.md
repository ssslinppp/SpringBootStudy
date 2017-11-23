# Springboot中使用yaml配置文件
### 使用YAML代替Properties 
YAML是JSON的一个超集，也是一种方便的定义层次配置数据的格式。   
无论何时将`SnakeYAML`库放到classpath下，SpringApplication类都会自动支持YAML作为properties的替换。        
`注`：如果使用'starter POMs'，spring-boot-starter会自动提供SnakeYAML。

### 相关注解
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
对应的Java POJO

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




















