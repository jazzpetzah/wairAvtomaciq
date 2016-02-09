package com.wearezeta.auto.web.pages.external;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DeleteAccountPage extends WebPage {

    @FindBy(css = ExternalLocators.DeleteAccountPage.cssSubmitButton)
    private WebElement submitButton;
    
    private static final String ERROR_TEXT = "Something went wrong.";

    public DeleteAccountPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    @Override
    public void setUrl(String url) {
        super.setUrl(url);
    }

    public void clickDeleteAccountButton() {
        submitButton.click();
    }

    public boolean isSuccess() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.cssSelector(ExternalLocators.DeleteAccountPage.cssSuccess));
    }
    
    public boolean isWrongKey() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.cssSelector(ExternalLocators.DeleteAccountPage.cssWrongKey));
    }
    
    public boolean isWrongCode() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.cssSelector(ExternalLocators.DeleteAccountPage.cssWrongCode));
    }
    
    public boolean isButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.cssSelector(ExternalLocators.DeleteAccountPage.cssSubmitButton));
    }
    
    public boolean isErrorMessage() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(getDriver(), By.xpath(ExternalLocators.DeleteAccountPage.xpathLabelByText.apply(ERROR_TEXT)));
    }
}
