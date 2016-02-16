package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TakeoverPage extends AndroidPage {

    private static final By idTakeoverScreen = By.id("cm__confirm_action_light");
    private static final By xpathTakeoverScreenText = By
        .xpath("//*[@id='text' and @value='Do you still want to send your message?']");
    private static final By idTakeoverCloseBtn = By.id("cancel");
    private static final By idTakeoverHeader = By.id("header");
    private static final By idTakeoverSendAnywayBtn = By.id("positive");
    private static final By idTakeoverShowBtn = By.id("negative");

    public TakeoverPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitForTakeoverScreenVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idTakeoverScreen);
    }

    public boolean waitForTakeoverScreenInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idTakeoverScreen);
    }

    public void tapCloseBtn() throws Exception {
        getElement(idTakeoverCloseBtn, "Close button is not present").click();
    }

    public String getHeaderText() throws Exception {
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idTakeoverHeader)) {
            throw new Exception("Header is not present");
        }
        return getElement(idTakeoverHeader).getText();
    }

    public boolean isTakeoverScreenTextCorrect() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), getElement(xpathTakeoverScreenText));
    }

    public void tapShowBtn() throws Exception {
        getElement(idTakeoverShowBtn, "Show Device/People button is not visible").click();
    }

    public void tapSendAnywayBtn() throws Exception {
        getElement(idTakeoverSendAnywayBtn, "Send Anyway button is not visible").click();
    }
}
