package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class RegistrationPage extends AndroidPage {

    private static final String xpathStrParentSignUpContainer = "//*[@id='fl__sign_up__main_container']";

    private static final By idSignUpGalleryIcon = By.id("gtv__sign_up__gallery_icon");

    private static final By xpathNameField =
            By.xpath("(" + xpathStrParentSignUpContainer + "//*[@id='tet__profile__guided'])[1]");

    private static final By idCreateUserBtn = By.id("zb__sign_up__create_account");

    private static final By idVerifyEmailBtn = By.id("ttv__sign_up__resend");

    private static final By idNextArrow = By.id("gtv__sign_up__next");

    private static final By idInvitationContinueButton = By.id("zb__email_invite__register");

    public static final By idChooseMyOwnButton = By.id("zb__choose_own_picture");

    private static Function<String, String> xpathChoosePictSrcDialogButtonByName = name ->
            String.format("//*[starts-with(@id, 'button') and @value='%s']", name);

    public RegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void selectPicture() throws Exception {
        getElement(idSignUpGalleryIcon).click();
    }

    public void setName(String name) throws Exception {
        getElement(xpathNameField, "Name field is not visible").sendKeys(name);
        this.getWait().until(ExpectedConditions.elementToBeClickable(getElement(idNextArrow))).click();
    }

    public void createAccount() throws Exception {
        getElement(idCreateUserBtn, "Create user button is not visible").click();
    }

    public boolean isConfirmationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idVerifyEmailBtn);
    }

    public void enterPassword(String password) throws Exception {
        getElement(idInvitationContinueButton, "Invitation password input is not visible").sendKeys(password);
    }

    public void tapContinueButton() throws Exception {
        getElement(idInvitationContinueButton).click();
    }

    public void tapOwnPictureButton() throws Exception {
        getElement(idChooseMyOwnButton).click();
    }

    public void selectPictureSource(String src) throws Exception {
        final By locator = By.xpath(xpathChoosePictSrcDialogButtonByName.apply(src));
        getElement(locator, "Source selection alert is not visible").click();
    }

    public boolean waitUntilUnsplashScreenIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idChooseMyOwnButton, 60);
    }
}
