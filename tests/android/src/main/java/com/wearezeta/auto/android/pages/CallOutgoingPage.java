package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

public class CallOutgoingPage extends CallingOverlayPage {

    private static final By xpathOutgoingVideoCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'Ringing') " +
                    "and //*[@id='ccbv__button_right']]");

    private static final By xpathOutgoingAudioCallContainer =
            By.xpath("//*[@id='ttv__calling__header__subtitle' and contains(@value, 'Ringing') " +
                    "and //*[@id='ccbv__button_middle']]");

    public CallOutgoingPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private static final int VISIBILITY_TIMEOUT_SECONDS = 20;

    public boolean waitUntilVisible(boolean isVideoCall) throws Exception {

        return isVideoCall ? DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOutgoingVideoCallContainer,
                VISIBILITY_TIMEOUT_SECONDS) : DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathOutgoingAudioCallContainer,
                VISIBILITY_TIMEOUT_SECONDS);
    }

    public boolean waitUntilNotVisible(boolean isVideoCall) throws Exception {
        return isVideoCall ? DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOutgoingVideoCallContainer)
                : DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathOutgoingAudioCallContainer);
    }
    
}
