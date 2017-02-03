package com.wearezeta.auto.android.pages.collections;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CollectionImagePreviewPage extends CollectionsPage implements ISupportsCollectionTopToolbar {
    private static final By idPreviewRoot = By.id("collection_image_view");

    private static final By idLikeButton = By.id("toolbar_like");
    private static final By idDownloadButton = By.id("toolbar_download");
    private static final By idShareButton = By.id("toolbar_share");
    private static final By idDeleteButton = By.id("toolbar_delete");
    private static final By idViewButton = By.id("toolbar_view");

    public CollectionImagePreviewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idPreviewRoot);
    }

    public boolean waitUntilInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idPreviewRoot);
    }

    public void tapOnButton(String buttonName) throws Exception {
        getElement(getToolbarLocator(buttonName)).click();
    }

    private static By getToolbarLocator(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "like":
                return idLikeButton;
            case "download":
                return idDownloadButton;
            case "share":
                return idShareButton;
            case "delete":
                return idDeleteButton;
            case "view":
                return idViewButton;
            case "close":
            case "back":
                return getButtonLocatorOnCollectionTopToolbar(buttonName);
            default:
                throw new IllegalArgumentException(String.format("Cannot fint the locator for '%s'", buttonName));
        }
    }

}
