package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;


import com.wearezeta.auto.common.driver.*;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.ws.rs.NotSupportedException;
import org.openqa.selenium.By;

public class DeviceDetailPage extends AndroidPage {

    private static final By xpathSettingsTitle = By.xpath("//*[@id='action_bar_container' and .//*[@value='Settings']]");

    private static final By xpathDeviceId = By.xpath("//*[@id='summary']");
    
    private static final By idDeviceHeader = By.id("ttv__row__otr_header");
    
    private static final Function<String,By> xpathDeviceHeaderMatch = (expected) -> By.xpath(String.format("//*[@id='ttv__row__otr_header' and @value='%s']", expected));
    
    public DeviceDetailPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsTitle);
    }
    
    public boolean isHeaderTextVisible(String match) throws Exception{
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathDeviceHeaderMatch.apply(match));
    }
    
    public String getHeaderText() throws Exception {
        return getElementIfDisplayed(idDeviceHeader).orElseThrow(
                ()->new Exception("Header text is not present on the device detail page")
        ).getText();
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
