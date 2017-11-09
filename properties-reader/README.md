# 概述
读取配置文件中的内容，包括List/Map/Tree等；

# List 配置文件
```
@ConfigurationProperties(prefix = "list.all")
@EnableConfigurationProperties
```
支持String/int/boolean/enum等类型的注入；   
代码见：ListPropertiesConfig.java    
Test见：ListPropertiesConfigTest.java   