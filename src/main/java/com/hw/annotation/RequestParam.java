package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 RequestParam 注解
 * @Author hw
 * @Date 2018/12/3 15:51
 * @Version 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    /**
     * 参数的别名
     */
    String value() default "";

    /**
     * 参数是否必须
     */
    boolean required() default true;
}
