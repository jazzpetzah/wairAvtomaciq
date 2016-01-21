package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ParticipantsPage extends AbstractConversationDetailsPage {
    public static final By xpathConfirmLeaveButton = By.xpath("//*[@id='confirm' and @value='LEAVE']");

    public static final Function<String, String> xpathStrParticipantAvatarByName = name -> String
            .format("//*[@id='pfac__participants']//*[@id='ttv__group__adapter' and @value='%s']/parent::*/parent::*",
                    name.split("\\s+")[0]);

    private static final String idStrConvoNameInput = "taet__participants__header__editable";
    private static final By idConvoNameInput = By.id(idStrConvoNameInput);
    private static final Function<String, String> xpathConvoNameInputByValue = value -> String
            .format("//*[@id='%s' and @value='%s']", idStrConvoNameInput, value);

    private static final Function<String, String> xpathStrSubheaderByValue = value -> String
            .format("//*[@id='ttv__participants__sub_header' and @value='%s']", value);

    public ParticipantsPage(Future<ZetaAndroidDriver> lazyDriver,
                            GroupPopover container) throws Exception {
        super(lazyDriver, container);
    }

    private OtherUserPersonalInfoPage getOUPIPageInstance() throws Exception {
        return this.getAndroidPageInstance(OtherUserPersonalInfoPage.class);
    }

    public void tapConfirmLeaveButton() throws Exception {
        getElement(xpathConfirmLeaveButton).click();
    }

    public boolean waitForParticipantAvatarVisible(String name)
            throws Exception {
        final By locator = By.xpath(xpathStrParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public void tapParticipantAvatar(String name) throws Exception {
        final By locator = By.xpath(xpathStrParticipantAvatarByName.apply(name));
        getElement(locator, String.format("The avatar of '%s' is not visible", name)).click();
    }

    public boolean waitForParticipantAvatarNotVisible(String name)
            throws Exception {
        final By locator = By.xpath(xpathStrParticipantAvatarByName.apply(name));
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
    }

    public void renameConversation(String newName) throws Exception {
        final WebElement convoNameInput = getElement(idConvoNameInput);
        convoNameInput.click();
        convoNameInput.clear();
        convoNameInput.sendKeys(newName);
        getDriver().tapSendButton();
    }

    public void tapOpenConversationButton() throws Exception {
        getOUPIPageInstance().tapLeftActionBtn();
    }

    public boolean waitUntilConversationNameVisible(String expectedName)
            throws Exception {
        final By locator = By.xpath(xpathConvoNameInputByValue.apply(expectedName));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

    public boolean waitUntilSubheaderIsVisible(String expectedText)
            throws Exception {
        final By locator = By.xpath(xpathStrSubheaderByValue.apply(expectedText));
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
    }

}
