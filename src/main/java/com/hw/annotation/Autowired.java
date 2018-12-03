package com.hw.annotation;

import java.lang.annotation.*;

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
     * 是否必须
     */
    boolean required() default true;
}
