package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class TakeOverPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(TakeOverPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.TakeOverScreenPage.cssChooseYourOwnButton)
    private WebElement chooseYourOwnButton;

    @FindBy(css = WebAppLocators.TakeOverScreenPage.cssTakeThisOneButton)
    private WebElement takeThisOneButton;

    @FindBy(css = WebAppLocators.TakeOverScreenPage.cssTakeOverName)
    private WebElement takeOverName;

    @FindBy(css = WebAppLocators.TakeOverScreenPage.cssTakeOverUniqueUsername)
    private WebElement takeOverUniqueUsername;

    public TakeOverPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public String getUniqueUsername() throws Exception{
        return takeOverUniqueUsername.getText();
    }

    public String getName() throws Exception{
        return takeOverName.getText();
    }

    public boolean isTakeOverScreenVisible()throws Exception{
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(WebAppLocators.TakeOverScreenPage.idTakeOverScreen));
    }

    public boolean isTakeOverScreenNotVisible()throws Exception{
        return DriverUtils.waitUntilLocatorDissapears(getDriver(),
                By.id(WebAppLocators.TakeOverScreenPage.idTakeOverScreen));
    }

    public boolean isTakeThisOneButtonVisible()throws Exception{
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.TakeOverScreenPage.cssTakeThisOneButton));
    }

    public boolean isChooseYourOwnButtonVisible()throws Exception{
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.cssSelector(WebAppLocators.TakeOverScreenPage.cssChooseYourOwnButton));
    }

    public void clickChooseYourOwnButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), chooseYourOwnButton);
        chooseYourOwnButton.click();
    }

    public void clickTakeThisOneButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), takeThisOneButton);
        takeThisOneButton.click();
    }

}
