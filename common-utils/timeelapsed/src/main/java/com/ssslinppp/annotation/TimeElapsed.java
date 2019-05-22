package com.ssslinppp.annotation;

import java.lang.annotation.*;

/**
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeElapsed {

    /**
     * 期望的最大运行时间，默认时间：5s
     * <p>
     * 当超过maxMillseconds时，打印warn日志；<br/>
     * 否则打印info日志；<br/>
     *
     * @return
     */
    long expectMaxMillseconds() default 5_000;

    /**
     * 是否打印方法参数，默认值：false
     *
     * @return
     */
    boolean needPrintParams() default false;
}
