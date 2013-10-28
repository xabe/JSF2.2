package com.prueba.gui.importer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintImportField {
	
	String columnName() default "";
	
	String messageError() default "{error.import}";
	
	String pattern() default ".+";
	
	boolean isCompulsory()  default false;
	
	boolean isPK() default false;
}
