package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {
    @FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
    private WebElement closePeoplePickerBtn;

    public static final String idPickerSearch = "puet_pickuser__searchbox";
    @FindBy(id = idPickerSearch)
    public WebElement pickerSearch;

    public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    private PeoplePickerPage getAndroidPeoplePickerPage() throws Exception {
        return this.getAndroidPageInstance(PeoplePickerPage.class);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.id(PeoplePickerPage.idPeoplePickerClearbtn))
                && pickerSearch.getLocation().getX() >= 0;
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), By.id(PeoplePickerPage.idPeoplePickerClearbtn))
                || pickerSearch.getLocation().getX() < 0;
    }

    public void tapCloseButton() throws Exception {
        verifyLocatorPresence(By.id(PeoplePickerPage.idPeoplePickerClearbtn), "Close button is not visible").click();
    }

    public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
        getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
    }

    public void tapFoundItem(String item) throws Exception {
        final By locator = By
                .xpath(PeoplePickerPage.xpathPeoplePickerContactByName
                        .apply(item));
        verifyLocatorPresence(locator,
                String.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
                        item)).click();
    }

    public boolean waitUntilTopPeopleIsVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                By.xpath(PeoplePickerPage.xpathTopPeopleAvatars));
    }

    public void tapTopPeopleAvatar(String name) throws Exception {
        final By locator = By.xpath(PeoplePickerPage.xpathTopPeopleAvatarByName.apply(name));
        getDriver().findElement(locator).click();
    }

    public Optional<BufferedImage> takeAvatarScreenshot(String name)
            throws Exception {
        final By locator = By.xpath(PeoplePickerPage.xpathFoundAvatarByName.apply(name));
        return this.getElementScreenshot(verifyLocatorPresence(locator,
                String.format("User avatar for '%s' is not visible", name)));
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

    public boolean waitUntilOpenOrCreateConversationButtonIsVisible(String expectedCaption)
            throws Exception {
        return getAndroidPeoplePickerPage()
                .waitUntilOpenOrCreateConversationButtonIsVisible(expectedCaption);
    }

    public boolean waitUntilOpenConversationButtonIsInvisible()
            throws Exception {
        return getAndroidPeoplePickerPage()
                .waitUntilOpenOrCreateConversationButtonIsInvisible();
    }

    public void tapCameraButton() throws Exception {
        getAndroidPeoplePickerPage().tapCameraButton();
    }

    public void tapCallButton() throws Exception {
        getAndroidPeoplePickerPage().tapCallButton();
    }

}
