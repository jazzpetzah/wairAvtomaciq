package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.function.Function;
import org.openqa.selenium.By;

public class CallIncomingPage extends AndroidPage {

    private static final String idStrCallingContainer = "tcfl__calling__container";
    private static final By idCallingContainer = By.id(idStrCallingContainer);
    private static final By xpathIncomingCallContainer = By.xpath("//*[@id='ttv__calling__header__duration' and contains(@value, 'Calling')]");
    private static final By idMainContent = By.id("cpv__calling__participants");
    
    private static final Function<String, String> xpathCallingHeaderByName = name -> String
            .format("//*[@id='ttv__calling__header__name' and contains(@value, '%s')]",
                    name);

    public CallIncomingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathIncomingCallContainer, VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathIncomingCallContainer);
    }
    
    public boolean waitUntilNameAppearsOnCallingBarCaption(String name) throws Exception {
        final By locator = By.xpath(xpathCallingHeaderByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }
    
    public void ignoreCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 95, 20, 95);
    }
    
    public void acceptCall() throws Exception {
        DriverUtils.swipeElementPointToPoint(getDriver(), getElement(idMainContent), 1500, 50, 95, 80, 95);
    }

}