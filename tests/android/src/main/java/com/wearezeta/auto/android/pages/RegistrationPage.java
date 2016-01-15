package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class RegistrationPage extends AndroidPage {

    private static final String xpathParentSignUpContainer = "//*[@id='fl__sign_up__main_container']";

    private static final String idSignUpGalleryIcon = "gtv__sign_up__gallery_icon";
    @FindBy(id = idSignUpGalleryIcon)
    protected WebElement signUpGalleryIcon;

    private static final String xpathNameField = "(" + xpathParentSignUpContainer + "//*[@id='tet__profile__guided'])[1]";
    @FindBy(xpath = xpathNameField)
    protected WebElement nameField;

    private static final String idCreateUserBtn = "zb__sign_up__create_account";
    @FindBy(id = idCreateUserBtn)
    private WebElement createUserBtn;

    private static final String idVerifyEmailBtn = "ttv__sign_up__resend";
    @FindBy(id = idVerifyEmailBtn)
    private WebElement verifyEmailBtn;

    private static final String idNextArrow = "gtv__sign_up__next";
    @FindBy(id = idNextArrow)
    protected WebElement nextArrow;

    @FindBy(id = ContactListPage.idConfirmCancelButton)
    private WebElement laterBtn;

    @FindBy(id = PeoplePickerPage.idPickerSearch)
    private WebElement pickerSearch;

    private static final String idInvitationPasswordInput = "tet__email_invite__password";
    @FindBy(id = idInvitationPasswordInput)
    private WebElement invitationPassword;

    private static final String idInvitationContinueButton = "zb__email_invite__register";
    @FindBy(id = idInvitationContinueButton)
    private WebElement invitationContinueButton;

    public static final String idChooseMyOwnButton = "zb__choose_own_picture";
    @FindBy(id = idChooseMyOwnButton)
    private WebElement chooseMyOwnButton;

    public static final String idKeepPictureButton = "zb__keep_picture";
    @FindBy(id = idKeepPictureButton)
    private WebElement keepPictureButton;

    private static Function<String, String> xpathChoosePictSrcDialogButtonByName = name ->
            String.format("//*[starts-with(@id, 'button') and @value='%s']", name);

    public RegistrationPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void selectPicture() {
        signUpGalleryIcon.click();
    }

    public void setName(String name) throws Exception {
        verifyLocatorPresence(By.xpath(xpathNameField), "Name field is not visible");
        nameField.sendKeys(name);
        this.getWait().until(ExpectedConditions.elementToBeClickable(nextArrow));
        nextArrow.click();
    }

    public void createAccount() throws Exception {
        verifyLocatorPresence(By.id(idCreateUserBtn), "Create user button is not visible");
        createUserBtn.click();
    }

    public boolean isConfirmationVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idVerifyEmailBtn));
    }

    public void enterPassword(String password) throws Exception {
        verifyLocatorPresence(By.id(idInvitationContinueButton), "Invitation password input is not visible");
        invitationPassword.sendKeys(password);
    }

    public void tapContinueButton() {
        invitationContinueButton.click();
    }

    public void tapOwnPictureButton() {
        chooseMyOwnButton.click();
    }

    public void selectPictureSource(String src) throws Exception {
        final By locator = By.xpath(xpathChoosePictSrcDialogButtonByName.apply(src));
        verifyLocatorPresence(locator, "Source selection alert is not visible");
        getDriver().findElement(locator).click();
    }

    public boolean waitUntilUnsplashScreenIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idChooseMyOwnButton), 60);
    }
}
