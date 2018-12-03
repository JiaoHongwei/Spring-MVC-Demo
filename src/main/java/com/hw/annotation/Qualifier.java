package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 Qualifier 注解 用来指定注入的bean实例
 * @Author hw
 * @Date 2018/12/3 16:32
 * @Version 1.0
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Qualifier {
    String value() default "";
}
