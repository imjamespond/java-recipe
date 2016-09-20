package com.pengpeng.stargame.annotation;

import java.lang.annotation.*;

/**
 * 消息处理方法标记
 *
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcAnnotation {
    String name() default "";
    String cmd();
    boolean lock() default true;
    boolean check() default false;
    Class<?> vo() default void.class;
    Class<?> req();
}
