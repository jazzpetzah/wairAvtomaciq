package com.wearezeta.auto.common.locators;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class ZetaElementLocatorFactory implements ElementLocatorFactory, ResetImplicitlyWaitTimeOut  {
	final ZetaSearchContext searchContext;
	private final ZetaTimeOutContainer timeOutContainer;

	public ZetaElementLocatorFactory(ZetaSearchContext searchContext,
			long implicitlyWaitTimeOut, TimeUnit timeUnit) {
		this.searchContext = searchContext;
		this.timeOutContainer = new ZetaTimeOutContainer(implicitlyWaitTimeOut, timeUnit);
	}
	
	public ZetaElementLocatorFactory(ZetaSearchContext searchContext) {
		this(searchContext, ZetaFieldDecorator.DEFAULT_IMPLICITLY_WAIT_TIMEOUT, 
				AppiumFieldDecorator.DEFAULT_TIMEUNIT);
	}
	@Override
	public ElementLocator createLocator(final Field field) {
		return new ZetaElementLocator(searchContext, field, timeOutContainer); //Zeta Time Out container
	  }

	@Override
	public void resetImplicitlyWaitTimeOut(long timeOut, TimeUnit timeUnit) {
		timeOutContainer.resetImplicitlyWaitTimeOut(timeOut, timeUnit);
	}
}
