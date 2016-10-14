package com.wearezeta.auto.win.pages.webapp;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class OptionsPage extends com.wearezeta.auto.web.pages.OptionsPage {
    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssImportAddressbookButton)
    private WebElement importAddressbookButton;

    public OptionsPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    public void clickImportAddressbookButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), importAddressbookButton);
        importAddressbookButton.click();
    }
}
