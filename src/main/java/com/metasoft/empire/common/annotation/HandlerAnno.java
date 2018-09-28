package com.metasoft.empire.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HandlerAnno {
    String name() default "";
    String cmd();
    Class<?> req();
    HandlerType type() default HandlerType.RPC;
}
