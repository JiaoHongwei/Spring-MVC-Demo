package com.hw.annotation;

import java.lang.annotation.*;

/**
 * @Description 自定义 Quatifier 注解
 * @Author hw
 * @Date 2018/12/3 16:32
 * @Version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Quatifier {
    String value() default "";
}
