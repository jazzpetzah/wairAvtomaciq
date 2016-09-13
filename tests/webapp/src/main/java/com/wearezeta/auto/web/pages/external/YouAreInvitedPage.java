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

public class YouAreInvitedPage extends WebPage {

	@FindBy(xpath = ExternalLocators.YouAreInvitedPage.xpathConnectButton)
	private WebElement connectButton;
	
	@FindBy(css = ExternalLocators.YouAreInvitedPage.cssDownloadButton)
	private WebElement downloadButton;
	
	@FindBy(css = ExternalLocators.YouAreInvitedPage.cssConnectWireButton)
	private WebElement connectWireButton;
	
	@FindBy(css = ExternalLocators.YouAreInvitedPage.cssDownloadWireButton)
	private WebElement openButton;


	public YouAreInvitedPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public void setUrl(String url) {
		// To make sure that we are redirected to staging site
		try {
			super.setUrl(transformSiteUrl(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String transformSiteUrl(String url)
			throws Exception {
		final URI uri = new URI(url);
		final String website = CommonUtils.getWebsitePathFromConfig(DownloadPage.class);
		return website + uri.getPath();
	}

	public void waitUntilConnectButtonVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(ExternalLocators.YouAreInvitedPage.xpathConnectButton)) : "Connect button has not beem show on You are invited page after ";
	}

	public void waitUntilDownloadWireButtonVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(ExternalLocators.YouAreInvitedPage.cssDownloadWireButton)) : "Download Wire button has not been show on You are invited page";
	}
	
	public void waitUntilDownloadButtonVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(ExternalLocators.YouAreInvitedPage.cssDownloadButton)) : "Download button has not been show on You are invited page";
	}

	public void clickConnectButton() {
		connectButton.click();
	}
	
	public String getDownloadHref() {
		return downloadButton.getAttribute("href");
	}
	
	public String getConnectHref() {
		return connectWireButton.getAttribute("href");
	}
	
	public String getOpenHref() {
		return openButton.getAttribute("href");
	}
}
