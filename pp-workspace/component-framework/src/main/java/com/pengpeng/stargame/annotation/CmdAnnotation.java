package com.pengpeng.stargame.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pengpeng.stargame.cmd.response.Response;

/**
 * 消息处理方法标记
 * 
 */
@Target( { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CmdAnnotation {
	String name();
	String cmd();
    boolean channel() default false;
	boolean check() default false;
	Class<?> vo() default void.class;
    Class<?> req();
	Class<? extends Response> rsp() default Response.class;
}
