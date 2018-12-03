package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 RequestMapping 注解
 * @Author hw
 * @Date 2018/12/3 15:47
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RequestMapping {

    /**
     * 表示 类 或者 方法的 访问路径
     */
    String value() default "";
}
