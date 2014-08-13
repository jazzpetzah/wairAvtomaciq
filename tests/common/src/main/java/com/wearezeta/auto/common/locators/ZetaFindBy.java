package com.wearezeta.auto.common.locators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.support.How;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface ZetaFindBy {
	How how() default How.ID;
	
	String locatorsDb() default "";
	
	String locatorKey() default "";
	
}
