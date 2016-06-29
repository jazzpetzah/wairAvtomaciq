package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;


import com.wearezeta.auto.common.driver.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.NotSupportedException;
import org.openqa.selenium.By;

public class DeviceDetailPage extends AndroidPage {

    private static final By xpathSettingsTitle = By.xpath("//*[@id='action_bar_container' and .//*[@value='Settings']]");

    private static final By xpathDeviceId = By.xpath("//*[@id='summary']");
    
//    private static final By idDeviceHeader = By.id("ttv__row__otr_header");
    
    private static final Function<String,String> xpathDeviceHeaderMatch = 
            (expected) -> String.format("//*[@id='ttv__row__otr_header' and @value='%s']", expected);
    
    public DeviceDetailPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsTitle);
    }
    
    public boolean isHeaderTextVisible(String match) throws Exception{
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathDeviceHeaderMatch.apply(match)));
    }

    public String getName() throws Exception {
        throw new NotSupportedException("Not implemented yet");
    }

    /**
     * Id will be retrieved from summary, which will be separated into 2 parts by comma
     *
     * @return the device ID and the Activation place
     * @throws Exception
     */
    public String getId() throws Exception {
        final String summary = getElement(xpathDeviceId).getText();
        final Pattern p =
                Pattern.compile("ID:\\s+(\\w{2}\\s+\\w{2}\\s+\\w{2}\\s+\\w{2}\\s+\\w{2}\\s+\\w{2}\\s+\\w{2}\\s+\\w{2})");
        final Matcher m = p.matcher(summary);
        if (m.find()) {
            return m.group(1);
        }
        throw new IllegalStateException(String.format("Cannot parse id from device summary string '%s'", summary));
    }
}
