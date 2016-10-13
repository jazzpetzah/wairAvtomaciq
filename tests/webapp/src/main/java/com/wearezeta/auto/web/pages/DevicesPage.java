package com.wearezeta.auto.web.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DevicesPage extends WebPage {

    @FindBy(css = WebAppLocators.DevicesPage.cssCurrentDeviceId)
    private WebElement currentDeviceId;

    @FindBy(css = WebAppLocators.DevicesPage.cssActiveDeviceIds)
    private WebElement firstDevice;

    @FindBy(css = WebAppLocators.DevicesPage.cssActiveDevicesLabels)
    private List<WebElement> activeDevicesLabels;

    public DevicesPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getCurrentDeviceId() {
        return currentDeviceId.getText();
    }

    public boolean waitForDevices() throws Exception {
        // Unfortunately there is no other workaround than waiting for 1 second
        Thread.sleep(1000);
        return DriverUtils.waitUntilElementClickable(this.getDriver(), firstDevice);
    }

    public List<String> getActiveDevicesLabels() {
        return activeDevicesLabels.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public void clickDevice(String device) throws Exception {
        final String locator = WebAppLocators.DevicesPage.xpathDeviceLabel.apply(device);
        DriverUtils.waitUntilElementClickable(getDriver(), getDriver().findElement(By.xpath(locator)));
        getDriver().findElement(By.xpath(locator)).click();
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
