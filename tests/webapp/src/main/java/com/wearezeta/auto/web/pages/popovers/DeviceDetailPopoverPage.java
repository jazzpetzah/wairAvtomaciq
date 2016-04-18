package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DeviceDetailPopoverPage extends AbstractPopoverContainer {

    @FindBy(how = How.CSS, using = ".user-profile-device-detail [data-uie-name='do-back']")
    private WebElement backButton;

    @FindBy(how = How.CSS, using = ".user-profile-device-detail [data-uie-name='device-id']")
    private WebElement deviceId;

    @FindBy(how = How.CSS, using = ".user-profile-device-detail .user-profile-device-detail-fingerprint")
    private WebElement fingerprint;

    @FindBy(how = How.CSS, using = ".slider")
    private WebElement verificationToggle;

    @FindBy(how = How.CSS, using = ".user-profile-device-reset span")
    private WebElement resetSession;

    public DeviceDetailPopoverPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected String getXpathLocator() {
        return "";
    }

    public String getDeviceId() {
        return deviceId.getText().toLowerCase();
    }

    public String getFingerPrint() {
        return fingerprint.getText();
    }

    public void verifyDevice() throws Exception {
        assert(DriverUtils.waitUntilElementClickable(this.getDriver(),
                verificationToggle)): "Verify button not clickable";
        verificationToggle.click();
    }

    public void clickBackButton() {
        backButton.click();
    }

    public void clickResetSession() {
        resetSession.click();
    }
}
