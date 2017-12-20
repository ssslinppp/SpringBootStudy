package com.ssslinppp.failRetry.aop;

import com.ssslinppp.failRetry.annotation.FailRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Desc: 注解 {@link FailRetry} 中方法扩展点的默认实现
 * <p>
 * User: liulin ,Date: 2017/12/20 , Time: 10:18 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
public abstract class FailRetrySupport {
    private static final Logger logger = LoggerFactory.getLogger(FailRetrySupport.class);

    /**
     * 是否需要重试
     *
     * @return
     */
    protected boolean shouldRetry() {
        return true;
    }

    protected void handleException(String methodName, Object[] params, Exception e) {
        String msg = String.format("FailRetrySupport - {methodName: %s, params: %s ，Exception: %s",
                methodName, Arrays.asList(params), e.getMessage());
//        logger.error(msg, e);
        logger.error(msg);
    }

    protected void beforeExceptionalReturn(String methodName, Object[] params) {
        logger.warn("FailRetrySupport - all fail retry have finished, [method: {} , params: {}]", methodName, Arrays.asList(params));
    }
}
