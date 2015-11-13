package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
	@FindBy(id = giphyPreviewButtonId)
	private WebElement giphyPreviewButton;
	final By giphyPreviewButtonLocator = By.id(giphyPreviewButtonId);

	public static final String sendButtonId = "ttv__confirmation__confirm";
	@FindBy(id = sendButtonId)
	private WebElement sendButton;
	final By giphySendButtonLocator = By.id(sendButtonId);

	public static final String cancelButtonId = "ttv__confirmation__cancel";
	@FindBy(id = cancelButtonId)
	private WebElement cancelButton;
	final By giphyCancelButtonLocator = By.id(cancelButtonId);

	public static final String giphyReloadButtonId = "gtv__giphy_preview__reload_button";
	@FindBy(id = giphyReloadButtonId)
	private WebElement giphyReloadButton;
	final By giphyReloadButtonLocator = By.id(giphyReloadButtonId);

	public static final String giphyLinkButtonId = "gtv__giphy_preview__link_button";
	@FindBy(id = giphyLinkButtonId)
	private WebElement giphyLinkButton;
	final By giphyLinkButtonLocator = By.id(giphyLinkButtonId);

	public static final String giphyPreviewTitleId = "ttv__giphy_preview__title";
	@FindBy(id = giphyPreviewTitleId)
	private WebElement giphyPreviewTitle;
	final By giphyPreviewTitleLocator = By.id(giphyPreviewTitleId);

	private static final String xpathGiphyLoadingIndicator = "//*[@id='liv__giphy_preview__loading']/*";
	final By giphyLoadingProgressLocator = By.xpath(xpathGiphyLoadingIndicator);

	@Override
	protected ZetaAndroidDriver getDriver() throws Exception {
		return (ZetaAndroidDriver) super.getDriver();
	}

	public GiphyPreviewPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickOnGIFButton() throws Exception {
		assert DriverUtils.waitUntilLocatorAppears(getDriver(),
				giphyPreviewButtonLocator);
		giphyPreviewButton.click();
	}

	private static final int GIPHY_LOCATOR_TIMEOUT_SECONDS = 5;

	public boolean isGiphyPreviewShown() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				giphySendButtonLocator, GIPHY_LOCATOR_TIMEOUT_SECONDS)
				&& DriverUtils
						.waitUntilLocatorAppears(getDriver(),
								giphyCancelButtonLocator,
								GIPHY_LOCATOR_TIMEOUT_SECONDS)
				&& DriverUtils.waitUntilLocatorAppears(getDriver(),
						giphyLinkButtonLocator, GIPHY_LOCATOR_TIMEOUT_SECONDS)
				&& DriverUtils
						.waitUntilLocatorAppears(getDriver(),
								giphyPreviewTitleLocator,
								GIPHY_LOCATOR_TIMEOUT_SECONDS);
	}

	private static final int GIPHY_LOAD_TIMEOUT_SECONDS = 60;

	public void clickSendButton() throws Exception {
		assert DriverUtils.waitUntilLocatorAppears(getDriver(),
				giphyLoadingProgressLocator, GIPHY_LOCATOR_TIMEOUT_SECONDS);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(),
				giphyLoadingProgressLocator, GIPHY_LOAD_TIMEOUT_SECONDS)) {
			log.warn(String
					.format("It seems that giphy has not been loaded within %s seconds (the progress bar is still visible)",
							GIPHY_LOAD_TIMEOUT_SECONDS));
		}
		assert DriverUtils.waitUntilElementClickable(getDriver(), sendButton);
		sendButton.click();
		final By giphySendLocator = By.id(sendButtonId);
		assert DriverUtils.waitUntilLocatorDissapears(getDriver(),
				giphySendLocator, GIPHY_LOCATOR_TIMEOUT_SECONDS);
	}

}
