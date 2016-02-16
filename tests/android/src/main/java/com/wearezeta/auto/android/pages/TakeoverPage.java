package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TakeoverPage extends AndroidPage {

    private static final By idTakeoverScreen = By.id("ll__confirmation_dialog__message_container");

    private static final By xpathTakeoverScreenText = By
        .xpath("//*[@id='text' and contains(@value,'Do you still want to send your message?')]");

    public static final By xpathTakeoverScreenHeader = By
        .xpath("//*[@id='header' and contains(@value, 'started using a new device.')]");

    private static final By idTakeoverSendAnywayBnt = By.id("positive");

    private static final By idTakeoverShowDeviceBnt = By.id("negative");

    public TakeoverPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitForTakeoverScreenVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idTakeoverScreen);
    }

    public boolean isTakeoverScreenHeaderCorrect(String name) throws Exception {
        final String headerText = getElement(xpathTakeoverScreenHeader, "No takeover header is found").getText();
        return headerText.toLowerCase().contains(name.toLowerCase());
    }

    public boolean isTakeoverScreenTextCorrect() throws Exception {
        return DriverUtils.isElementPresentAndDisplayed(getDriver(), getElement(xpathTakeoverScreenText));
    }

    public void tapShowDeviceBnt() throws Exception {
        getElement(idTakeoverShowDeviceBnt, "Show Device button is not visible").click();
    }

    public void tapSendAnywayBnt() throws Exception {
        getElement(idTakeoverSendAnywayBnt, "Send Anyway button is not visible").click();
    }
}
