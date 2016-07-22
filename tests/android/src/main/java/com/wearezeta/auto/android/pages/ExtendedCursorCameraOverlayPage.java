package com.wearezeta.auto.android.pages;


import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class ExtendedCursorCameraOverlayPage extends AndroidPage {

    private static final By idExtendedCursorContainer = By.id("ecc__conversation");

    private static final By idCameraContainer = By.id("");

    private static final By idCameraSwitchButton = By.id("gtv__cursor_image__front_back");

    private static final By idOpenExternalCameraButton = By.id("gtv__cursor_image__open_camera");

    private static final By idOpenExternalVideoButton = By.id("gtv__cursor_image__open_video");

    private static final By idTakePictureButton = By.id("gtv__cursor_image__take_picture");

    private static final By idNavigationOpenGalleryButton = By.id("gtv__cursor_image__nav_open_gallery");

    private static final By idNavigationCameraBackButton = By.id("gtv__cursor_image__nav_camera_back");

    private static final FunctionalInterfaces.FunctionFor2Parameters<String, Integer,  Integer>
            xpathStrGalleryImageItem = (row, column)
            -> String.format("(//*[@id='iv__cursor_gallery_item%d'])[%d]", row, column);


    public ExtendedCursorCameraOverlayPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnButton(String buttonName) throws Exception {
        By locator = getButtonLocator(buttonName);
        getElement(locator, String.format("The button %s is not visible in extended cursor camera layout", buttonName))
                .click();
    }

    public boolean waitUntilOverlayVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idExtendedCursorContainer);
    }

    public boolean waitUntilOverlayInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idExtendedCursorContainer);
    }

    private By getButtonLocator(String buttonName) {
        switch(buttonName.toLowerCase()) {
            case "take photo":
                return idTakePictureButton;
            case "switch camera":
                return idCameraSwitchButton;
            case "gallery":
                return idNavigationOpenGalleryButton;
            default:
                throw new IllegalStateException(
                        String.format("Do not support button %s in extended cursor camera layout", buttonName));
        }
    }

}
