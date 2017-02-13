package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class QuickReplyPage extends AndroidPage {
    private static final String strIdTextInput = "tet__quick_reply__message";

    private static final By idTextInput = By.id(strIdTextInput);
    private static final By idContainer = By.id("fl__quick_reply__container");
    private static final By idOpenWireButton = By.id("ll__quick_reply__open_external");

    private static final Function<String, String> xpathStrQuickReplyNameByValue = value -> String
            .format("//*[@id='ttv__quick_reply__name' and @value='%s']", value);
    private static final Function<String, String> xpathStrQuickReplyCounterByValue = value -> String
            .format("//*[@id='ttv__quick_reply__counter' and @value='%s']", value);
    private static final Function<String, String> xpathStrQuickReplyReceviedContent = value -> String
            .format("//*[@id='ttv__quick_reply__content' and @value='%s']", value);
    private static final Function<String, String> xpathStrEditTextByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", strIdTextInput, value);


    public QuickReplyPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idContainer);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idContainer);
    }

    public void tapButton(String buttonName) throws Exception {
        By locator = getButtonLocatorByType(buttonName);
        getElement(locator).click();
    }

    public boolean waitUntilContentVisible(String contentType, String value) throws Exception {
        By locator = getContentLocatorByType(contentType, value);
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void typeAndSendMessage(String message) throws Exception {
        final WebElement cursorInput = getElement(idTextInput);
        final int maxTries = 5;
        int ntry = 0;
        do {
            cursorInput.clear();
            cursorInput.sendKeys(message);
            ntry++;
        } while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrEditTextByValue.apply(message)), 2) && ntry < maxTries);
        if (ntry >= maxTries) {
            throw new IllegalStateException(String.format(
                    "The string '%s' was autocorrected. Please disable autocorrection on the device and restart the " +
                            "test.",
                    message));
        }
        pressKeyboardSendButton();
    }

    private static By getButtonLocatorByType(String buttonType) throws Exception {
        switch (buttonType.toLowerCase()) {
            case "open wire":
                return idOpenWireButton;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the button type '%s'", buttonType));
        }
    }

    private static By getContentLocatorByType(String contentType, String expectedValue) throws Exception {
        switch (contentType.toLowerCase()) {
            case "reply name":
                return By.xpath(xpathStrQuickReplyNameByValue.apply(expectedValue));
            case "received message counter":
                return By.xpath(xpathStrQuickReplyCounterByValue.apply(expectedValue));
            case "received message":
                return By.xpath(xpathStrQuickReplyReceviedContent.apply(expectedValue));
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the content type '%s'", contentType));
        }
    }

}
