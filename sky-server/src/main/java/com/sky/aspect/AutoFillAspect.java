package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自动填充 实现公共字段填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
    * 切入点
     */
    @Pointcut("execution(public * com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")//切入点表达式 （指定在哪些方法(也即有该注解的方法上）上执行切面逻辑）
    public void autoFillPointcut(){}

    /**
     * 前置通知 在通知中进行公共字段的填充
     */
    @Before("autoFillPointcut()")
    public void before(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        log.info("开始进行自动填充公共字段");
        //获取当前被拦截的方法的注解的数据库操作类型
        MethodSignature   signature = (MethodSignature)joinPoint.getSignature();//获取被拦截的方法的签名信息
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取被拦截的方法的注解对象
        OperationType type = autoFill.value();//获取注解对象的数据库操作类型
        //获取当前被拦截的方法的参数---实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        Object entity = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //赋值 通过反射
        if (type == OperationType.INSERT){
           try {
               Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
               Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
               Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
               Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
               //通过反射为对象赋值
               setCreateTime.invoke(entity,now);
               setCreateUser.invoke(entity,currentId);
               setUpdateTime.invoke(entity,now);
               setUpdateUser.invoke(entity,currentId);
           } catch (NoSuchMethodException e) {
               throw new RuntimeException(e);
           }

        } else if (type == OperationType.UPDATE) {
            Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            //通过反射为对象赋值
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }


    }

}
