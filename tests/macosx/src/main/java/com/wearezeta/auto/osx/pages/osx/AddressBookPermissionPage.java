package com.wearezeta.auto.osx.pages.osx;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import static com.wearezeta.auto.osx.common.OSXConstants.Scripts.ADDRESSBOOK_PERMISSION_SCRIPT;
import com.wearezeta.auto.osx.locators.OSXLocators;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AddressBookPermissionPage extends OSXPage {

    @FindBy(how = How.XPATH, using = OSXLocators.AddressBookPermissionPage.xpathWindow)
    private WebElement window;

    @FindBy(how = How.XPATH, using = OSXLocators.AddressBookPermissionPage.xpathOkButton)
    private WebElement okButton;

    @FindBy(how = How.XPATH, using = OSXLocators.AddressBookPermissionPage.xpathNoButton)
    private WebElement noButton;

    public AddressBookPermissionPage(Future<ZetaOSXDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isVisible() throws Exception {
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(OSXLocators.AddressBookPermissionPage.xpathWindow));
    }

    public boolean isNotVisible() throws Exception {
        // TODO: should be waitUntilLocatorDissapears but that's broken with Appium4Mac
        return !DriverUtils.waitUntilLocatorAppears(this.getDriver(),
                By.xpath(OSXLocators.AddressBookPermissionPage.xpathWindow));
    }

    public void clickOk() throws Exception {
        String script = String.format(new String(Files.readAllBytes(Paths.get(getClass()
                .getResource(ADDRESSBOOK_PERMISSION_SCRIPT).toURI()))), "OK");
        getDriver().executeScript(script);
    }

    public void clickNo() throws Exception {
        String script = String.format(new String(Files.readAllBytes(Paths.get(getClass()
                .getResource(ADDRESSBOOK_PERMISSION_SCRIPT).toURI()))), "Don't Allow");
        getDriver().executeScript(script);
    }

}
