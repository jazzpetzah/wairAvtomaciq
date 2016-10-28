package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.support.How;

public class DeviceDetailPage extends WebPage {

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='go-back']")
    private WebElement backButton;

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='device-model']")
    private WebElement deviceModel;

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='device-id']")
    private WebElement deviceId;

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='do-verify']")
    private WebElement verificationToggle;

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='go-remove-device']")
    private WebElement removeDeviceLink;

    @FindBy(css = "[data-uie-name='modal-remove-device'] input")
    private WebElement passwordInput;

    @FindBy(css = "[data-uie-name='modal-remove-device'] [data-uie-name='do-delete']")
    private WebElement removeButton;

    @FindBy(css = "[data-uie-name='preferences-devices-details'] [data-uie-name='do-session-reset']")
    private WebElement resetSessionButton;

    public DeviceDetailPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickBackButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), backButton);
        backButton.click();
    }

    public String getDeviceName() {
        return deviceModel.getText();
    }

    public String getDeviceId() {
        return deviceId.getText();
    }

    public void verifyDevice() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), verificationToggle);
        verificationToggle.click();
    }

    public void clickRemoveDeviceLink() {
        removeDeviceLink.click();
    }

    public void setPassword(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickRemoveButton() {
        removeButton.click();
    }

    public void clickResetSessionButton() {
        resetSessionButton.click();
    }
}
