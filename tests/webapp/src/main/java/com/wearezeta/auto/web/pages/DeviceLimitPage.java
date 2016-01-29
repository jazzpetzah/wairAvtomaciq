package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class DeviceLimitPage extends WebPage {

	@FindBy(css = "#section-limit")
	WebElement deviceLimitInfo;

	@FindBy(css = "[data-uie-name='do-manage-devices']")
	WebElement manageDevicesButton;

	@FindBy(css = "[data-uie-name='go-sign-out']")
	WebElement signOutButton;

	@FindBy(css = "[data-uie-name='device-header-model']")
	List<WebElement> deviceNames;

	public DeviceLimitPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void clickManageDevicesButton() {
		manageDevicesButton.click();
	}

	public void clickSignOutButton() {
		signOutButton.click();
	}

	public boolean isDeviceLimitInfoShown() throws Exception {
		return DriverUtils.waitUntilElementClickable(getDriver(),
				deviceLimitInfo);
	}

	public List<String> getDevicesNames() {
		return deviceNames.stream()
				.map(a -> a.getText())
				.collect(Collectors.toList());
	}
}
