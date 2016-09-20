package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class DeviceDetailPage extends WebPage {

    @FindBy(css = ".self-settings-section header div")
    private WebElement deviceName;

    @FindBy(css = ".self-settings-section .device-info .label-xs")
    private WebElement deviceLabel;

    @FindBy(css = ".self-settings-section [data-uie-name='go-remove-device']")
    private WebElement removeDeviceLink;

    @FindBy(css = ".self-settings-section [data-uie-name='remove-device-password']")
    private WebElement passwordInput;

    @FindBy(css = ".self-settings-section [data-uie-name='do-remove-device']")
    private WebElement removeButton;

    @FindBy(css = ".self-settings-section .self-settings-reset-session-button-label")
    private WebElement resetSessionButton;

    public DeviceDetailPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getDeviceName() {
        return deviceName.getText();
    }

    public String getDeviceLabel() {
        return deviceLabel.getText();
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
