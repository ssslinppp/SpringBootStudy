package com.ssslinppp;

import com.ssslinppp.failRetry.annotation.FailRetry;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2017/12/20 , Time: 11:31 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MyFailService {

    //    @FailRetry(handleExceptionMethod = "doException", beforeExceptionalReturn = "allRetryFail", interval = 5_000)
//    @FailRetry(interval = 3_000)
    @FailRetry
    public String query(int id) {
        System.out.println("### queryId: " + id);
        if (new Random().nextInt() % 3 != 0) {
            throw new RuntimeException("### query exception....");
        }
        System.out.println("### queryId success: " + id);
        return null;
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
