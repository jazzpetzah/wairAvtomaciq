package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
    private static final By idImagePreview = By.id("fl__overlay_container");

    //region Image Preview
    private static final By idSendButton = By.id("ttv__confirmation__confirm");
    private static final By idCancelButton = By.id("ttv__confirmation__cancel");
    private static final By idPreviewTitle = By.id("ttv__giphy_preview__title");
    //endregion

    //region Grid View
    private static final String idStrGridImage = "iv__row_giphy_image";
    private static final String idStrSearchField = "cet__giphy_preview__search";

    private static final By xpathLoadingIndicator = By.xpath("//*[@id='liv__giphy_preview__loading']/*");
    private static final By idGridImage = By.id(idStrGridImage);
    private static final By idGridPreviewToolbar = By.id("t__giphy__toolbar");
    private static final By idGridPreviewCloseButton = By.id("gtv__giphy_preview__close_button");
    private static final By idGridPreviewSearchTextInput = By.id(idStrSearchField);
    private static final By idGridImagesContainer = By.id("rv__giphy_image_preview");

    private static final By xpathGridPreviewErrorPlaceHolder = By.xpath(
            "//*[@id='ttv__giphy_preview__error' and @value='NO GIFS FOUND.']");

    private static final Function<String, String> xpathSearchFieldByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", idStrSearchField, value);

    private static final Function<Integer, String> xpathGridImageByIndex = index -> String
            .format("(//*[@id='%s'])[%d]", idStrGridImage, index);
    //endregion

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

    public boolean waitUntilGiphySearchField(String text) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathSearchFieldByValue.apply(text)));
    }

    public boolean waitUntilGiphyErrorMessageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathGridPreviewErrorPlaceHolder);
    }

    public boolean waitUntilGiphyErrorMessageInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathGridPreviewErrorPlaceHolder);
    }

    public void tapOnCloseButton() throws Exception {
        getElement(idGridPreviewCloseButton).click();
    }

    public void clearSearchField() throws Exception {
        getElement(idGridPreviewSearchTextInput).clear();
    }

    public BufferedImage getGiphyImageContainerState() throws Exception {
        return this.getElementScreenshot(getElement(idGridImagesContainer)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Giphy Image Container")
        );
    }

    public BufferedImage getGiphyImagePreviewState() throws Exception {
        return this.getElementScreenshot(getElement(idImagePreview)).orElseThrow(
                () -> new IllegalStateException("Cannot get a screenshot of Giphy Image Preview")
        );
    }

    public By getConfirmationButtonLocator(String buttonName) throws Exception {
        switch(buttonName.toLowerCase()) {
            case "cancel":
                return idCancelButton;
            case "send":
                return idSendButton;
            default:
                throw new IllegalArgumentException(
                        String.format("Cannot identify the giphy confirmation button type '%s'", buttonName));
        }
    }

    public boolean waitUntilConfirmationButtonVisible(String buttonName) throws Exception {
        By locator = getConfirmationButtonLocator(buttonName);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapGifPreviewByIndex(String index) throws Exception {
        By locator = getGiphyImageLocator(index);
        final WebElement giphyGridCell = getElement(locator);
        giphyGridCell.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator)) {
            // The grid has not been loaded yet
            giphyGridCell.click();
        }
    }

    private By getGiphyImageLocator(String index) {
        if(index.toLowerCase().contains("random")) {
            return idGridImage;
        } else {
            int indexNumber = Integer.valueOf(index.replaceAll("\\D+", "").trim());
            return By.xpath(xpathGridImageByIndex.apply(indexNumber));
        }
    }

    private static final int GIPHY_LOAD_TIMEOUT_SECONDS = 60;

    public void tapOnConfirmationButton(String buttonName) throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathLoadingIndicator,
                GIPHY_LOAD_TIMEOUT_SECONDS)) {
            log.warn(String.format(
                    "It seems that giphy has not been loaded within %s seconds (the progress bar is still visible)",
                    GIPHY_LOAD_TIMEOUT_SECONDS));
        }

        By locator = getConfirmationButtonLocator(buttonName);
        final WebElement button = getElement(locator);
        button.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), idSendButton, GIPHY_LOCATOR_TIMEOUT_SECONDS)) {
            // Sometimes the animation is not loaded fast enough
            button.click();
        }
    }
}
