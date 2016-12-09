package com.wearezeta.auto.android.pages.details_overlay.common;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceDetailsPage extends AndroidPage {
    private static final By xpathSettingsTitle = By.xpath("//*[@id='action_bar_container' and .//*[@value='Settings']]");
    private static final By xpathDeviceId = By.xpath("//*[@id='summary']");

    private static final Function<String, String> xpathDeviceHeaderMatch =
            (expected) -> String.format("//*[@id='ttv__row__otr_header' and @value='%s']", expected);

    //region Switch
    private static final String VERIFIED_STATE = "Verified";
    private static final String NOT_VERIFIED_STATE = "Not verified";
    private static final String idStrSwitch = "os__single_otr_client__verify";
    private static final By xpathSingleOtrSwitch = By.xpath(String.format("//*[@id='%s']/*", idStrSwitch));
    private static final Function<String, String> xpathStrOtrSwitchByState = state ->
            String.format("//*[@id='%s']/*[@value='%s']", idStrSwitch, state);
    //endregion

    public DeviceDetailsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathSettingsTitle);
    }

    public boolean isHeaderTextVisible(String match) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath(xpathDeviceHeaderMatch.apply(match)));
    }

    public String getName() throws Exception {
        throw new NotImplementedException("not implemented");
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

    public boolean verifyDevice() throws Exception {
        boolean isAlreadyVerified = DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathStrOtrSwitchByState.apply(VERIFIED_STATE)), 1);
        if(!isAlreadyVerified) {
            final By unselectedSwitchLocator = By.xpath(xpathStrOtrSwitchByState.apply(NOT_VERIFIED_STATE));
            final Optional<WebElement> otrSwitch = getElementIfDisplayed(unselectedSwitchLocator, 3);
            if (otrSwitch.isPresent()) {
                //Need to tap on standalone switch, because tap on switch text do nothing
                getElement(xpathSingleOtrSwitch).click();
            }
            return otrSwitch.isPresent();
        }
        return true;
    }
}
