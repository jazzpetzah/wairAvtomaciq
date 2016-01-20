package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {

    public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private PeoplePickerPage getAndroidPeoplePickerPage() throws Exception {
        return this.getAndroidPageInstance(PeoplePickerPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), PeoplePickerPage.idPeoplePickerClearbtn)
                && getElement(PeoplePickerPage.idPickerSearch).getLocation().getX() >= 0;
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), PeoplePickerPage.idPeoplePickerClearbtn)
                || getElement(PeoplePickerPage.idPickerSearch).getLocation().getX() < 0;
    }

    public void tapCloseButton() throws Exception {
        getElement(PeoplePickerPage.idPeoplePickerClearbtn, "Close button is not visible").click();
    }

    public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
        getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
    }

    public void tapFoundItem(String item) throws Exception {
        this.hideKeyboard();
        final By locator = By.xpath(PeoplePickerPage.xpathStrPeoplePickerContactByName.apply(item));
        getElement(locator,
                String.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
                        item)).click();
    }

    public boolean waitUntilTopPeopleIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), PeoplePickerPage.xpathTopPeopleAvatars);
    }

    public void tapTopPeopleAvatar(String name) throws Exception {
        final By locator = By.xpath(PeoplePickerPage.xpathStrTopPeopleAvatarByName.apply(name));
        getElement(locator).click();
    }

    public Optional<BufferedImage> takeAvatarScreenshot(String name) throws Exception {
        final By locator = By.xpath(PeoplePickerPage.xpathStrFoundAvatarByName.apply(name));
        return this.getElementScreenshot(getElement(locator, String.format("User avatar for '%s' is not visible", name)));
    }

    public boolean waitUntilAvatarIsVisible(String name) throws Exception {
        return this.getAndroidPeoplePickerPage().isUserVisible(name);
    }

    public boolean waitUntilGroupAvatarIsVisible(String name) throws Exception {
        return this.getAndroidPeoplePickerPage().isGroupVisible(name);
    }

    public boolean waitUntilAvatarIsInvisible(String name) throws Exception {
        return this.getAndroidPeoplePickerPage().isUserInvisible(name);
    }

    public boolean waitUntilGroupAvatarIsInvisible(String name) throws Exception {
        return this.getAndroidPeoplePickerPage().isGroupInvisible(name);
    }

    public void doShortSwipeDown() throws Exception {
        getAndroidPeoplePickerPage().doShortSwipeDown();
    }

    public void doLongSwipeDown() throws Exception {
        getAndroidPeoplePickerPage().doLongSwipeDown();
    }

    public void tapOpenOrCreateConversationButton() throws Exception {
        getAndroidPeoplePickerPage().tapOpenConversationButton();
    }

    public boolean waitUntilOpenOrCreateConversationButtonIsVisible(String expectedCaption) throws Exception {
        return getAndroidPeoplePickerPage().waitUntilOpenOrCreateConversationButtonIsVisible(expectedCaption);
    }

    public boolean waitUntilOpenConversationButtonIsInvisible() throws Exception {
        return getAndroidPeoplePickerPage().waitUntilOpenOrCreateConversationButtonIsInvisible();
    }

    public void tapCameraButton() throws Exception {
        getAndroidPeoplePickerPage().tapCameraButton();
    }

    public void tapCallButton() throws Exception {
        getAndroidPeoplePickerPage().tapCallButton();
    }

}
