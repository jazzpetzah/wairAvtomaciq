package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.ContactListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ConversationActionsOverlay extends AbstractPopoverContainer {
    public static final String xpathRootLocator = "//*[@id='fl__conversation_list__settings_box']//*[@id='ll__settings_box__container']";

    private ConversationActionsMenuPage conversationActionsMenuPage;

    public ConversationActionsOverlay(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
        this.conversationActionsMenuPage = new ConversationActionsMenuPage(
                lazyDriver, this);
    }

    @Override
    public boolean waitUntilVisible() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getLocator())) {
            final WebElement self = getDriver().findElement(getLocator());
            final int windowHeight = getDriver().manage().window().getSize().height;
            return self.getLocation().getX() >= 0
                    && self.getLocation().getY() < windowHeight;
        }
        return false;
    }

    @Override
    public boolean waitUntilInvisible() throws Exception {
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), getLocator(),
                2)) {
            final WebElement self = getDriver().findElement(getLocator());
            final int windowHeight = getDriver().manage().window().getSize().height;
            return self.getLocation().getX() < 0
                    || self.getLocation().getY() >= windowHeight;
        }
        return true;
    }

    @Override
    protected By getLocator() {
        return By.xpath(xpathRootLocator);
    }

    public void selectMenuItem(String itemName) throws Exception {
        this.conversationActionsMenuPage.selectMenuItem(itemName);
    }

    public void confirmDeleteConversationAlert() throws Exception {
        this.getAndroidPageInstance(ContactListPage.class).confirmDeleteConversationAlert();
    }
}
