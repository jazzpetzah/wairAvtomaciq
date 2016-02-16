package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.misc.Interfaces;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class VideoCallPage extends IOSPage {

    private static final By nameCallingUserImage = By.name("CallingTopUsersImage");

    private static final By nameCallStatusLabel = By.name("CallStatusLabel");

    private static final Function<String, String> xpathStrCallStatusLabel = name -> String.format("//UIAStaticText[@name='CallStatusLabel' and @value='%s RINGING']", name);

    private static final By nameMuteButton = By.name("CallMuteButton");

    private static final By nameLeaveCallButton = By.name("LeaveCallButton");

    private static final By nameAcceptCallButton = By.name("AcceptButton");

    private static final Interfaces.FunctionFor2Parameters<String, String, String> xpathStrButtonByNameAndVisibility = (name, visibility) ->
            String.format("//UIAButton[@name='%sButton' and @visible='%s']", name, visibility);

    public VideoCallPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isRingingToUserLabelShown(String username) throws Exception {
        By locator = By.xpath(xpathStrCallStatusLabel.apply(username));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public boolean isButtonWithPointedVisibilityShown(String buttonName, String visibility) throws Exception {
        By locator = By.xpath(xpathStrButtonByNameAndVisibility.apply(buttonName, visibility));
        return DriverUtils.waitUntilLocatorAppears(getDriver(), locator);
    }

    public void clickHangUpButton() throws Exception {
        getElement(nameLeaveCallButton).click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), nameLeaveCallButton)) {
            throw new IllegalStateException("Hang Up button is still visible");
        }
    }
}
