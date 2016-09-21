package com.wearezeta.auto.web.pages.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class VerifyPage extends WebPage {
	
	@FindBy(css = ExternalLocators.VerifyPage.cssDownloadIPhone)
	private WebElement iphoneDownloadButton;
	
	@FindBy(css = ExternalLocators.VerifyPage.cssDownloadAndroid)
	private WebElement androidDownloadButton;
	
	@FindBy(css = ExternalLocators.VerifyPage.cssDownloadOSX)
	private WebElement osxDownloadButton;
	
	@FindBy(css = ExternalLocators.VerifyPage.cssDownloadWindows)
	private WebElement windowsDownloadButton;
	
	@FindBy(css = ExternalLocators.VerifyPage.cssWebappButton)
	private WebElement webappButton;

	@FindBy(css = ExternalLocators.VerifyPage.cssOpenWireButton)
	private WebElement openWireButton;
	
	private static final String ERROR_TEXT = "Something went wrong.";
	
	public VerifyPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	protected ZetaWebAppDriver getDriver() throws Exception {
		return (ZetaWebAppDriver) super.getDriver();
	}

	public String getDownloadUrl(String agent) {
		switch (agent) {
			case "iphone":
				return iphoneDownloadButton.getAttribute("href");
			case "android":
				return androidDownloadButton.getAttribute("href");
			case "osx":
				return osxDownloadButton.getAttribute("href");
			case "windows":
				return windowsDownloadButton.getAttribute("href");
			default: break;
		}
		return "";
	}
	
	public String getWebappUrl() {
		return webappButton.getAttribute("href");
	}
	
	public boolean isErrorMessageVisible() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(ExternalLocators.VerifyPage.xpathLabelByText
								.apply(ERROR_TEXT)));
		}

	public String getWireUrlFromButton() {
		return openWireButton.getAttribute("href");
	}
}
