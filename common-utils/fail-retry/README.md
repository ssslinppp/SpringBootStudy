# 概述
此模块用于`方法的失败重试`，使用`annotation`+`AOP`方式实现；   

# 详述

示例：
```
@Service
public class MyFailService {

    //    @FailRetry(handleExceptionMethod = "doException", beforeExceptionalReturn = "allRetryFail")
    @FailRetry
    public String query(int id) {
        System.out.println("### queryId: " + id);
        throw new RuntimeException("### query exception....");
    }

    public void doException(String methodName, Object[] params, Exception e) {
        String msg = String.format("自定义的异常处理,方法名：%s ，参数列表: %s , 异常： %s", methodName, Arrays.asList(params), e.getMessage());
        System.out.println(msg);
    }

    public void allRetryFail(String methodName, Object[] params) {
        String msg = String.format("所有的失败重试都已结束,方法名：%s ，参数列表: %s %s", methodName, Arrays.asList(params));
        System.out.println(msg);
    }
}
```