package com.ssslinppp.aop;

import com.google.common.base.Stopwatch;
import com.ssslinppp.annotation.TimeElapsed;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2017/12/19 , Time: 16:49 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@Aspect
@Component
public class TimeElapsedAop {
    private static final Logger logger = LoggerFactory.getLogger(TimeElapsedAop.class);

    @Around(value = "@annotation(around)")
    public Object timeElapsedAround(ProceedingJoinPoint pjp, TimeElapsed around) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Object obj = pjp.proceed();
        long timeElapsed = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);

        //打印日志
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        if (timeElapsed > around.expectMaxMillseconds()) {
            if (around.needPrintParams()) {
                logger.warn("TimeElapsedAop - exec [{}, params: {}] take a long time, total cost {} ms",
                        signature.toString(), pjp.getArgs(), timeElapsed);
            } else {
                logger.warn("TimeElapsedAop - exec {} take a long time, total cost {} ms, expect max time {} ms",
                        signature.toString(), timeElapsed, around.expectMaxMillseconds());
            }
        } else {
            if (around.needPrintParams()) {
                logger.info("TimeElapsedAop - exec [{}, params: {}]  finish ,time elapsed {} ms",
                        signature.toString(), pjp.getArgs(), timeElapsed);
            } else {
                logger.info("TimeElapsedAop - exec {} finish ,time elapsed {} ms", signature.toString(), timeElapsed);
            }
        }

        return obj;
    }

}

