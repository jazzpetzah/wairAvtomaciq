package com.wearezeta.auto.win.pages.webapp;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

import java.util.LinkedList;
import java.util.concurrent.Callable;

public class AboutPage extends WebPage {
	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger.getLog(AboutPage.class
			.getName());

	@SuppressWarnings("unused")
	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@FindBy(how = How.XPATH, using = WebAppLocators.AboutPage.xpathVersion)
	private WebElement version;

	public AboutPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isVisible() throws Exception {
		return executeOnLatestWindow(() -> DriverUtils.waitUntilLocatorAppears(
				this.getDriver(),
				By.xpath(WebAppLocators.AboutPage.xpathVersion)));
	}

	public boolean isVersionVisible() throws Exception {
		return executeOnLatestWindow(() -> DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(),
						By.cssSelector(WebAppLocators.AboutPage.xpathVersion)));
	}

	public String getVersion() throws Exception {
		return executeOnLatestWindow(version::getText);
	}

	private <T> T executeOnLatestWindow(Callable<T> function) throws Exception {
		LinkedList<String> windowHandles = new LinkedList<>(getDriver()
				.getWindowHandles());
		// switch to latest window
		getDriver().switchTo().window(windowHandles.getLast());
		T result = function.call();
		// switch to oldest window
		getDriver().switchTo().window(windowHandles.getFirst());
		return result;
	}

}
