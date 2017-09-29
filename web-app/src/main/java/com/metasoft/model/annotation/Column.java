package com.metasoft.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	String desc() default "";//描述,不显示留空
	String value() default "";//数据库字段名,可留空
	String type() default "varchar(32) not null";//字段信息
}
