package com.pengpeng.stargame.annotation;

import java.lang.annotation.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-28下午2:46
 */
@Target({ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EventAnnotation{
    public String name();
    public String desc();
}
