package com.ssslinppp.failRetry.aop;

import com.google.common.collect.Lists;
import com.ssslinppp.failRetry.annotation.FailRetry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2017/12/20 , Time: 10:24 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Component
@Aspect
public class FailRetryAspect extends FailRetrySupport {

    private static final Logger logger = LoggerFactory.getLogger(FailRetryAspect.class);

    private static ThreadLocal<Integer> retryCounters;

    static {
        retryCounters = ThreadLocal.withInitial(() -> {
            return 0;
        });
    }

    @Around(value = "@annotation(failRetry)")
    public Object retryAround(ProceedingJoinPoint pjp, FailRetry failRetry) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Object target = pjp.getTarget();

        try {
            while (doShouldRetry(target, failRetry)) {
                try {
                    return pjp.proceed();
                } catch (Exception e) {
                    doHandleException(target, signature.toString(), pjp.getArgs(), failRetry, e);
                }
            }
        } finally {
            retryCounters.set(0);
        }

        doBeforeExceptionalReturn(target, signature.toString(), pjp.getArgs(), failRetry);

        return null;
    }


    /**
     * 异常处理
     *
     * @param target
     * @param methodName
     * @param targetParams
     * @param failRetry
     * @param e
     * @throws Throwable
     */
    private void doHandleException(Object target, String methodName, Object[] targetParams, FailRetry failRetry, Exception e)
            throws Throwable {
        if (StringUtils.isEmpty(failRetry.handleExceptionMethod())) {
            super.handleException(methodName, targetParams, e);
        } else {
            List<Object> params = Lists.newArrayList();
            Method excetionMethod = null;
            for (Method method : target.getClass().getDeclaredMethods()) {
                if (method.getName().equals(failRetry.handleExceptionMethod())) {
                    excetionMethod = method;

                    //设置参数列表
                    for (Class clz : method.getParameterTypes()) {
                        if (clz == Object[].class) { //表示target的参数列表
                            params.add(targetParams);
                        } else if (clz == Exception.class) {
                            params.add(e);
                        } else if (clz == String.class) {
                            params.add(methodName);
                        }
                    }
                    break;
                }
            }

            if (ObjectUtils.isEmpty(excetionMethod)) {
                super.handleException(methodName, targetParams, e);
            } else {
                excetionMethod.invoke(target, params.toArray());
            }
        }
    }


    /**
     * 异常返回之前，对异常的处理
     *
     * @param target
     * @param methodName
     * @param targetParams
     * @param failRetry
     * @throws Throwable
     */
    private void doBeforeExceptionalReturn(Object target, String methodName, Object[] targetParams, FailRetry failRetry) throws Throwable {
        if (StringUtils.isEmpty(failRetry.beforeExceptionalReturn())) {
            super.beforeExceptionalReturn(methodName, targetParams);
        } else {
            List<Object> params = Lists.newArrayList();
            Method beforeReturnMethod = null;
            for (Method method : target.getClass().getDeclaredMethods()) {
                if (method.getName().equals(failRetry.beforeExceptionalReturn())) {
                    beforeReturnMethod = method;

                    //设置参数列表
                    for (Class clz : method.getParameterTypes()) {
                        if (clz == Object[].class) {
                            params.add(targetParams);
                        } else if (clz == String.class) {
                            params.add(methodName);
                        }
                    }
                    break;
                }
            }

            if (ObjectUtils.isEmpty(beforeReturnMethod)) {
                super.beforeExceptionalReturn(methodName, targetParams);
            } else {
                beforeReturnMethod.invoke(target, params.toArray());
            }
        }
    }


    private boolean doShouldRetry(Object target, FailRetry failRetry) {
        // 判断次数
        Integer currentCount = retryCounters.get();
        retryCounters.set(++currentCount);
        if (currentCount > failRetry.maxRetryCount()) {
            return false;
        }

        String methodName = failRetry.shouldRetryMethod();
        if (StringUtils.isEmpty(methodName)) {
            return super.shouldRetry();
        } else {
            Method method = null;
            try {
                method = target.getClass().getMethod(methodName);
            } catch (NoSuchMethodException e) {
                return super.shouldRetry();
            }

            try {
                return (boolean) method.invoke(target, new Object[]{});
            } catch (Exception e) {

            }
        }

        return false;
    }

}
