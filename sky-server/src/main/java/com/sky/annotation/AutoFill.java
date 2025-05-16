package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动填充注解
 */
@Target(ElementType.METHOD)//注解作用在方法上
@Retention(RetentionPolicy.RUNTIME)//注解在运行时有效
public @interface AutoFill {
    //操作类型 update insert
    OperationType  value();
}
