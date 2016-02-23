package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.function.Function;
import org.openqa.selenium.By;

public class CallOngoingVideoPage extends CallingOverlayPage {

    private static final By xpathOngoingCallContainer =
            By.xpath("//*[@id='spv__self_preview']");

    public CallOngoingVideoPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
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

    @Override
    public void toggleVideo() throws Exception {
        // TODO click somewhere to hover in the call controls
        super.toggleVideo();
    }

    @Override
    public void hangup() throws Exception {
        // TODO click somewhere to hover in the call controls
        super.hangup();
    }

    @Override
    public void toggleMute() throws Exception {
        // TODO click somewhere to hover in the call controls
        super.toggleMute();
    }
}
