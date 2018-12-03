package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 Repository 注解
 * @Author hw
 * @Date 2018/12/3 15:45
 * @Version 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Repository {
    /**
     * Repository bean 别名
     */
    String value() default "";
}
