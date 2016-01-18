package com.wearezeta.auto.android_tablet.pages;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationsListPage extends AndroidTabletPage {
    private static final int PLAY_PAUSE_BUTTON_WIDTH_PERCENTAGE = 15;

    public TabletConversationsListPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private ContactListPage getContactListPage() throws Exception {
        return this.getAndroidPageInstance(ContactListPage.class);
    }

    private PeoplePickerPage getPeoplePickerPage() throws Exception {
        return this.getAndroidPageInstance(PeoplePickerPage.class);
    }

    private static final int LOAD_TIMEOUT = 15; // seconds

    private static final String xpathConvoViewOrSelfProfile = String.format("//*[@id='%s' or @id='%s']",
            TabletSelfProfilePage.idSelfProfileView, TabletConversationViewPage.idRootLocator);

    public void verifyConversationsListIsLoaded() throws Exception {
        if (ScreenOrientationHelper.getInstance().fixOrientation(getDriver()) == ScreenOrientation.PORTRAIT) {
            if (DriverUtils.waitUntilLocatorAppears(getDriver(),
                    By.xpath(xpathConvoViewOrSelfProfile),
                    LOAD_TIMEOUT)) {
                // FIXME: Workaround for self profile as start page issue
                int ntry = 1;
                final int maxRetries = 3;
                final int leftBorderWidth = getDriver().manage().window()
                        .getSize().width / 4;
                Optional<WebElement> selfProfileEl = Optional.empty();
                if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                        By.id(TabletSelfProfilePage.idSelfProfileView), 1)) {
                    selfProfileEl = Optional.of(getDriver().findElement(
                            By.id(TabletSelfProfilePage.idSelfProfileView)));
                }
                do {
                    if (DriverUtils.waitUntilLocatorDissapears(getDriver(),
                            By.id(ContactListPage.idSelfUserAvatar), 2)
                            || (selfProfileEl.isPresent() && selfProfileEl
                            .get().getLocation().getX() < leftBorderWidth)) {
                        DriverUtils.swipeByCoordinates(getDriver(), 1000, 30,
                                50, 90, 50);
                        // FIXME: Self profile could switch to full colour
                        // instead
                        // of being swiped
                        if (DriverUtils.waitUntilLocatorIsDisplayed(
                                getDriver(),
                                By.id(ContactListPage.idSelfUserAvatar), 1)
                                && (selfProfileEl.isPresent() && selfProfileEl
                                .get().getLocation().getX() > leftBorderWidth)) {
                            break;
                        } else {
                            this.tapOnCenterOfScreen();
                            DriverUtils.swipeByCoordinates(getDriver(), 1000,
                                    30, 50, 90, 50);
                        }
                    } else {
                        break;
                    }
                    ntry++;
                } while (ntry <= maxRetries);
                if (ntry > maxRetries) {
                    throw new IllegalStateException(
                            String.format(
                                    "Conversations list was not shown after %d retries",
                                    maxRetries));
                }
            } else {
                throw new IllegalStateException(String.format(
                        "The initial view has not been loaded within %s seconds",
                        LOAD_TIMEOUT));
            }
        }
        getContactListPage().verifyContactListIsFullyLoaded();
    }

    public TabletSelfProfilePage tapMyAvatar() throws Exception {
        getContactListPage().tapOnMyAvatar();
        // Wait for transition animation
        Thread.sleep(1000);
        return new TabletSelfProfilePage(this.getLazyDriver());
    }

    public void tapSearchInput() throws Exception {
        getPeoplePickerPage().tapPeopleSearch();
    }

    public boolean waitUntilConversationIsVisible(String name) throws Exception {
        final By locator = By.xpath(ContactListPage.xpathContactByName
                .apply(name));
        return DriverUtils
                .waitUntilLocatorIsDisplayed(getDriver(), locator, 40);
    }

    public boolean waitUntilConversationIsInvisible(String name)
            throws Exception {
        final By locator = By.xpath(ContactListPage.xpathContactByName
                .apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void tapConversation(String name) throws Exception {
        final By locator = By.xpath(ContactListPage.xpathContactByName
                .apply(name));
        getElement(locator,
                String.format("The conversation '%s' does not exist in the conversations list", name)).click();
    }

    public boolean waitUntilConversationIsSilenced(String name)
            throws Exception {
        final By locator = By.xpath(ContactListPage.xpathMutedIconByConvoName
                .apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilConversationIsNotSilenced(String name)
            throws Exception {
        final By locator = By.xpath(ContactListPage.xpathMutedIconByConvoName
                .apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public boolean isAnyConversationVisible() throws Exception {
        return getContactListPage().isAnyConversationVisible();
    }

    public boolean isNoConversationsVisible() throws Exception {
        return getContactListPage().isNoConversationsVisible();
    }

    public boolean waitUntilMissedCallNotificationVisible(String convoName)
            throws Exception {
        return getContactListPage().waitUntilMissedCallNotificationVisible(
                convoName);
    }

    public boolean waitUntilMissedCallNotificationInvisible(String convoName)
            throws Exception {
        return getContactListPage().waitUntilMissedCallNotificationInvisible(
                convoName);
    }

    public void doShortSwipeDown() throws Exception {
        getContactListPage().doShortSwipeDown();
    }

    public void doLongSwipeDown() throws Exception {
        getContactListPage().doLongSwipeDown();
    }

    public void doSwipeLeft() throws Exception {
        DriverUtils.swipeByCoordinates(getDriver(), 1000, 95, 50, 10, 50);
    }

    public boolean waitUntilPlayPauseButtonVisibleNextTo(String convoName)
            throws Exception {
        try {
            return getContactListPage()
                    .isPlayPauseMediaButtonVisible(convoName);
        } catch (InvalidElementStateException e) {
            // Workaround for Selendroid (or application) bug
            return true;
        }
    }

    public Optional<BufferedImage> getScreenshotOfPlayPauseButton(
            Rectangle elementCoord) throws Exception {
        final BufferedImage fullScreenshot = this.takeScreenshot().orElseThrow(
                IllegalStateException::new);
        return Optional.of(fullScreenshot.getSubimage(elementCoord.x,
                elementCoord.y, elementCoord.width, elementCoord.height));
    }

    public void tapPlayPauseMediaButton(Rectangle elementCoord)
            throws Exception {
        this.getDriver().tap(1, elementCoord.x + elementCoord.width / 2,
                elementCoord.y + elementCoord.height / 2, 1);
    }

    public Rectangle calcPlayPauseButtonCoordinates(String convoName)
            throws Exception {
        final Rectangle result = new Rectangle();
        final WebElement convoElement = getDriver().findElement(
                By.xpath(ContactListPage.xpathContactByName.apply(convoName)));
        final int playPauseButtonWidth = convoElement.getSize().width
                * PLAY_PAUSE_BUTTON_WIDTH_PERCENTAGE / 100;
        result.setLocation(
                convoElement.getLocation().x + convoElement.getSize().width
                        - playPauseButtonWidth, convoElement.getLocation().y);
        result.setSize(playPauseButtonWidth, convoElement.getSize().height);
        return result;
    }

    public void doLongSwipeUp() throws Exception {
        getContactListPage().doLongSwipeUp();
    }

    public void swipeRightListItem(String name) throws Exception {
        final By locator = By.xpath(ContactListPage.xpathContactByName
                .apply(name));
        DriverUtils.swipeElementPointToPoint(getDriver(), getDriver()
                .findElement(locator), 1000, 20, 50, 100, 50);
    }

    public void tapInviteButton() throws Exception {
        getContactListPage().tapInviteButton();
    }
}
