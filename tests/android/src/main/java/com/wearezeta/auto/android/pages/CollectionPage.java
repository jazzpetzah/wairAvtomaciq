package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

import static com.wearezeta.auto.common.driver.DriverUtils.waitUntilLocatorDissapears;
import static com.wearezeta.auto.common.driver.DriverUtils.waitUntilLocatorIsDisplayed;

public class CollectionPage extends ConversationViewPage {

    private static By idTitle = By.id("tv__collection_toolbar__content");

    public CollectionPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isTitleVisible() throws Exception {
        return waitUntilLocatorIsDisplayed(getDriver(), idTitle);
    }

    public boolean isTitleInvisible() throws Exception {
        return waitUntilLocatorDissapears(getDriver(), idTitle);
    }

    public boolean isCollectionNameVisible(String name) throws Exception {
        return waitUntilUserDataVisible("user name", name);
    }

    public boolean isCollectionNameInvisible(String name) throws Exception {
        return waitUntilUserDataInvisible("user name", name);
    }

}
