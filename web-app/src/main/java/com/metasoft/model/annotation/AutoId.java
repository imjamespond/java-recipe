package com.metasoft.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AutoId {
	String value() default "ID";
	String start() default "1";
	String stride() default "1";
	String desc() default "序号";
}
