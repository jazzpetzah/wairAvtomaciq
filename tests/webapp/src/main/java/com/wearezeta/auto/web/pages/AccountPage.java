package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.driver.DriverUtils;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
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
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

public class AccountPage extends WebPage {

    @SuppressWarnings("unused")
    private static final Logger log = ZetaLogger.getLog(AccountPage.class.getSimpleName());

    private String rememberedUniqueUsername;

    @FindBy(css = WebAppLocators.AccountPage.cssLogoutButton)
    private WebElement logoutButton;

    @FindBy(css = "[data-uie-name='go-logout']")
    private WebElement logoutInDialogButton;

    @FindBy(css = ".modal-logout .checkbox span")
    private WebElement clearDataCheckbox;

    @FindBy(css = WebAppLocators.AccountPage.cssSelfNameInput)
    private WebElement nameInput;

    @FindBy(css = WebAppLocators.AccountPage.cssUniqueUsername)
    private WebElement uniqueUsernameInput;

    @FindBy(css = WebAppLocators.AccountPage.cssUniqueUsernameError)
    private WebElement uniqueUsernameError;

    @FindBy(css = WebAppLocators.AccountPage.cssUniqueUsernameHint)
    private WebElement uniqueUsernameHint;

    @FindBy(css = WebAppLocators.AccountPage.cssNameSelfUserMail)
    private WebElement userMail;

    @FindBy(css = WebAppLocators.AccountPage.cssNameSelfUserPhoneNumber)
    private WebElement userPhoneNumber;

    @FindBy(css = WebAppLocators.AccountPage.cssPicture)
    private WebElement picture;

    @FindBy(css = WebAppLocators.AccountPage.cssSelectPicture)
    private WebElement selectPictureInput;

    @FindBy(css = WebAppLocators.AccountPage.cssAccentColorPickerInputs)
    private List<WebElement> colorsInColorPicker;

    @FindBy(css = WebAppLocators.AccountPage.cssDeleteAccountButton)
    private WebElement deleteAccountButton;

    @FindBy(css = WebAppLocators.AccountPage.cssCancelDeleteAccountButton)
    private WebElement cancelDeleteAccountButton;

    @FindBy(css = WebAppLocators.AccountPage.cssConfirmDeleteAccountButton)
    private WebElement confirmDeleteAccountButton;

    public AccountPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void logout() throws Exception {
        logoutButton.click();
    }

    // Wrapper only
    public boolean isLogoutInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                By.cssSelector(WebAppLocators.AccountPage.cssLogoutButton));
    }

    public boolean isLogoutDialogShown() throws Exception {
        return DriverUtils.waitUntilElementClickable(this.getDriver(), logoutInDialogButton);
    }

    public void checkClearDataInLogoutDialog() {
        if (!clearDataCheckbox.isSelected()) {
            clearDataCheckbox.click();
        }
    }

    public void logoutInLogoutDialog() {
        logoutInDialogButton.click();
    }

    public String getName() throws Exception {
        DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), By.cssSelector(
                WebAppLocators.AccountPage.cssSelfNameInput));
        return nameInput.getAttribute("value");
    }

    public String getUserMail() {
        return userMail.getText();
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber.getText();
    }

    public void setName(String name) throws InterruptedException {
        nameInput.click();
        Thread.sleep(1000);
        nameInput.clear();
        nameInput.sendKeys(name);
        nameInput.sendKeys(Keys.ENTER);
    }

    public void typeUniqueUsername(String name) throws InterruptedException {
        uniqueUsernameInput.click();
        Thread.sleep(1000);
        uniqueUsernameInput.clear();
        uniqueUsernameInput.sendKeys(name);
    }

    public void submitUniqueUsername() throws InterruptedException {
        uniqueUsernameInput.sendKeys(Keys.ENTER);
    }

    public void setUniqueUsername(String name) throws InterruptedException {
        typeUniqueUsername(name);
        submitUniqueUsername();
    }

    public String getUniqueUsernameError() {
        return uniqueUsernameError.getText();
    }

    public String getUniqueUsernameHint() throws Exception {
        return uniqueUsernameHint.getText();
    }

    public String getUniqueUsername() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), uniqueUsernameInput);
        return uniqueUsernameInput.getAttribute("value");
    }

    public BufferedImage getPicture() throws Exception {
        Optional<BufferedImage> screenshot = getElementScreenshot(picture);
        if (!screenshot.isPresent()) {
            throw new Exception("Could not get screenshot of contact list with background");
        }
        return screenshot.get();
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
        for (WebElement colorInput : colorsInColorPicker) {
            final String checked = colorInput.getAttribute("checked");
            if (checked != null && checked.toLowerCase().contains("true")) {
                String id = colorInput.getAttribute("id");
                id = id.replaceAll("\\D+", "");
                if (!id.isEmpty()) {
                    return Integer.parseInt(id);
                }
            }
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

    public void clickDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), deleteAccountButton);
        deleteAccountButton.click();
    }

    public void clickCancelDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), cancelDeleteAccountButton);
        cancelDeleteAccountButton.click();
    }

    public void clickConfirmDeleteAccountButton() throws Exception {
        DriverUtils.waitUntilElementClickable(getDriver(), confirmDeleteAccountButton);
        confirmDeleteAccountButton.click();
    }

    public String getRememberedUniqueUsername() {
        return rememberedUniqueUsername;
    }

    public void setRememberedUniqueUsername(String rememberedUniqueUsername) {
        this.rememberedUniqueUsername = rememberedUniqueUsername;
    }
}
