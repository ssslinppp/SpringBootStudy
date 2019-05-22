package com.ssslinppp.failRetry.annotation;

import java.lang.annotation.*;

/**
 * Desc: 失败重试
 * <p>
 * 某些方法执行时，若执行失败（如抛出异常），希望`失败重试`，并可以设置失败重试的次数，此模块就是完成此功能；
 * <p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FailRetry {
    /**
     * 最大失败重试次数，默认值：3
     *
     * @return
     */
    int maxRetryCount() default 3;

    /**
     * 两次重试之间的时间间隔(毫秒)，默认：500ms
     *
     * @return
     */
    long interval() default 500;

    /**
     * 异常处理，需传入一个方法名；
     * <p>
     * 默认的异常处理行为：仅仅是记录日志
     * <p>
     * 方法参数类型选择（可以是如下参数的任意组合，顺序无要求）：
     * <ul>
     * <li>String: 表示方法名</li>
     * <li>Object[]: 表示方法参数</li>
     * <li>Exception: 表示抛出的异常</li>
     * </ul>
     * <p>
     * 示例：
     * <pre>{@code
     *  public void doException(String methodName, Object[] params, Exception e) {
     *        // do something
     * }
     * }</pre>
     *
     * @return
     */
    String handleExceptionMethod() default "";

    /**
     * 用来控制是否需要下次重试<br/>
     * 方法要求：
     * <ul>
     * <li>参数：无参</li>
     * <li>返回值：boolean</li>
     * <li>无异常抛出</li>
     * </ul>
     *
     * @return
     */
    String shouldRetryMethod() default "";

    /**
     * 在所有重试都失败后进行处理的扩展点，需传入一个方法名；
     * <p>
     * 默认的处理行为：仅仅是记录日志
     * <p>
     * 方法参数类型选择（可以是如下参数的任意组合，顺序无要求）：
     * <ul>
     * <li>String: 表示方法名</li>
     * <li>Object[]: 表示方法参数</li>
     * </ul>
     * <p>
     * 示例：
     * <pre>{@code
     *  public void allRetryFail(String methodName, Object[] params) {
     *          // do something
     *  }
     * }</pre>
     *
     * @return
     */
    String beforeExceptionalReturn() default "";
}
