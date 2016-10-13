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

    @FindBy(how = How.CSS, using = WebAppLocators.SettingsPage.cssConfirmText)
    private WebElement confirmText;

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

    public String getDeleteInfo() throws Exception {
        return confirmText.getText();
    }
}
