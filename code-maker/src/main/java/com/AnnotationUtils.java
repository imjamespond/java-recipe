package com;

import java.lang.annotation.Annotation;

public class AnnotationUtils {
	
	public static <A extends Annotation> A getAnno(Class<?> cla,Class<A> annotationClass){
		return cla.getAnnotation(annotationClass);
	}
	public static <A extends Annotation> A getAnno(Object cla,Class<A> annotationClass){
		return cla.getClass().getAnnotation(annotationClass);
	}
}
