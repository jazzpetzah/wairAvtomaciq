package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;

public class GiphyPreviewPage extends IOSPage {

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphyRefreshButton)
	private WebElement giphyMoreButton;

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphyLinkButton)
	private WebElement giphyLinkButton;

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphyTitleButton)
	private WebElement giphyTitleButton;

	@FindBy(how = How.XPATH, using = IOSLocators.GiphyPreviewPage.xpathGithyImage)
	private WebElement giphyImage;

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphyCancelRequestButton)
	private WebElement giphyRejectButton;

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameGiphySendButton)
	private WebElement giphySendButton;

	@FindBy(how = How.NAME, using = IOSLocators.GiphyPreviewPage.nameNoGifsText)
	private WebElement noGifsText;

	public GiphyPreviewPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void tapSendGiphyButton() throws Exception {
		giphySendButton.click();
	}

	public boolean isGiphyRefreshButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphyMoreButton);
	}

	public boolean isGiphyLinkButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphyLinkButton);
	}

	public boolean isGiphyTitleButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphyTitleButton);
	}

	public boolean isGiphyImageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.xpath(IOSLocators.GiphyPreviewPage.xpathGithyImage));
	}

	public boolean isGiphyRejectButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphyRejectButton);
	}

	public boolean isGiphySendButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphySendButton);
	}

	public boolean isNoGifsTextVisible() throws Exception {
		return DriverUtils
				.isElementPresentAndDisplayed(getDriver(), noGifsText);
	}

	public void clickGiphyMoreButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), giphyMoreButton);
		giphyMoreButton.click();
	}

	public boolean isGiphyGridShown() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(IOSLocators.GiphyPreviewPage.nameGiphyGrid));
	}

}