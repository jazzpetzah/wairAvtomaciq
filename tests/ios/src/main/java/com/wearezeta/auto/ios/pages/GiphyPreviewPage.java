package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GiphyPreviewPage extends IOSPage {
	private static final String nameGiphyRefreshButton = "leftButton";
	@FindBy(name = nameGiphyRefreshButton)
	private WebElement giphyMoreButton;

	private static final String nameGiphyLinkButton = "rightButton";
    @FindBy(name = nameGiphyLinkButton)
	private WebElement giphyLinkButton;

	private static final String nameGiphyTitleButton = "centerButton";
    @FindBy(name = nameGiphyTitleButton)
	private WebElement giphyTitleButton;

	private static final String xpathGithyImage = "//UIAButton[@name='rejectButton']/preceding-sibling::UIAImage[1]";
    @FindBy(xpath = xpathGithyImage)
	private WebElement giphyImage;

	private static final String nameGiphyCancelRequestButton = "rejectButton";
    @FindBy(name = nameGiphyCancelRequestButton)
	private WebElement giphyRejectButton;

    public static final String nameGiphySendButton = "acceptButton";
    @FindBy(name = nameGiphySendButton)
	private WebElement giphySendButton;

	private static final String nameNoGifsText = "OOOPS, NO MORE GIFS";
    @FindBy(name = nameNoGifsText)
	private WebElement noGifsText;

	private static final String nameGiphyGrid = "giphyCollectionView";

    public GiphyPreviewPage(Future<ZetaIOSDriver> driver) throws Exception {
		super(driver);
		// TODO Auto-generated constructor stub
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
				By.xpath(xpathGithyImage));
	}

	public boolean isGiphyRejectButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphyRejectButton);
	}

	public boolean isGiphySendButtonVisible() throws Exception {
		return DriverUtils.isElementPresentAndDisplayed(getDriver(),
				giphySendButton);
	}

	public void clickGiphyMoreButton() throws Exception {
		DriverUtils.waitUntilElementClickable(getDriver(), giphyMoreButton);
		giphyMoreButton.click();
	}

	public boolean isGiphyGridShown() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(getDriver(),
				By.name(nameGiphyGrid));
	}
}