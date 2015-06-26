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

	//@FindBy(css = "#giphy-modal [data-uie-name='enter-giphy-query']")
	@FindBy(css = "#giphy-modal input")
	private WebElement searchInput;

	final static String gifContainerLocator = "#giphy-modal .gif-container-item";

	@FindBy(css = gifContainerLocator)
	private WebElement gifContainer;

	final static String giphyLinkLocator = "#giphy-modal [data-uie-name='go-giphy-link']";

	@FindBy(css = giphyLinkLocator)
	private WebElement giphyLink;

	public GiphyPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public boolean isGiphyLinkVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(giphyLinkLocator));
	}

	public String getSearchTerm() {
		return searchInput.getAttribute("value");
	}

	public boolean isGifImageVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(gifContainerLocator));
	}

	public boolean isMoreButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.cssSelector(moreButtonLocator));
	}

	public ConversationPage clickSendButton() throws Exception {
		sendButton.click();
		return new ConversationPage(getLazyDriver());
	}

}
