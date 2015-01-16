package com.wearezeta.auto.common.locators;

import java.lang.reflect.Field;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class ZetaElementLocatorFactory implements ElementLocatorFactory {
	public final SearchContext searchContext;
	
	public ZetaElementLocatorFactory(SearchContext searchContext) {
		this.searchContext = searchContext;
	}
	
	@Override
	public ElementLocator createLocator(final Field field) {
		return new ZetaElementLocator(searchContext, field);
	  }
}
