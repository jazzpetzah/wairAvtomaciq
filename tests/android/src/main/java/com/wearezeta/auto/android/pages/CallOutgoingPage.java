package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class CallOutgoingPage extends CallingOverlayPage {

    private static final By xpathOngoingCallContainer =
            By.xpath("//*[@id='ttv__calling__header__duration' and contains(@value, 'RINGING') and //*[@id='ccbv__calling_controls__hangup']]");
    
    private static final By idParticipants = By.id("chv__calling__participants_grid__chathead");

    public CallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingCallContainer);
    }

    public int getNumberOfParticipants() throws Exception {
        return getElements(idParticipants).size();
    }
    
}
