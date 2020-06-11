package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * @author : wulg29230
 * @Date : 2020/3/9 14:36
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {
    /**
     * 入参的类型
     * @return
     */
    Class<?> type();

    /**
     * 入参中作为redis锁的key字段
     * @return
     */
    String key();

    /**
     * 获取锁等待时间
     * @return
     */
    long waitTime() default 0L;
}
