package com.pengpeng.stargame.annotation;

import java.lang.annotation.*;

@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ManagerAnnotation {
    public String id();
    public String cache() default "";
}
