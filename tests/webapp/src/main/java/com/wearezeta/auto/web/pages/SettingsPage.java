package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class SettingsPage extends WebPage {

    private static final int SLIDER_CIRCLE_SIZE = 20;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSettingsCloseButton)
    private WebElement settingsCloseButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssSoundAlertsLevel)
    private WebElement soundAlertsLevel;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeviceLabels)
    private List<WebElement> deviceLabels;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssConfirmText)
    private WebElement confirmText;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssBackButton)
    private WebElement backButton;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssVerificationToggle)
    private WebElement verificationToggle;

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssDeviceIds)
    private WebElement firstDevice;

    public SettingsPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        final String xpath = WebAppLocators.SettingsPage.xpathSettingsDialogRoot;
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(xpath));
    }

    public void clickCloseButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), settingsCloseButton);
        settingsCloseButton.click();
    }

    public List<String> getDeviceLabels() {
        return deviceLabels.stream().map(w -> w.getText())
                .collect(Collectors.toList());
    }

    public void clickDevice(String device) throws Exception {
        final String locator = WebAppLocators.SettingsPage.xpathDeviceLabel
                .apply(device);
        DriverUtils.waitUntilElementClickable(getDriver(), getDriver().findElement(By.xpath(locator)));
        getDriver().findElement(By.xpath(locator)).click();
    }

    public String getDeleteInfo() throws Exception {
        return confirmText.getText();
    }

    public void clickBackButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), backButton);
        backButton.click();
    }

    public void verifyDevice() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), verificationToggle);
        verificationToggle.click();
    }

    public boolean waitForDevices() throws Exception {
        // Unfortunately there is no other workaround than waiting for 1 second
        Thread.sleep(1000);
        return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
    }

    public List<String> getVerifiedDeviceIds() throws Exception {
        final By useElement = By.xpath(".//*[local-name()='use']");
        List<WebElement> deviceList = getDriver().findElements(useElement);
        List<String> idList = new ArrayList<>();

        for (int i = 0; i < deviceList.size(); i++) {
            if ("user-device-verified".equals(deviceList.get(i).getAttribute("data-uie-name"))) {
                WebElement parent = deviceList.get(i).findElement(By.xpath("parent::*"));
                idList.add(parent.getAttribute("data-uie-value").toUpperCase());
            }
        }
        return idList;
    }
}
