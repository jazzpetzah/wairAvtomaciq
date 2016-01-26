package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
    private static final By idSendButton = By.id("ttv__confirmation__confirm");

    private static final By idCancelButton = By.id("ttv__confirmation__cancel");

    // private static final By idGiphyReloadButton = By.id("gtv__giphy_preview__reload_button");

    private static final By idGiphyLinkButton = By.id("gtv__giphy_preview__link_button");

    private static final By idGiphyPreviewTitle = By.id("ttv__giphy_preview__title");

    private static final By xpathGiphyLoadingIndicator = By.xpath("//*[@id='liv__giphy_preview__loading']/*");

    private static final By idGiphyGridImage = By.id("iv__row_giphy_image");

    public GiphyPreviewPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void clickOnGIFButton() throws Exception {
        getElement(idGiphyPreviewButton, "GIF button is not visible in the cursor input").click();
    }

    private static final int GIPHY_LOCATOR_TIMEOUT_SECONDS = 5;

    public boolean isGiphyPreviewShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idSendButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)
                && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idCancelButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)
                && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idGiphyLinkButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)
                && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idGiphyPreviewTitle, GIPHY_LOCATOR_TIMEOUT_SECONDS);
    }

    public boolean isGiphyGridViewShown() throws Exception {
        return selectVisibleElements(idGiphyGridImage).size() > 0;
    }

    public void clickOnSomeGif() throws Exception {
        final WebElement giphyGridCell = getElement(idGiphyGridImage);
        giphyGridCell.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), idGiphyGridImage)) {
            // The grid has not been loaded yet
            giphyGridCell.click();
        }
    }

    private static final int GIPHY_LOAD_TIMEOUT_SECONDS = 60;

    public void clickSendButton() throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathGiphyLoadingIndicator,
                GIPHY_LOAD_TIMEOUT_SECONDS)) {
            log.warn(String.format(
                    "It seems that giphy has not been loaded within %s seconds (the progress bar is still visible)",
                    GIPHY_LOAD_TIMEOUT_SECONDS));
        }
        final WebElement sendButton = getElement(idSendButton);
        sendButton.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), idSendButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)) {
            // Sometimes the animation is not loaded fast enough
            sendButton.click();
        }
    }

    public void clickGiphyLinkButton() throws Exception {
        getElement(idGiphyLinkButton).click();
    }
}
