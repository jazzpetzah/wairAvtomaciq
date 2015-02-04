package com.wearezeta.auto.common.locators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface ZetaFindBy {
	ZetaHow how() default ZetaHow.ID;
	
	String locatorsDb() default "";
	
	String locatorKey() default "";
	
}
