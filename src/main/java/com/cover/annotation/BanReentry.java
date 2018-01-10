package com.cover.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Created by zhaoqing on 2017/10/13.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BanReentry {

    String prefix() default "cover:";

    String key() default "";//default clz + methodName

    int sec() default 10;

}
