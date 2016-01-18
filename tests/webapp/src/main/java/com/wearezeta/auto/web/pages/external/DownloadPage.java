package com.wearezeta.auto.web.pages.external;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

public class DownloadPage extends WebPage {
	
	// FIXME: Works for staging backend only
	private static final String SITE_ROOT = WebAppConstants.STAGING_SITE_ROOT;
	
	@FindBy(css = ExternalLocators.DownloadPage.cssDownloadIOS)
	private WebElement iosDownloadButton;
	
	@FindBy(css = ExternalLocators.DownloadPage.cssDownloadAndroid)
	private WebElement androidDownloadButton;
	
	@FindBy(css = ExternalLocators.DownloadPage.cssDownloadOSX)
	private WebElement osxDownloadButton;
	
	@FindBy(css = ExternalLocators.DownloadPage.cssDownloadWindows)
	private WebElement windowsDownloadButton;
	
	@FindBy(css = ExternalLocators.DownloadPage.cssDownloadWebapp)
	private WebElement webappDownloadButton;
	
	public DownloadPage(Future<ZetaWebAppDriver> lazyDriver)
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
	
	public String getIOSDownloadHref() {
		return iosDownloadButton.getAttribute("href");
	}

	public String getAndroidDownloadHref() {
		return androidDownloadButton.getAttribute("href");
	}
	
	public String getOSXDownloadHref() {
		return osxDownloadButton.getAttribute("href");
	}
	
	public String getWindowsDownloadHref() {
		return windowsDownloadButton.getAttribute("href");
	}
	
	public String getWebappDownloadHref() {
		return webappDownloadButton.getAttribute("href");
	}
	
}
