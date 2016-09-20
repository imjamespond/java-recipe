package com.pengpeng.stargame.annotation;

import java.lang.annotation.*;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-12下午3:54
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DaoAnnotation {
    String prefix();
}
