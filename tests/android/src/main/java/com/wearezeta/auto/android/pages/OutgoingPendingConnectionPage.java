package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class OutgoingPendingConnectionPage extends AndroidPage {
    private static final By idPageRoot = By.id("fl__pending_connect_request");

    private static final Function<String, String> xpathStrNameByText = text -> String
            .format("//*[@id='taet__participants__header' and @value='%s']", text);

    public OutgoingPendingConnectionPage(
            Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPageRoot);
    }

    public boolean waitUntilNameVisible(String expectedName) throws Exception {
        final By locator = By.xpath(xpathStrNameByText.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
}
