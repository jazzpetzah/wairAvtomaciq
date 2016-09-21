package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
    private static final By idSendButton = By.id("ttv__confirmation__confirm");
    private static final By idCancelButton = By.id("ttv__confirmation__cancel");

    private static final By idPreviewTitle = By.id("ttv__giphy_preview__title");

    private static final By xpathLoadingIndicator = By.xpath("//*[@id='liv__giphy_preview__loading']/*");

    private static final By idGridImage = By.id("iv__row_giphy_image");

    private static final By idGridPreviewToolbar = By.id("t__giphy__toolbar");

    private static final String strIdSearchField = "cet__giphy_preview__search";
    private static final By idGridPreviewSearchTextInput = By.id(strIdSearchField);

    private static final By idGridPreviewErrorPlaceHolder = By.id("ttv__giphy_preview__error");

    private static final Function<String, String> xpathSearchFieldByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", strIdSearchField, value);

    private static final String ERROR_MESSAGE = "NO GIFS FOUND.";


    public GiphyPreviewPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private static final int GIPHY_LOCATOR_TIMEOUT_SECONDS = 5;

    public boolean isGiphyPreviewShown() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idSendButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)
                && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idCancelButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)
                && DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                idPreviewTitle, GIPHY_LOCATOR_TIMEOUT_SECONDS);
    }

    public void typeTextOnSearchField(String text, boolean hideKeyboard) throws Exception {
        final WebElement cursorInput = getElement(idGridPreviewSearchTextInput);
        final int maxTries = 3;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(text);
            ntry++;
        } while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSearchFieldByValue.apply(text)), 2)
                && ntry < maxTries);
        if (ntry >= maxTries) {
            throw new IllegalStateException(String.format(
                    "The string '%s' was autocorrected. Please disable autocorrection on the device and restart the " +
                            "test.",
                    text));
        }
        pressKeyboardSendButton();
        if (hideKeyboard) {
            this.hideKeyboard();
        }
    }

    public boolean isGiphyGridViewShown() throws Exception {
        return selectVisibleElements(idGridPreviewToolbar).size() > 0;
    }

    public void clickOnSomeGif() throws Exception {
        final WebElement giphyGridCell = getElement(idGridImage);
        giphyGridCell.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), idGridImage)) {
            // The grid has not been loaded yet
            giphyGridCell.click();
        }
    }

    private static final int GIPHY_LOAD_TIMEOUT_SECONDS = 60;

    public void clickSendButton() throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathLoadingIndicator,
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
}
