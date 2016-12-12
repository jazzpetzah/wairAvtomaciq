package com.wearezeta.auto.android.pages.details_overlay.single;


import com.wearezeta.auto.android.pages.details_overlay.BasePendingIncomingConnectionOverlay;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class SinglePendingIncomingConnectionPage extends BasePendingIncomingConnectionOverlay {
    private static final String idStrUserName = "ttv__connect_request__display_name";

    public SinglePendingIncomingConnectionPage(Future<ZetaAndroidDriver> driver) throws Exception {
        super(driver);
    }

    private final static int MAX_USERS_TO_SCROLL = 5;

    public void scrollToContact(String contactName) throws Exception {
        scrollToContact(contactName, MAX_USERS_TO_SCROLL);
    }

    public void scrollToContact(String userName, final int maxUsersToScroll) throws Exception {
        final By locator = getAcceptButtonLocatorByUsername(userName);
        int ntry = 1;
        final int SCROLL_POS_START = 48;
        final int SCROLL_POS_END = 70;
        final int maxScrolls = maxUsersToScroll * (100 / (SCROLL_POS_END - SCROLL_POS_START) + 1);
        do {
            if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator, 1)) {
                return;
            }
            this.swipeByCoordinates(500, 50, SCROLL_POS_END, 50, SCROLL_POS_START);
            ntry++;
        } while (ntry <= maxScrolls);
        throw new RuntimeException(String.format("Failed to find user %s in the inbox after scrolling %s times!", userName, maxScrolls));
    }

    public void tapButton(String userName, String buttonName) throws Exception {
        scrollToContact(userName);
        By locator = getButtonLocator(userName, buttonName);
        getElement(locator, String.format("%s button for user '%s' is not visible", buttonName, userName)).click();
    }

    @Override
    protected String getUserNameId() {
        return idStrUserName;
    }

    @Override
    protected By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "ignore":
                return super.getIgnoreButtonLocator();
            case "connect":
                return super.getAcceptButtonLocator();
        }
        return super.getButtonLocatorByName(name);
    }

    private By getButtonLocator(String userName, String buttonName) {
        switch (buttonName.toLowerCase()) {
            case "connect":
                return getAcceptButtonLocatorByUsername(userName);
            case "ignore":
                return getIgnoreButtonLocatorByUserName(userName);
            default:
                throw new IllegalArgumentException(String.format("Cannot find the locator of button '%s'", buttonName));
        }
    }
}
