# 概述
此模块用于统计函数运行时间，使用`annotation`+`AOP`方式实现；   

# 详述
声明中包括两个参数：
```
// 当超过期望值时，就会打印warn日志，否则打印info日志
long expectMaxMillseconds() default 5_000;

// 日志中是否打印方法参数
boolean needPrintParams() default false;
```

示例：
```
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
```
