package com.wearezeta.auto.ios.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class GiphyPreviewPage extends IOSPage {
    private static final By nameGiphyRefreshButton = By.name("leftButton");

    private static final By nameGiphyLinkButton = By.name("rightButton");

    private static final By nameGiphyTitleButton = By.name("centerButton");

    // TODO: assign a name to Giphy image element
    private static final By xpathGiphyImage = By.xpath("//UIAImage[@visible='true']");

    private static final By nameGiphyCancelRequestButton = By.name("rejectButton");

    public static final By xpathGiphySendButton = By.xpath("//UIAButton[@label='SEND']");

    private static final By nameGiphyGrid = By.name("giphyCollectionView");

    public GiphyPreviewPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    public void tapSendGiphyButton() throws Exception {
        getElement(xpathGiphySendButton).click();
    }

    public boolean isGiphyRefreshButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGiphyRefreshButton);
    }

    public boolean isGiphyLinkButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGiphyLinkButton);
    }

    public boolean isGiphyTitleButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGiphyTitleButton);
    }

    public boolean isGiphyImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGiphyImage);
    }

    public boolean isGiphyRejectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGiphyCancelRequestButton);
    }

    public boolean isGiphySendButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGiphySendButton);
    }

    public void clickGiphyMoreButton() throws Exception {
        getElement(nameGiphyRefreshButton, "Giphy Refresh button is not visible").click();
    }

    public boolean isGiphyGridShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), nameGiphyGrid);
    }
}