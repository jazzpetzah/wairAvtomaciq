package com.wearezeta.auto.web.pages.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class YouAreInvitedPage extends WebPage {
	// FIXME: Works for staging backend only
	private static final String SITE_ROOT = WebAppConstants.STAGING_SITE_ROOT;

	@FindBy(xpath = ExternalLocators.YouAreInvitedPage.xpathConnectButton)
	private WebElement connectButton;

	public YouAreInvitedPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public void setUrl(String url) {
		// To make sure that we are redirected to staging site
		try {
			super.setUrl(transformSiteUrl(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static String transformSiteUrl(String url)
			throws URISyntaxException {
		final URI uri = new URI(url);
		return SITE_ROOT + uri.getPath();
	}

	private static final int BUTTONS_VISIBILITY_TIMEUOT_SECONDS = 5;

	public void waitUntilConnectButtonVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(ExternalLocators.YouAreInvitedPage.xpathConnectButton),
						BUTTONS_VISIBILITY_TIMEUOT_SECONDS) : "Connect button has not beem show on You are invited page after "
				+ BUTTONS_VISIBILITY_TIMEUOT_SECONDS + " seconds timeout";
	}

	public void waitUntilDownloadWireButtonVisible() throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.xpath(ExternalLocators.YouAreInvitedPage.xpathDownloadWireButton),
						BUTTONS_VISIBILITY_TIMEUOT_SECONDS) : "Download Wire button has not beem show on You are invited page after "
				+ BUTTONS_VISIBILITY_TIMEUOT_SECONDS + " seconds timeout";
	}

	public void clickConnectButton() {
		connectButton.click();
	}
}
