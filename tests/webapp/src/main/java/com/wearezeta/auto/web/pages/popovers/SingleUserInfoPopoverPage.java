package com.wearezeta.auto.web.pages.popovers;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton)
	private WebElement blockButton;
	
	@FindBy(how = How.XPATH, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathUnblockButton)
	private WebElement unblockButton;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDevicesTab)
	private WebElement devicesTab;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDetailsTab)
	private WebElement detailsTab;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDevicesText)
	private WebElement devicesText;

	@FindBy(how = How.CSS, using = PopoverLocators.DeviceDetailPopoverPage.cssDeviceIds)
	private WebElement firstDevice;

	@FindBy(how = How.CSS, using = PopoverLocators.DeviceDetailPopoverPage.cssDeviceIds)
	private List<WebElement> deviceIds;

	@FindBy(how = How.CSS, using = PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssDevices)
	private List<WebElement> devices;

	public SingleUserInfoPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathBlockButton;
	}

	private WebElement getAddButtonElement() throws Exception {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public boolean isAddButtonVisible() throws Exception {
		return getAddButtonElement().isDisplayed();
	}

	public boolean isBlockButtonVisible() {
		return blockButton.isDisplayed();
	}
	
	public boolean isUnblockButtonVisible() {
		return unblockButton.isDisplayed();
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddButtonElement());
		getAddButtonElement().click();
	}

	public void clickBlockButton() {
		blockButton.click();	
	}
	public void clickUnblockButton() {
		unblockButton.click();
	}

	public void switchToDevicesTab() {
		devicesTab.click();
	}
	public void switchToDetailsTab() {
		detailsTab.click();
	}

	public String getDevicesText() {
		return devicesText.getText();
	}

	public boolean isUserVerified() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(PopoverLocators.SingleUserPopover
				.SingleUserInfoPage.cssUserVerifiedIcon));
	}

	public List<String> getDeviceIds() {
		return deviceIds.stream().map(w -> w.getText().toLowerCase()).collect(Collectors.toList());
	}

	public List<String> getVerifiedDeviceIds() {
		return deviceIds.stream().filter(w -> "#icon-verified".equals(w.findElement(By.xpath
				("//*[@class='user-profile-device']//*[local-name() = 'use']")).getAttribute("href"))).map(w -> w.findElement
				(By.cssSelector("[data-uie-name='device-id']")).getText()).collect
				(Collectors.toList());
	}

	public boolean waitForDevices() throws Exception {
		return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
	}

	public void clickDevice(String deviceId) throws Exception {
		String locator = PopoverLocators.DeviceDetailPopoverPage.cssDeviceById.apply(deviceId);
		this.getDriver().findElement(By.cssSelector(locator)).click();
	}
}
