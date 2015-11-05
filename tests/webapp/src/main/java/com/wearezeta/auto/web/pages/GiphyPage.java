package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class GiphyPage extends WebPage {

	@FindBy(css = "#giphy-modal [data-uie-name='do-send-gif']")
	private WebElement sendButton;

	final static String moreButtonLocator = "#giphy-modal [data-uie-name='do-try-another']";

	@FindBy(css = moreButtonLocator)
	private WebElement moreButton;

	@FindBy(css = "#giphy-modal [data-uie-name='do-close']")
	private WebElement closeButton;

	@FindBy(css = "#giphy-modal [data-uie-name='giphy-query']")
	private WebElement searchInput;

	final static String gifContainerLocator = "#giphy-modal .gif-container-item";

	@FindBy(css = gifContainerLocator)
	private WebElement gifContainer;

	final static String giphyQueryLocator = "[data-uie-name='giphy-query']";

	@FindBy(css = giphyQueryLocator)
	private WebElement giphyLink;

	public GiphyPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isGiphyQueryVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(giphyQueryLocator));
	}

	public String getSearchTerm() {
		return searchInput.getText();
	}

	public boolean isGifImageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(gifContainerLocator));
	}

	public boolean isMoreButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(moreButtonLocator));
	}

	public void clickSendButton() throws Exception {
		sendButton.click();
	}

}
