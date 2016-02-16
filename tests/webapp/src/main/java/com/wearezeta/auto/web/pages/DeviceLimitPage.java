package com.wearezeta.auto.web.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;

public class DeviceLimitPage extends WebPage {

    private final Function<String, String> cssGoRemoveLocator = deviceName -> String.format
            ("[data-uie-name='go-remove-device'][data-uie-value='%s']", deviceName);

    private final Function<String, String> cssPwdFieldLocator = deviceName -> String.format
            (".device-remove-form[data-uie-value='%s'] [data-uie-name='remove-device-password']", deviceName);

    private final Function<String, String> cssDoRemoveLocator = deviceName -> String.format
            (".device-remove-form[data-uie-value='%s'] [data-uie-name='do-remove-device']", deviceName);

    private final Function<String, String> cssCancelRemoveLocator = deviceName -> String.format
            (".device-remove-form[data-uie-value='%s'] [data-uie-name='remove-device-cancel']", deviceName);

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

    public void clickRemoveDeviceButton(String deviceName) throws Exception {
        this.getDriver().findElement(By.cssSelector(cssGoRemoveLocator.apply(deviceName))).click();
    }

    public void enterPassword(String deviceName, String password) throws Exception {
        WebElement field = this.getDriver().findElement(By.cssSelector(cssPwdFieldLocator.apply(deviceName)));
        field.clear();
        field.sendKeys(password);
    }

    public void clickRemoveButton(String deviceName) throws Exception {
        this.getDriver().findElement(By.cssSelector(cssDoRemoveLocator.apply(deviceName))).click();
    }

    public void clickCancelButton(String deviceName) throws Exception {
        this.getDriver().findElement(By.cssSelector(cssCancelRemoveLocator.apply(deviceName))).click();
    }
}
