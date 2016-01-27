package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import java.util.stream.Collectors;

public class OtherUserPersonalInfoPage extends AndroidPage {

    private static final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;

    public static final By xpathUnblockButton = By.xpath("//*[contains(@id, '__unblock_button') and @shown='true']");

    private static final Function<String, String> xpathParticipantNameByText = text -> String
            .format("//*[@id='ttv__participants__header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathParticipantEmailByText = text -> String
            .format("//*[@id='ttv__participants__sub_header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathSingleParticipantNameByText = text -> String
            .format("//*[@id='ttv__single_participants__header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathSingleParticipantEmailByText = text -> String
            .format("//*[@id='ttv__single_participants__sub_header' and @value='%s']",
                    text);

    private static final Function<String, String> xpathSingleParticipantTabByText = text -> String
            .format("//*[@value='%s']",
                    text);

    private static final By idParticipantDevices = By.id("ttv__row_otr_device");


    private static final Function<String, String> xpathParticipantAvatarByName = name -> String
            .format("//*[@id='cv__group__adapter' and ./parent::*/*[@value='%s']]",
                    name.split("\\s+")[0]);

    private static final By idParticipantsHeader = By.id("ttv__participants__header");

    private static final By idParticipantsHeaderEditable = By.id("taet__participants__header__editable");

    private static final By idUserProfileConfirmationMenu = By.id("user_profile_confirmation_menu");

    private static final Function<String, String> xpathConvOptionsMenuItemByName = name -> String
            .format("//*[@id='fl__participant__settings_box']"
                    + "//*[starts-with(@id, 'ttv__settings_box__item') and @value='%s']"
                    + "/parent::*//*[@id='fl_options_menu_button']", name.toUpperCase());

    public static final By xpathLeftActionButton =
            By.xpath("//*[contains(@id, '__left__action') and starts-with(@id, 'gtv__') and @shown='true']");

    public static final By xpathRightActionButton =
            By.xpath("//*[contains(@id, '__right__action') and starts-with(@id, 'gtv__') and @shown='true']");

    private static final By idParticipantsSubHeader = By.id("ttv__participants__sub_header");

    public OtherUserPersonalInfoPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public void tapRightActionButton() throws Exception {
        final List<WebElement> visibleButtons = selectVisibleElements(xpathRightActionButton);
        if (visibleButtons.isEmpty()) {
            throw new IllegalStateException("Cannot locate right action button");
        }
        this.getDriver().tap(1, visibleButtons.get(0).getLocation().getX() + visibleButtons.get(0).getSize().width / 2,
                visibleButtons.get(0).getLocation().getY() + visibleButtons.get(0).getSize().height / 2,
                DriverUtils.SINGLE_TAP_DURATION);
    }

    public void pressOptionsMenuButton() throws Exception {
        tapRightActionButton();
    }

    public boolean isUnblockBtnVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathUnblockButton);
    }

    private static By[] getOneToOneOptionsMenuLocators() {
        return new By[]{
                By.xpath(xpathConvOptionsMenuItemByName.apply("BLOCK")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("SILENCE")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("DELETE")),
                By.xpath(xpathConvOptionsMenuItemByName.apply("ARCHIVE"))};
    }

    private static By[] getSingleParticipantTabs() {
        return new By[]{
            By.xpath(xpathSingleParticipantTabByText.apply("DETAILS")),
            By.xpath(xpathSingleParticipantTabByText.apply("DEVICES"))
        };
    }

    public List<String> getParticipantDevices() throws Exception {
        return selectVisibleElements(idParticipantDevices).stream().map(WebElement::getText).map((string) -> string.replaceAll("(PHONE)|(DESKTOP)|(\\n)|(\\s)|(ID:)", "").toLowerCase()).collect(Collectors.toList());
    }

    public void selectSingleParticipantTab(String itemName) throws Exception {
        final By locator = By.xpath(xpathSingleParticipantTabByText.apply(itemName));
        getElement(locator,
                String.format("Single participant tab item '%s' could not be found on the current screen", itemName)).
                click();
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
        return new By[]{PeoplePickerPage.idParticipantsClose,
                idParticipantsSubHeader, idParticipantsHeader};
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
        final By locator = By.xpath(xpathConvOptionsMenuItemByName.apply(itemName));
        getElement(locator,
                String.format("Conversation menu item '%s' could not be found on the current screen", itemName)).
                click();
    }

    public boolean isOtherUserNameVisible(String expectedName) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathParticipantNameByText.apply(expectedName)), 1)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
                .xpath(xpathSingleParticipantNameByText.apply(expectedName)), 1);
    }

    public boolean isOtherUserMailVisible(String expectedEmail)
            throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(xpathParticipantEmailByText.apply(expectedEmail)), 1)
                || DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By
                .xpath(xpathSingleParticipantEmailByText.apply(expectedEmail)), 1);
    }

    public boolean isConversationAlertVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idUserProfileConfirmationMenu);
    }

    public void pressConfirmBtn() throws Exception {
        getElement(xpathConfirmBtn, "Confirmation button is not visible").click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathConfirmBtn, 3)) {
            throw new IllegalStateException("Confirmation button is still visible after 3 seconds timeout");
        }
    }

    public void tapLeftActionBtn() throws Exception {
        final List<WebElement> visibleButtons = selectVisibleElements(xpathLeftActionButton);
        if (visibleButtons.isEmpty()) {
                throw new IllegalStateException("Cannot locate left action button");
        }
        this.getDriver().tap(1, visibleButtons.get(0).getLocation().getX() + visibleButtons.get(0).getSize().width / 2,
                visibleButtons.get(0).getLocation().getY() + visibleButtons.get(0).getSize().height / 2,
                DriverUtils.SINGLE_TAP_DURATION);
    }

    public boolean isBackGroundImageCorrect(String imageName) throws Exception {
        final BufferedImage bgImage = getElementScreenshot(getElement(idPager))
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

    public void tapOnParticipantsHeader() throws Exception {
        getElement(idParticipantsHeader).click();
    }

    public void renameGroupChat(String chatName) throws Exception {
        final WebElement groupChatNameEditable = getElement(idParticipantsHeaderEditable);
        groupChatNameEditable.clear();
        groupChatNameEditable.sendKeys(chatName);
        this.pressKeyboardSendButton();
    }

    public void tapOnParticipant(String name) throws Exception {
        // Wait for animation
        Thread.sleep(1000);
        final By nameLocator = By.xpath(xpathParticipantAvatarByName.apply(name));
        getElement(nameLocator, String.format("The avatar of '%s' is not visible", name)).click();
    }

    public String getSubHeader() throws Exception {
        return getElement(idParticipantsSubHeader).getText();
    }

    public String getConversationName() throws Exception {
        return getElement(idParticipantsHeader).getText();
    }

    public void tapCloseButton() throws Exception {
        final WebElement closeButton = getElement(PeoplePickerPage.idParticipantsClose,
                "Close participants button is not visible");
        final int halfHeight = this.getDriver().manage().window().getSize().getHeight() / 2;
        int ntry = 1;
        final int maxRetries = 3;
        do {
            closeButton.click();
            ntry++;
        } while (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), PeoplePickerPage.idParticipantsClose, 1)
                && closeButton.getLocation().getY() < halfHeight
                && ntry <= maxRetries);
        if (ntry > maxRetries) {
            throw new AssertionError(
                    String.format(
                            "The conversations details screen has not been closed after %s retries",
                            maxRetries));
        }
    }

    public void tapSingleParticipantCloseButton() throws Exception {
        final WebElement closeButton = getElement(PeoplePickerPage.idSingleParticipantClose,
                "Close single participant button is not visible");
        final int halfHeight = this.getDriver().manage().window().getSize().getHeight() / 2;
        int ntry = 1;
        final int maxRetries = 3;
        do {
            closeButton.click();
            ntry++;
        } while (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), PeoplePickerPage.idSingleParticipantClose, 1)
                && closeButton.getLocation().getY() < halfHeight
                && ntry <= maxRetries);
        if (ntry > maxRetries) {
            throw new AssertionError(
                    String.format(
                            "The participant details screen has not been closed after %s retries",
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
}
