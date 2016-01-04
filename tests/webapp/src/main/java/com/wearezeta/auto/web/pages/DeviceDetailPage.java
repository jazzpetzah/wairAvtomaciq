package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class DeviceDetailPage extends WebPage {

	@FindBy(css = ".self-settings-device-details .device-info .label-xs")
	WebElement deviceLabel;

	@FindBy(css = ".self-settings-device-details [data-uie-name='go-password-reset']")
	WebElement removeDeviceLink;

	@FindBy(css = ".self-settings-device-details .device-remove-input")
	WebElement passwordInput;

	@FindBy(css = ".self-settings-device-details .device-remove-button-remove")
	WebElement removeButton;

	public DeviceDetailPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
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

}
