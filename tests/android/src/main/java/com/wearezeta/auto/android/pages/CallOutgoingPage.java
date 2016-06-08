package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class CallOutgoingPage extends CallingOverlayPage {

    private static final By xpathOngoingVideoCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'RINGING') " +
                    "and //*[@id='ccbv__calling_controls__hangup']]");

    private static final By xpathOngoingAudioCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'RINGING') " +
                    "and //*[@id='ccbv__calling_controls__hangup']]");

    public CallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible(boolean isVideoCall) throws Exception {

        return isVideoCall ? DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingVideoCallContainer,
                VISIBILITY_TIMEOUT_SECONDS) : DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOngoingAudioCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible(boolean isVideoCall) throws Exception {
        return isVideoCall ? DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingVideoCallContainer)
                : DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOngoingAudioCallContainer);
    }
    
}
