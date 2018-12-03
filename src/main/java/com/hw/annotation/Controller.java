package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 Controller 注解
 * @Author hw
 * @Date 2018/12/3 15:38
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

    /**
     * Controller bean的别名
     */
    String value() default "";
}
