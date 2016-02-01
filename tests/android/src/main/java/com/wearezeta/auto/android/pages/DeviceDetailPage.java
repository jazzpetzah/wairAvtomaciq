package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;


import com.wearezeta.auto.common.driver.*;
import javax.ws.rs.NotSupportedException;
import org.openqa.selenium.By;

public class DeviceDetailPage extends AndroidPage {

    private static final By xpathSettingsTitle = By.xpath("//*[@id='action_bar_container' and .//*[@value='Settings']]");

    private static final By xpathDeviceId = By.xpath("//*[@id='summary']");

    public DeviceDetailPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsTitle);
    }

    public String getName() throws Exception {
        throw new NotSupportedException("Not implemented yet");
    }

    public String getModel() throws Exception {
        return getElement(xpathDeviceId).getText().replaceAll("(\\s\\nID:.*$)", "").toLowerCase();
    }

    public String getId() throws Exception {
        return getElement(xpathDeviceId).getText().replaceAll("(^.*\\nID:)|(\\n(.*)$)|(\\s)", "").toLowerCase();
    }

    public String getActivationInfo() throws Exception {
        return getElement(xpathDeviceId).getText().replaceAll("(^.*\\nID:.*\\n)", "").toLowerCase();
    }

    public String getFingerprint() throws Exception {
        throw new NotSupportedException("Not implemented yet");
    }
    
}
