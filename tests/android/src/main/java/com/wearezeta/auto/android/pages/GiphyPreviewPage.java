package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
    @FindBy(id = giphyPreviewButtonId)
    private WebElement giphyPreviewButton;
    private final By giphyPreviewButtonLocator = By.id(giphyPreviewButtonId);

    private static final String sendButtonId = "ttv__confirmation__confirm";
    @FindBy(id = sendButtonId)
    private WebElement sendButton;
    private final By giphySendButtonLocator = By.id(sendButtonId);

    private static final String cancelButtonId = "ttv__confirmation__cancel";
    @FindBy(id = cancelButtonId)
    private WebElement cancelButton;
    private final By giphyCancelButtonLocator = By.id(cancelButtonId);

    private static final String giphyReloadButtonId = "gtv__giphy_preview__reload_button";
    @FindBy(id = giphyReloadButtonId)
    private WebElement giphyReloadButton;

    private static final String giphyLinkButtonId = "gtv__giphy_preview__link_button";
    @FindBy(id = giphyLinkButtonId)
    private WebElement giphyLinkButton;
    private final By giphyLinkButtonLocator = By.id(giphyLinkButtonId);

    private static final String giphyPreviewTitleId = "ttv__giphy_preview__title";
    @FindBy(id = giphyPreviewTitleId)
    private WebElement giphyPreviewTitle;
    private final By giphyPreviewTitleLocator = By.id(giphyPreviewTitleId);

    private static final String xpathGiphyLoadingIndicator = "//*[@id='liv__giphy_preview__loading']/*";
    private final By giphyLoadingProgressLocator = By.xpath(xpathGiphyLoadingIndicator);

    private static final String giphyGridImageId = "iv__row_giphy_image";
    @FindBy(id = giphyGridImageId)
    private List<WebElement> giphyGridImages;


    private static final String idOpenGiphyCollectionButton = "gtv__giphy_preview__link_button";
    @FindBy(id = idOpenGiphyCollectionButton)
    private WebElement openGiphyCollectionButton;

    private static final Function<Integer, String> xpathGiphyPreviewByIndex = idx ->
            String.format("//*[@id='rv__giphy_image_preview']/*[@id='iv__row_giphy_image'][%d]", idx);

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

    public boolean isGiphyGridViewShown() {
        return giphyGridImages.size() > 0;
    }

    public void clickOnSomeGif() {
        int toSelect = 3;
        giphyGridImages.get(toSelect).click();
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

    public void clickGiphyLinkButton() {
        giphyLinkButton.click();
    }

    public void clickShowGiphyGridButton() {
        openGiphyCollectionButton.click();
    }

    public void selectFirstGridItem() throws Exception {
        final By locator = By.xpath(xpathGiphyPreviewByIndex.apply(1));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                "Giphy previews collection is not visible";
        getDriver().findElement(locator).click();
    }
}
