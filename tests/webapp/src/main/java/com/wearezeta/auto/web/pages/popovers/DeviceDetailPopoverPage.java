package com.wearezeta.auto.web.pages.popovers;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DeviceDetailPopoverPage extends AbstractPopoverContainer {

    @FindBy(how = How.CSS, using = PopoverLocators.DeviceDetailPopoverPage.cssDeviceIds)
    private WebElement firstDevice;

    @FindBy(how = How.CSS, using = PopoverLocators.DeviceDetailPopoverPage.cssDeviceIds)
    private List<WebElement> deviceIds;

    @FindBy(how = How.CSS, using = ".user-profile-device-detail [data-uie-name='do-back']")
    private WebElement backButton;

    @FindBy(how = How.CSS, using = ".user-profile-device-detail [data-uie-name='device-id']")
    private WebElement deviceId;

    @FindBy(how = How.CSS, using = ".user-profile-device-detail .user-profile-device-detail-fingerprint")
    private WebElement fingerprint;

    @FindBy(how = How.CSS, using = ".slider")
    private WebElement verificationToggle;

    public DeviceDetailPopoverPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    protected String getXpathLocator() {
        return "";
    }

    public List<String> getDeviceIds() {
        return deviceIds.stream().map(w -> w.getText().toLowerCase()).collect(Collectors.toList());
    }

    public boolean waitForDevices() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
    }

    public void clickDevice(String deviceId) throws Exception {
        String locator = PopoverLocators.DeviceDetailPopoverPage.cssDeviceById.apply(deviceId);
        this.getDriver().findElement(By.cssSelector(locator)).click();
    }

    public String getDeviceId() {
        return deviceId.getText().toLowerCase();
    }

    public String getFingerPrint() {
        return fingerprint.getText();
    }

    public void verifyDevice() {
        verificationToggle.click();
    }

    public void clickBackButton() {
        backButton.click();
    }
}
