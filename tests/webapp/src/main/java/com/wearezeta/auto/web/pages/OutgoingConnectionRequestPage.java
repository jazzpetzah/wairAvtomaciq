package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.WebAppLocators;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class OutgoingConnectionRequestPage extends WebPage {

    @FindBy(css = WebAppLocators.OutgoingRequestPage.cssCancelRequestButton)
    private WebElement cancelRequestButton;

    public OutgoingConnectionRequestPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isCancelRequestButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(
                WebAppLocators.OutgoingRequestPage.cssCancelRequestButton));
    }

    public void clickCancelPendingRequestButton() throws Exception {
        cancelRequestButton.click();
    }

    public String getCommonFriends() throws Exception {
        String css = WebAppLocators.OutgoingRequestPage.cssCommonFriends;
        return getDriver().findElement(By.cssSelector(css)).getText();
    }
}
