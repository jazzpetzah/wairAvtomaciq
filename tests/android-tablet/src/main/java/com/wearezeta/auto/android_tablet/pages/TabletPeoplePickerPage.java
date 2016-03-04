package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.SearchPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {

    public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    private SearchPage getAndroidPeoplePickerPage() throws Exception {
        return this.getAndroidPageInstance(SearchPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), SearchPage.xpathMainSearchField);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), SearchPage.xpathMainSearchField);
    }

    public void tapCloseButton() throws Exception {
        getElement(SearchPage.idPeoplePickerClearbtn, "Close button is not visible").click();
    }

    public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
        getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
    }

    public void tapFoundItem(String item) throws Exception {
        this.hideKeyboard();
        final By locator = By.xpath(SearchPage.xpathStrPeoplePickerContactByName.apply(item));
        getElement(locator,
                String.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
                        item)).click();
    }

    public void tapTopPeopleAvatar(String name) throws Exception {
        final By locator = By.xpath(SearchPage.xpathStrTopPeopleAvatarByName.apply(name));
        getElement(locator).click();
    }

    public Optional<BufferedImage> takeAvatarScreenshot(String name) throws Exception {
        final By locator = By.xpath(SearchPage.xpathStrFoundAvatarByName.apply(name));
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

    public void tapActionButton(String name) throws Exception {
        getAndroidPeoplePickerPage().tapActionButton(name);
    }

    public void typeBackspaceInSearchInput() throws Exception {
        getAndroidPeoplePickerPage().typeBackspaceInSearchInput();
    }

    public boolean waitUntilActionButtonIsVisible(String buttonName) throws Exception {
        return getAndroidPeoplePickerPage().waitUntilActionButtonIsVisible(buttonName);
    }

    public boolean waitUntilActionButtonIsInvisible(String buttonName) throws Exception {
        return getAndroidPeoplePickerPage().waitUntilActionButtonIsInvisible(buttonName);
    }
}
