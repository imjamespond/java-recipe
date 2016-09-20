package com.pengpeng.stargame.annotation;

import java.lang.annotation.*;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-17下午2:15
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Desc {
    String value();
}
