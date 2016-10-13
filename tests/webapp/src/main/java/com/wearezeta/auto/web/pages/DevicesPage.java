package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class DevicesPage extends WebPage {

    @FindBy(how = How.CSS, using = WebAppLocators.DevicesPage.cssCurrentDeviceId)
    private WebElement currentDeviceId;

    public DevicesPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getCurrentDeviceId() {
        return currentDeviceId.getText();
    }


}
