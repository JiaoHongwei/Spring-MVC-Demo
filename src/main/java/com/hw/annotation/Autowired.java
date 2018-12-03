package com.hw.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description 自定义自动注入 Autowired 注解
 * @Author hw
 * @Date 2018/12/3 16:42
 * @Version 1.0
 */
@Documented
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    /**
     * Declares whether the annotated dependency is required.
     * @return
     */
    boolean required() default true;
}
