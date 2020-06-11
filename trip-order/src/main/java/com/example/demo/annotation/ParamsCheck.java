package com.example.demo.annotation;

/**
 * @author : wulg29230
 * @Date : 2020/3/10 9:24
 **/
public @interface ParamsCheck {
    Class<?> clazz() default Object.class;
}
