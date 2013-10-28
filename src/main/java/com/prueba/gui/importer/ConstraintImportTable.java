package com.prueba.gui.importer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstraintImportTable {
	
	String type() default "incremental";
	
	boolean ignoreErrors() default true;
	
	String[] keys() default {"id"};
}
