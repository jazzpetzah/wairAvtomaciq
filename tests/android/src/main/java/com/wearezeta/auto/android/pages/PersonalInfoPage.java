package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class PersonalInfoPage extends AndroidPage {

    public static final String xpathParentSelfProfileOverlay = "//*[@id='fl__conversation_list__profile_overlay']";

    private static final String idBackgroundOverlay = "v_background_dark_overlay";
    @FindBy(id = idBackgroundOverlay)
    private WebElement backgroundOverlay;

    private static final String idTakePhotoButton = "gtv__camera_control__take_a_picture";
    @FindBy(id = idTakePhotoButton)
    private WebElement takePhotoBtn;

    private static final String xpathSettingsBox = xpathParentSelfProfileOverlay
            + "//*[@id='ll__settings_box_container']";
    @FindBy(xpath = xpathSettingsBox)
    private WebElement settingBox;

    private static final Function<String, String> xpathEmailFieldByValue = value -> String
            .format(xpathParentSelfProfileOverlay
                    + "//*[@id='ttv__profile__email' and @value='%s']", value);

    private static final Function<String, String> xpathNameFieldByValue = value -> String
            .format(xpathParentSelfProfileOverlay
                    + "//*[@id='ttv__profile__name' and @value='%s']", value);

    private static final Function<String, String> xpathEditFieldByValue = value -> String
            .format(xpathParentSelfProfileOverlay
                    + "//*[@id='tet__profile__guided' and @value='%s']", value);
    private static final String xpathNameEdit = xpathParentSelfProfileOverlay
            + "//*[@id='tet__profile__guided']";
    @FindBy(xpath = xpathNameEdit)
    private WebElement nameEdit;

    private static final String idChangePhotoBtn = "gtv__camera_control__change_image_source";
    @FindBy(id = idChangePhotoBtn)
    private WebElement changePhotoBtn;

    @FindBy(id = idGalleryBtn)
    private WebElement galleryBtn;

    @FindBy(xpath = DialogPage.xpathConfirmOKButton)
    private WebElement confirmBtn;

    private static final String xpathProfileOptionsButton = xpathParentSelfProfileOverlay
            + "//*[@id='gtv__profile__settings_button']";
    @FindBy(xpath = xpathProfileOptionsButton)
    private WebElement ellipsisButton;

    private static final Function<String,String> xpathProfileMenuItem = name ->
            String.format("//*[@id='ttv__settings_box__item' and @value='%s']" +
                    "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    private static final String xpathAboutButton = xpathParentSelfProfileOverlay
            + "//*[@id='ttv__profile__settings_box__about']";
    @FindBy(xpath = xpathAboutButton)
    private WebElement aboutButton;

    private static final String xpathSelfProfileClose = xpathParentSelfProfileOverlay
            + "//*[@id='gtv__profile__close_button']";
    @FindBy(xpath = xpathSelfProfileClose)
    private WebElement selfProfileClose;

    @FindBy(id = idPager)
    private WebElement page;

    private static final String idOpenFrom = "tiles";
    @FindBy(id = idOpenFrom)
    private List<WebElement> openFrom;

    private static final String idLightBulbButton = "gtv__profile__theme_button";
    @FindBy(id = idLightBulbButton)
    private WebElement lightBulbButton;

    public PersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void tapOnPage() throws Exception {
        this.getDriver().tap(1, page, 500);
    }

    public void tapChangePhotoButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                changePhotoBtn);
        changePhotoBtn.click();
    }

    public void tapTakePhotoButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), takePhotoBtn);
        takePhotoBtn.click();
    }

    public void tapGalleryButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(), galleryBtn);
        galleryBtn.click();
    }

    public void tapConfirmButton() throws Exception {
        this.hideKeyboard();
        assert DriverUtils.waitUntilElementClickable(getDriver(), confirmBtn);
        // Wait for animation
        Thread.sleep(1000);
        confirmBtn.click();
    }

    public void tapEllipsisButton() throws Exception {
        assert DriverUtils
                .waitUntilElementClickable(getDriver(), ellipsisButton);
        try {
            ellipsisButton.click();
        } catch (ElementNotVisibleException e) {
            // pass silently, this throws exception due to some internal
            // Selendroid (or AUT %) ) issue
        }
    }

    public boolean isProfileMenuItemInvisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathProfileMenuItem.apply(itemName));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapProfileMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathProfileMenuItem.apply(itemName));
        assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) :
                String.format("Menu item '%s' is not present on the page", itemName);
        getDriver().findElement(locator).click();
    }

    public void tapOnMyName(String name) throws Exception {
        final By nameFieldlocator = By.xpath(xpathNameFieldByValue.apply(name));
        getDriver().findElement(nameFieldlocator).click();
    }

    public boolean waitUntilNameEditIsVisible(String name) throws Exception {
        final By locator = By.xpath(xpathEditFieldByValue.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                locator);
    }

    public void clearSelfName() throws Exception {
        nameEdit.clear();
    }

    public void changeSelfNameTo(String newName) throws Exception {
        nameEdit.sendKeys(newName);
        // Sometimes a phone might fail to apple the new name without this sleep
        Thread.sleep(500);
        this.getDriver().navigate().back();
    }

    public boolean waitUntilNameIsVisible(String expectedName) throws Exception {
        final By locator = By.xpath(xpathNameFieldByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 3);
    }

    public boolean isSettingsVisible() throws Exception {
        return DriverUtils
                .isElementPresentAndDisplayed(getDriver(), settingBox);
    }

    public void pressCloseButton() throws Exception {
        assert DriverUtils.waitUntilElementClickable(getDriver(),
                selfProfileClose);
        selfProfileClose.click();
    }

    public void tapLightBulbButton() {
        lightBulbButton.click();
    }
}
