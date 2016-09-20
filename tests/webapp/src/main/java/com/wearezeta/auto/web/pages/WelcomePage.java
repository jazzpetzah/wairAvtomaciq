package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;

public class WelcomePage extends WebPage {

    private static final int TIMEOUT_UNSPLASH = 15; // seconds

    @FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput)
    private WebElement chooseYourOwnInput;

    @FindBy(how = How.CSS, using = WebAppLocators.SelfPictureUploadPage.cssKeepPictureButton)
    private WebElement keepPictureButton;

    public WelcomePage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void waitUntilNotVisible() throws Exception {
        assert DriverUtils
                .waitUntilLocatorDissapears(
                        this.getDriver(),
                        By.xpath(WebAppLocators.SelfPictureUploadPage.cssKeepPictureButton)) : "Keep picture button is still visible";
    }

    public void waitUntilButtonsAreClickable() throws Exception {
        assert DriverUtils.waitUntilElementClickable(this.getDriver(),
                keepPictureButton, TIMEOUT_UNSPLASH) : "Keep picture button was not clickable";
    }

    public void uploadPicture(String pictureName) throws Exception {
        final String picturePath = WebCommonUtils
                .getFullPicturePath(pictureName);
        chooseYourOwnInput.sendKeys(picturePath);
        if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
            // manually trigger change event on input until https://bugzilla.mozilla.org/show_bug.cgi?id=1280947 is fixed
            this.getDriver().executeScript("evt = new Event('change');arguments[0].dispatchEvent(evt);", chooseYourOwnInput);
        }
        // manually trigger change event for redirect
        if (WebAppExecutionContext.getBrowser().isSupportingAccessToJavascriptContext()) {
            this.getDriver().executeScript("e = $.Event('change');$(\""
                    + WebAppLocators.SelfPictureUploadPage.cssChooseYourOwnInput + "\").trigger(e);");
        } else {
            throw new Exception("Geckodriver is unable to access script context in Firefox < 48. See https://bugzilla.mozilla"
                    + ".org/show_bug.cgi?id=1123506");
        }
    }

    public void keepPicture() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                keepPictureButton, TIMEOUT_UNSPLASH) : String.format(
                        "Keep picture button not clickable after %s seconds",
                        TIMEOUT_UNSPLASH);
        keepPictureButton.click();
    }
}
