package com.wearezeta.auto.common.locators;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.driver.ZetaDriver;

public final class ZetaSearchContext implements SearchContext {
	private Future<? extends RemoteWebDriver> lazyDriver;

	public Future<? extends RemoteWebDriver> getLazyDriver() {
		return this.lazyDriver;
	}

	public RemoteWebDriver getDriver() {
		try {
			return lazyDriver.get(ZetaDriver.INIT_TIMEOUT,
					TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public ZetaSearchContext(Future<? extends RemoteWebDriver> lazyDriver) {
		this.lazyDriver = lazyDriver;
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.getDriver().findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.getDriver().findElement(by);
	}
}
