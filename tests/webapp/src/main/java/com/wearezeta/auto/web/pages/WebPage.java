package com.wearezeta.auto.web.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.Alert;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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

	public static void clearPagesCollection() throws IllegalArgumentException,
			IllegalAccessException {
		clearPagesCollection(WebappPagesCollection.class, WebPage.class);
	}

	public void acceptAlert() throws Exception {
		Alert popup = getDriver().switchTo().alert();
		popup.accept();
	}

	public void switchLanguage(String language) throws Exception {
		String currentUrl = this.getDriver().getCurrentUrl();
		URL url = new URL(currentUrl);
		if (url.getQuery() == null) {
			this.getDriver().navigate().to(currentUrl + "?hl=" + language);
		} else {
			this.getDriver().navigate().to(currentUrl + "&hl=" + language);
		}
	}

	public String getText() throws Exception {
		return getDriver().findElement(By.tagName("body")).getText();
	}

	public List<String> getPlaceholders() throws Exception {
		List<WebElement> inputElements = getDriver().findElements(By.tagName("input"));
		List<String> placeholders = new ArrayList<>();
		for (WebElement element : inputElements) {
			if(element.isDisplayed()) {
				placeholders.add(element.getAttribute("placeholder"));
			}
		}
		return placeholders;
	}

	public List<String> getButtonValues() throws Exception {
		List<WebElement> inputElements = getDriver().findElements(By.cssSelector("input[type='submit']"));
		List<String> values = new ArrayList<>();
		for (WebElement element : inputElements) {
			if(element.isDisplayed()) {
				values.add(element.getAttribute("value"));
			}
		}
		return values;
	}
}
