package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;

public class WebPage extends BasePage {
	@Override
	protected ZetaWebAppDriver getDriver() throws Exception {
		return (ZetaWebAppDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaWebAppDriver> getLazyDriver() {
		return (Future<ZetaWebAppDriver>) super.getLazyDriver();
	}

	private String url = null;

	public String getUrl() {
		return this.url;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		this(lazyDriver, null);
	}

	public WebPage(Future<ZetaWebAppDriver> lazyDriver, String url)
			throws Exception {
		super(lazyDriver);

		this.url = url;
	}

	public void navigateTo() throws Exception {
		if (this.url != null) {
			this.getDriver().navigate().to(this.url);
		} else {
			throw new RuntimeException(
					String.format(
							"'%s' page does not support direct navigation and can be loaded from other pages only",
							this.getClass().getSimpleName()));
		}
	}

	public void openNewTab() throws Exception {
		String openinNewTab = "";
		if(WebAppExecutionContext.isCurrentPlatformWindows()) {
			openinNewTab = Keys.chord(Keys.CONTROL,"t");
		} else {
			openinNewTab = Keys.chord(Keys.META,"t");
		}
		getDriver().findElement(By.cssSelector("body")).sendKeys(openinNewTab);
	}

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(WebappPagesCollection.class, WebPage.class);
	}

	public void acceptAlert() throws Exception {
		Alert popup = getDriver().switchTo().alert();
		popup.accept();
	}
}
