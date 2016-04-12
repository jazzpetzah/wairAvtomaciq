package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.function.Function;
import org.openqa.selenium.By;

public class CallIncomingPage extends AndroidPage {
    private static final Function<String, String> xpathIncomingCallContainerByName = name -> String
            .format("//*[@id='ttv__calling__header__subtitle' and @value='%s']" , name);

    public static final By idMainContent = By.id("iccv__incoming_call_controls");
    
    private static final Function<String, String> xpathCallingHeaderByName = name -> String
            .format("//*[@id='ttv__calling__header__name' and contains(@value, '%s')]", name);

    public CallIncomingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible(String subtitle) throws Exception {
        By by = By.xpath(xpathIncomingCallContainerByName.apply(subtitle));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), by, VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible(String subtitle) throws Exception {
        By by = By.xpath(xpathIncomingCallContainerByName.apply(subtitle));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), by);
    }
    
    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        final By locator = By.xpath(xpathCallingHeaderByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public void ignoreCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 85, 20, 85);
    }
    
    public void acceptCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 85, 80, 85);
    }

}
