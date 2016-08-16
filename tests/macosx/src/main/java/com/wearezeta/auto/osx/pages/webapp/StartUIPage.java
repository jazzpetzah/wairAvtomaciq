package com.wearezeta.auto.osx.pages.webapp;

import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class StartUIPage extends com.wearezeta.auto.web.pages.StartUIPage {
    @FindBy(how = How.CSS, using = WebAppLocators.StartUIPage.cssBringFriendsFromContactsButton)
    private WebElement importAddressbookButton;

    

    public StartUIPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }
    
    public void clickImportAddressbookButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), importAddressbookButton);
        importAddressbookButton.click();
    }
}
