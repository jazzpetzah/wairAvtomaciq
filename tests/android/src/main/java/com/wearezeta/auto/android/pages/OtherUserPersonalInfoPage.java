package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class OtherUserPersonalInfoPage extends AndroidPage {

    private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;

    public static final String idConnectRequestUnblock = "zb__connect_request__unblock_button";
    public static final String idSingleUserUnblock = "zb__single_user_participants__unblock_button";

    private static final Function<String, String> xpathPartcipantNameByText = text -> String
            .format("//*[@id='ttv__participants__header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathPartcipantEmailByText = text -> String
            .format("//*[@id='ttv__participants__sub_header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathSingleParticipantNameByText = text -> String
            .format("//*[@id='ttv__single_participants__header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathSingleParticipantEmailByText = text -> String
            .format("//*[@id='ttv__single_participants__sub_header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathParticipantAvatarByName = name -> String
            .format("//*[@id='cv__group__adapter' and ./parent::*/*[@value='%s']]",
                    name.split("\\s+")[0]);

    private static final String idParticipantsHeader = "ttv__participants__header";
    @FindBy(id = idParticipantsHeader)
    private WebElement groupChatName;

    private static final String idParticipantsHeaderEditable = "taet__participants__header__editable";
    @FindBy(id = idParticipantsHeaderEditable)
    private WebElement groupChatNameEditable;

    private static final String idUserProfileConfirmationMenu = "user_profile_confirmation_menu";
    @FindBy(id = idUserProfileConfirmationMenu)
    private WebElement confirmMenu;

    private static final String idRenameButton = "ttv__conversation_settings__rename";
    @FindBy(id = idRenameButton)
    private WebElement renameButton;

    private static final Function<String, String> xpathConvOptionsMenuItemByName = name -> String
            .format("//*[@id='fl__participant__settings_box']"
                    + "//*[starts-with(@id, 'ttv__settings_box__item') and @value='%s']"
                    + "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    @FindBy(id = PeoplePickerPage.idParticipantsClose)
    private WebElement closeButton;

    @FindBy(how = How.CLASS_NAME, using = classNameFrameLayout)
    private WebElement frameLayout;

    @FindBy(id = idPager)
    private WebElement backGround;

    @FindBy(xpath = xpathConfirmBtn)
    private WebElement confirmBtn;

    public static final String xpathLeftActionButton =
            "//*[@id='fm__footer']//*[@id='gtv__participants__left__action']";
    @FindBy(xpath = xpathLeftActionButton)
    private WebElement leftActionButton;

    public static final String xpathRightActionButton =
            "//*[@id='fm__footer']//*[@id='gtv__participants__right__action']";
    @FindBy(xpath = xpathRightActionButton)
    private WebElement rightActionButton;

    public static final String xpathEllipsisButton =
            "//*[@id='fm__participants__footer' or @id='fm__footer']//*[@id='gtv__participants__right__action']";
    @FindBy(xpath = xpathEllipsisButton)
    private WebElement ellipsisButton;

    private static final String idParticipantsSubHeader = "ttv__participants__sub_header";
    @FindBy(id = idParticipantsSubHeader)
    private WebElement participantsSubHeader;

    @FindBy(id = IncomingPendingConnectionsPage.idConnectToHeader)
    private List<WebElement> connectToHeader;

    public OtherUserPersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void pressOptionsMenuButton() throws Exception {
        ellipsisButton.click();
    }

    public boolean isUnblockBtnVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idSingleUserUnblock), 5)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.id(idConnectRequestUnblock), 5);
    }

    private static By[] getOneToOneOptionsMenuLocators() {
        return new By[]{
                By.xpath(xpathConvOptionsMenuItemByName.apply("BLOCK")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("SILENCE")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("DELETE")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("ARCHIVE"))};
    }

    private static final int MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS = 5;

    public boolean areOneToOneMenuOptionsVisible() throws Exception {
        for (By locator : getOneToOneOptionsMenuLocators()) {
            if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator,
                    MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
                return false;
            }
        }
        return true;
    }

    public boolean areOneToOneMenuOptionsNotVisible() throws Exception {
        for (By locator : getOneToOneOptionsMenuLocators()) {
            if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                    locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
                return false;
            }
        }
        return true;
    }

    private static By[] getParticipantPageLocators() {
        return new By[]{By.id(PeoplePickerPage.idParticipantsClose),
                By.id(idParticipantsSubHeader), By.id(idParticipantsHeader)};
    }

    public boolean isParticipatPageUIContentNotVisible() throws Exception {
        for (By locator : getParticipantPageLocators()) {
            if (!DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
                    locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
                return false;
            }
        }
        return true;
    }

    public boolean isParticipatPageUIContentVisible() throws Exception {
        for (By locator : getParticipantPageLocators()) {
            if (!DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
                    locator, MENU_ITEM_VISIBILITY_TIMEOUT_SECONDS)) {
                return false;
            }
        }
        return true;
    }

    public void selectConvoSettingsMenuItem(String itemName) throws Exception {
        final By locator = By.xpath(xpathConvOptionsMenuItemByName
                .apply(itemName));
        getElement(locator,
                String.format("Conversation menu item '%s' could not be found on the current screen", itemName)).
                click();
    }

    public boolean isOtherUserNameVisible(String expectedName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathPartcipantNameByText.apply(expectedName)), 1)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
                .xpath(xpathSingleParticipantNameByText
                        .apply(expectedName)), 1);
    }

    public boolean isOtherUserMailVisible(String expectedEmail)
            throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathPartcipantEmailByText.apply(expectedEmail)), 1)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
                .xpath(xpathSingleParticipantEmailByText
                        .apply(expectedEmail)), 1);
    }

    public boolean isConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idUserProfileConfirmationMenu));
    }

    public void pressConfirmBtn() throws Exception {
        getElement(By.xpath(xpathConfirmBtn), "Confirmation button is not visible").click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), By.xpath(xpathConfirmBtn), 3)) {
            throw new IllegalStateException("Confirmation button is still visible after 3 seconds timeout");
        }
    }

    public void tapLeftActionBtn() throws Exception {
        leftActionButton.click();
    }

    public boolean isBackGroundImageCorrect(String imageName) throws Exception {
        final BufferedImage bgImage = getElementScreenshot(backGround)
                .orElseThrow(IllegalStateException::new);
        String path = CommonUtils.getImagesPath(CommonUtils.class);
        BufferedImage realImage = ImageUtil.readImageFromFile(path + imageName);
        double score = ImageUtil.getOverlapScore(realImage, bgImage);
        return (score >= MIN_ACCEPTABLE_IMAGE_VALUE);
    }

    public boolean isParticipantNotVisible(String name) throws Exception {
        final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapOnParticipantsHeader() {
        groupChatName.click();
    }

    public void renameGroupChat(String chatName) throws Exception {
        groupChatNameEditable.clear();
        groupChatNameEditable.sendKeys(chatName);
        this.pressKeyboardSendButton();
    }

    public void tapOnParticipant(String name) throws Exception {
        // Wait for animation
        Thread.sleep(1000);
        final By nameLocator = By.xpath(xpathParticipantAvatarByName
                .apply(name));
        assert DriverUtils
                .waitUntilLocatorIsDisplayed(getDriver(), nameLocator) : String
                .format("The avatar of '%s' is not visible", name);
        this.getDriver().findElement(nameLocator).click();
    }

    public String getSubHeader() {
        return participantsSubHeader.getText();
    }

    public String getConversationName() throws Exception {
        return groupChatName.getText();
    }

    public void tapCloseButton() throws Exception {
        final WebElement closeButton = getElement(By.id(PeoplePickerPage.idParticipantsClose),
                "Close participants button is not visible");
        final int halfHeight = this.getDriver().manage().window().getSize().getHeight() / 2;
        int ntry = 1;
        final int maxRetries = 3;
        do {
            closeButton.click();
            ntry++;
        } while (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(PeoplePickerPage.idParticipantsClose), 1)
                && closeButton.getLocation().getY() < halfHeight
                && ntry <= maxRetries);
        if (ntry > maxRetries) {
            throw new AssertionError(
                    String.format(
                            "The conversations details screen has not been closed after %s retries",
                            maxRetries));
        }
    }

    public boolean isParticipantAvatarVisible(String name) throws Exception {
        final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean isUserProfileMenuItemVisible(String itemName) throws Exception {
        final By locator = By.xpath(xpathConvOptionsMenuItemByName
                .apply(itemName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapRightActionButton() {
        rightActionButton.click();
    }
}
