package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class GiphyPage extends WebPage {

    private final static String GIPHY_QUERY_LOCATOR = "[data-uie-name='giphy-query']";
    private final static String MORE_BUTTON_LOCATOR = "#giphy-modal [data-uie-name='do-try-another']";
    private final static String GIF_CONTAINER_LOCATOR = "#giphy-modal .gif-container-item";

    @FindBy(css = "#giphy-modal [data-uie-name='do-send-gif']")
    private WebElement sendButton;

    @FindBy(css = MORE_BUTTON_LOCATOR)
    private WebElement moreButton;

    @FindBy(css = "#giphy-modal [data-uie-name='do-close']")
    private WebElement closeButton;

    @FindBy(css = "#giphy-modal [data-uie-name='giphy-query']")
    private WebElement searchInput;

    @FindBy(css = GIF_CONTAINER_LOCATOR)
    private WebElement gifContainer;

    @FindBy(css = GIPHY_QUERY_LOCATOR)
    private WebElement giphyLink;

    public GiphyPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isGiphyQueryVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(GIPHY_QUERY_LOCATOR));
    }

    public String getSearchTerm() {
        return searchInput.getText();
    }

    public boolean isGifImageVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(GIF_CONTAINER_LOCATOR));
    }

    public boolean isMoreButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(MORE_BUTTON_LOCATOR));
    }

    public void clickSendButton() throws Exception {
        sendButton.click();
    }

}
