# 概述
读取配置文件中的内容，包括List/Map/Tree等；

# List 配置文件
支持：
```
POJO：String, int, enum, boolean等类型
```

需要添加：
```
@ConfigurationProperties(prefix = "list.all")
@EnableConfigurationProperties
```

配置示例：
```properties
list.all.persons[0].name=Tom
list.all.persons[0].age=12
list.all.persons[0].sex=man
list.all.persons[0].isStudent=true
list.all.persons[1].name=July
list.all.persons[1].age=23
list.all.persons[1].sex=woman
list.all.persons[1].isStudent=true
list.all.persons[2].name=Hetty
list.all.persons[2].age=54
list.all.persons[2].sex=man
list.all.persons[2].isStudent=false
```
支持String/int/boolean/enum等类型的注入；   
代码见：ListPropertiesConfig.java    
Test见：ListPropertiesConfigTest.java   

# Map 配置文件
支持
```
Map<String, String>;
Map<String, POJO>: String,boolean,enum等
```

需要添加：
```
@ConfigurationProperties(prefix = "xxx.xxx")
@EnableConfigurationProperties
```

配置示例：
```properties
map.pojo.city.sz.name=Suzhou
map.pojo.city.sz.desc=This is suzhou
map.pojo.city.sz.isChina=true
map.pojo.city.sz.country=CHINA
map.pojo.city.newyork.name=New York
map.pojo.city.newyork.desc=This is New York
map.pojo.city.newyork.isChina=false
map.pojo.city.newyork.country=USA
```
