package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.Browser;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import java.io.File;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(AccountPage.class.getSimpleName());

    @FindBy(css = WebAppLocators.AccountPage.cssLogoutButton)
    private WebElement logoutButton;

    @FindBy(css = "[data-uie-name='go-logout']")
    private WebElement logoutInDialogButton;

    @FindBy(css = ".modal-logout .checkbox span")
    private WebElement clearDataCheckbox;

    @FindBy(css = WebAppLocators.AccountPage.cssSelfUserNameInput)
    private WebElement userNameInput;

    @FindBy(css = WebAppLocators.AccountPage.cssNameSelfUserMail)
    private WebElement userMail;

    @FindBy(css = WebAppLocators.AccountPage.cssNameSelfUserPhoneNumber)
    private WebElement userPhoneNumber;

    @FindBy(css = WebAppLocators.AccountPage.cssSelectPicture)
    private WebElement selectPictureInput;

    @FindBy(css = WebAppLocators.AccountPage.cssAccentColorPickerInputs)
    private List<WebElement> colorsInColorPicker;

    public AccountPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void logout() throws Exception {
        logoutButton.click();
    }

    public boolean isLogoutDialogShown() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), logoutButton);
    }

    public void checkClearDataInLogoutDialog() {
        if (!clearDataCheckbox.isSelected()) {
            clearDataCheckbox.click();
        }
    }

    public void logoutInLogoutDialog() {
        logoutInDialogButton.click();
    }

    public String getUserName() throws Exception {
        DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(
                WebAppLocators.AccountPage.cssSelfUserNameInput));
        return userNameInput.getAttribute("value");
    }

    public String getUserMail() {
        return userMail.getText();
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber.getText();
    }

    public void setUserName(String name) throws InterruptedException {
        userNameInput.click();
        Thread.sleep(1000);
        userNameInput.clear();
        userNameInput.sendKeys(name + "\n");
    }

    public void dropPicture(String pictureName) throws Exception {
        final String srcPicturePath = WebCommonUtils.getFullPicturePath(pictureName);
        assert new File(srcPicturePath).exists() : srcPicturePath + " file should exist on hub file system";

        /*
		 * The code below allows to upload the picture to the remote mode
		 * without Selenium interaction This could be useful when we have better
		 * solution for drag and drop
         */
        // final String dstPicturePathForScp = WebAppConstants.TMP_ROOT + "/"
        // + pictureName;
        // WebCommonUtils.putFileOnExecutionNode(this.getDriver().getNodeIp(),
        // srcPicturePath, dstPicturePathForScp);
        //
        // String dstPicturePath = null;
        // if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
        // dstPicturePath = WebAppConstants.WINDOWS_TMP_ROOT + "\\"
        // + pictureName;§
        // } else {
        // dstPicturePath = dstPicturePathForScp;
        // }
        // http://stackoverflow.com/questions/5188240/using-selenium-to-imitate-dragging-a-file-onto-an-upload-element
        final String inputId = "SelfImageUpload";
        this.getDriver().executeScript(
                inputId + " = window.$('<input id=\"" + inputId
                + "\"/>').attr({type:'file'}).appendTo('body');");
        // The file is expected to be uploaded automatically by Webdriver
        getDriver().findElement(By.id(inputId)).sendKeys(srcPicturePath);
        this.getDriver().executeScript(
                "e = $.Event('drop'); e.originalEvent = {dataTransfer : { files : "
                + inputId + ".get(0).files } }; $(\""
                + WebAppLocators.ProfilePicturePage.cssDropZone
                + "\").trigger(e);");
    }

    public void uploadPicture(String pictureName) throws Exception {
        final String picturePath = WebCommonUtils.getFullPicturePath(pictureName);
        if (WebAppExecutionContext.getBrowser() == Browser.Safari) {
            WebCommonUtils.sendPictureInSafari(picturePath, this.getDriver().getNodeIp());
        } else {
            selectPictureInput.sendKeys(picturePath);
        }
        if (WebAppExecutionContext.getBrowser() == Browser.Firefox) {
            // manually trigger change event on input
            this.getDriver().executeScript("evt = new Event('change');arguments[0].dispatchEvent(evt);", selectPictureInput);
        }
    }

    public AccentColor getCurrentAvatarAccentColor() throws Exception {
        final WebElement backgroundAvatarAccentColor = this.getDriver()
                .findElementByCssSelector(WebAppLocators.AccountPage.cssBackgroundAvatarAccentColor);
        return AccentColor.getByRgba(backgroundAvatarAccentColor.getCssValue("background-color"));
    }

    public String getCurrentAccentColor() throws Exception {
        final WebElement accentColorCircleDiv = this.getDriver()
                .findElementByCssSelector(WebAppLocators.AccountPage.cssCurrentAccentColorCircleDiv);
        return accentColorCircleDiv.getCssValue("border-top-color");
    }

    public int getCurrentAccentColorId() {
        int i = 1;
        for (WebElement colorInput : colorsInColorPicker) {
            final String checked = colorInput.getAttribute("checked");
            if (checked != null && checked.toLowerCase().contains("true")) {
                return i;
            }
            i++;
        }
        throw new RuntimeException("No accent color is selected in color picker");
    }

    public void selectAccentColor(String colorName) throws Exception {
        final int id = AccentColor.getByName(colorName).getId();
        final String cssAccentColorDiv = WebAppLocators.AccountPage.cssAccentColorDivById.apply(id);
        assert DriverUtils.waitUntilLocatorAppears(this.getDriver(), By.cssSelector(cssAccentColorDiv));
        final WebElement accentColorDiv = this.getDriver().findElementByCssSelector(cssAccentColorDiv);
        assert DriverUtils.waitUntilElementClickable(this.getDriver(), accentColorDiv);
        accentColorDiv.click();
    }

}
