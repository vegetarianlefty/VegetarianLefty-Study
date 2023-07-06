package com.vegetarianlefty.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/** 单机模式
 * <p>限流注解 Limit</p>
 *
 * @date 2023/7/6 10:23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Limit {
    // 资源key
    String key() default "";

    // 最多访问次数
    double permitsPerSecond();

    // 时间
    long timeout();

    // 时间类型
    TimeUnit timeunit() default TimeUnit.MILLISECONDS;

    // 提示信息
    String msg() default "系统繁忙,请稍后再试111";
}
