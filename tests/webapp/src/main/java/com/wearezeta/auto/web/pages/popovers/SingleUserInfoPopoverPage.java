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
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.PopoverLocators;
import org.apache.log4j.Logger;

class SingleUserInfoPopoverPage extends AbstractUserInfoPopoverPage {

    public static final Logger log = ZetaLogger
            .getLog(SingleUserInfoPopoverPage.class.getSimpleName());

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
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(
                PopoverLocators.SingleUserPopover.SingleUserInfoPage.cssUserVerifiedIcon));
    }

    public List<String> getDeviceIds() {
        return deviceIds.stream().map(w -> w.getText().toLowerCase()).collect(Collectors.toList());
    }

    public List<String> getVerifiedDeviceIds() throws Exception {
        final By useElement = By.xpath(".//*[local-name()='use']");
        final By deviceIdElement = By.cssSelector("[data-uie-name='device-id']");
        return devices.stream()
                .filter(w -> "user-device-verified".equals(w.findElement(useElement).getAttribute("data-uie-name")))
                .filter((w) -> {
                    log.info(w.getText());
                    return true;
                })
                .map(w -> w.findElement(deviceIdElement).getText())
                .filter((did) -> {
                    log.info(did);
                    return true;
                })
                .collect(Collectors.toList());
    }

    public boolean waitForDevices() throws Exception {
        // Unfortunately there is no other workaround than waiting for 1 second
        Thread.sleep(1000);
        return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
    }

    public void clickDevice(String deviceId) throws Exception {
        waitForDevices();
        String locator = PopoverLocators.DeviceDetailPopoverPage.cssDeviceById.apply(deviceId);
        this.getDriver().findElement(By.cssSelector(locator)).click();
    }
}
