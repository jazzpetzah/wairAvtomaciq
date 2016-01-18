package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class IncomingPendingConnectionsPage extends AndroidPage {
    public static final String idConnectToHeader = "taet__participants__header";
    @FindBy(id = idConnectToHeader)
    private WebElement connectToHeader;

    @FindBy(id = idConnectToHeader)
    private List<WebElement> connectToHeaders;

    private static final Function<String, String> xpathConnectToHeaderByText = text -> String
            .format("//*[@id='taet__participants__header' and @value='%s']",
                    text);

    private static final String idConnectRequestAccept = "zb__connect_request__accept_button";
    @FindBy(id = idConnectRequestAccept)
    private WebElement connectAcceptBtn;

    private static final Function<String, String> xpathAcceptButtonByHeaderText = text -> String
            .format("//*[@id='ll__connect_request__main_container' and .%s]//*[@id='%s']",
                    xpathConnectToHeaderByText.apply(text),
                    idConnectRequestAccept);

    @FindBy(id = idConnectRequestAccept)
    private List<WebElement> connectAcceptBtns;

    private static final String idConnectRequestIgnore = "zb__connect_request__ignore_button";
    @FindBy(id = idConnectRequestIgnore)
    private WebElement connectIgnoreBtn;

    private static final Function<String, String> xpathUserDetailsLeftButton = label -> String
            .format("//*[@id='ttv__participants__left_label' and @value='%s']",
                    label);

    @FindBy(id = PeoplePickerPage.idSendConnectionRequestButton)
    private WebElement sendConnectionRequestButton;

    private static final String idConnectToCharCounter =
            "ttv__send_connect_request__connect_button__character_counter";
    @FindBy(id = idConnectToCharCounter)
    private WebElement connectCharCounter;

    private static final String xpathCloseButton =
            "//*[@id='fl__conversation_list__profile_overlay']//*[@id='gtv__participants__close']";
    @FindBy(xpath = xpathCloseButton)
    private WebElement closeButton;

    private static final Function<String, String> xpathConnectMenuItemByText = text -> String
            .format("//*[@id='ttv__settings_box__item' and @value='%s']/parent::*//*[@id='fl_options_menu_button']",
                    text.toUpperCase());

    @FindBy(xpath = OtherUserPersonalInfoPage.xpathRightActionButton)
    private WebElement ellipsisButton;

    @FindBy(xpath = xpathConfirmBtn)
    private WebElement confirmBtn;

    public IncomingPendingConnectionsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickEllipsisButton() {
        ellipsisButton.click();
    }

    public void clickBlockBtn() throws Exception {
        final By blockButtonLocator = By.xpath(xpathConnectMenuItemByText.apply("Block"));
        final WebElement blockButton = getElement(blockButtonLocator, "Block button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), blockButton)) {
            throw new IllegalStateException("Block button is not clickable");
        }
        blockButton.click();
    }

    public void clickUnblockBtn() throws Exception {
        for (String id : new String[]{
                OtherUserPersonalInfoPage.idConnectRequestUnblock,
                OtherUserPersonalInfoPage.idSingleUserUnblock}) {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(id),
                    1)) {
                getDriver().findElement(By.id(id)).click();
                return;
            }
        }
        throw new AssertionError("Unblock button is not visible");
    }

    public void pressConfirmBtn() throws Exception {
        this.getWait().until(
                ExpectedConditions.elementToBeClickable(confirmBtn));
        confirmBtn.click();
    }

    public boolean isConnectToHeaderVisible(String name) throws Exception {
        final By locator = By.xpath(xpathConnectToHeaderByText.apply(name));
        boolean result = DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                locator);
        if (!result)
            log.debug("Can't find correct connect to header. Page source: "
                    + getDriver().getPageSource());
        return result;
    }

    public void scrollToInboxContact(String contactName, final int maxUsers)
            throws Exception {
        final By locator = By.xpath(xpathAcceptButtonByHeaderText
                .apply(contactName));
        int ntry = 1;
        final int SCROLL_POS_START = 48;
        final int SCROLL_POS_END = 70;
        final int maxScrolls = maxUsers
                * (100 / (SCROLL_POS_END - SCROLL_POS_START) + 1);
        do {
            if (DriverUtils
                    .waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return;
            }
            this.swipeByCoordinates(1000, 50, SCROLL_POS_END, 50,
                    SCROLL_POS_START);
            ntry++;
        } while (ntry <= maxScrolls);
        throw new RuntimeException(
                String.format(
                        "Failed to find user %s in the inbox after scrolling %s times!",
                        contactName, maxScrolls));
    }

    public void pressAcceptConnectButton() throws Exception {
        final Optional<WebElement> connectAcceptBtn = DriverUtils.getElementIfDisplayed(getDriver(),
                By.id(idConnectRequestAccept));
        final int windowHeight = this.getDriver().manage().window().getSize().getHeight();
        if (connectAcceptBtn.isPresent() &&
                connectAcceptBtn.get().getLocation().getY() > windowHeight / 2
                && connectAcceptBtn.get().getLocation().getY() < windowHeight) {
            connectAcceptBtn.get().click();
        } else {
            connectAcceptBtns.get(1).click();
        }
    }

    public void pressIgnoreButton() throws Exception {
        getElement(By.id(idConnectRequestIgnore), "Ignore button is not visible").click();
    }

    public boolean isIgnoreConnectButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(idConnectRequestIgnore));
    }

    public boolean isPending() throws Exception {
        final By locator = By.xpath(xpathUserDetailsLeftButton.apply("Pending"));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void pressLeftConnectButton() throws Exception {
        getElement(By.xpath(xpathUserDetailsLeftButton.apply("Connect")),
                "Connect button is not visible").click();
    }

    public void pressConnectButton() throws Exception {
        getElement(By.id(PeoplePickerPage.idSendConnectionRequestButton),
                "Connect button is not visible").click();
    }

    public void waitUntilIgnoreButtonIsClickable() throws Exception {
        if (!DriverUtils.waitUntilElementClickable(getDriver(), connectIgnoreBtn)) {
            throw new IllegalStateException("Ignore button is not clickable");
        }
    }

    public boolean getConnectButtonState() {
        String state = sendConnectionRequestButton.getAttribute("enabled");
        return Boolean.parseBoolean(state);
    }

    public int getCharCounterValue() {
        return Integer.parseInt(connectCharCounter.getText());
    }

    public void clickCloseButton() throws Exception {
        closeButton.click();
    }

}
