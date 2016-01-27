package com.wearezeta.auto.android_tablet.pages.camera;

import java.util.Optional;
import java.util.concurrent.Future;

import gherkin.lexer.Th;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android_tablet.common.ScreenOrientationHelper;
import com.wearezeta.auto.android_tablet.pages.AndroidTabletPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public abstract class AbstractCameraPage extends AndroidTabletPage {
    public static final By idGalleryButton = By.id("gtv__camera_control__pick_from_gallery");

    public static final By idTakePhotoButton = By.id("gtv__camera_control__take_a_picture");

    public AbstractCameraPage(Future<ZetaAndroidDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    protected abstract By getLensButtonLocator();

    public boolean waitUntilVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
                getLensButtonLocator(), 20);
    }

    public void tapLensButton() throws Exception {
        this.getDriver().findElement(getLensButtonLocator()).click();
    }

    public void tapTakePhotoButton() throws Exception {
        getElement(idTakePhotoButton, "Take Photo button is not visible", 15).click();
    }

    public boolean waitUntilTakePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idTakePhotoButton);
    }

    private boolean isGalleryModeActivated = false;

    public void confirmPictureSelection() throws Exception {
        Thread.sleep(1500);
        final Optional<WebElement> confirmButton = getElementIfDisplayed(DialogPage.xpathConfirmOKButton);
        if (confirmButton.isPresent()) {
            confirmButton.get().click();
        } else {
            // Workaround for unexpected orientation change issue
            final Optional<WebElement> takePhotoButton = getElementIfDisplayed(idTakePhotoButton);
            if (takePhotoButton.isPresent()) {
                if (isGalleryModeActivated) {
                    tapGalleryButton();
                    isGalleryModeActivated = false;
                } else {
                    takePhotoButton.get().click();
                }
                getElement(DialogPage.xpathConfirmOKButton,
                        "Picture selection confirmation has not been shown after the timeout", 5).click();
            } else {
                final Optional<WebElement> lensButton = getElementIfDisplayed(getLensButtonLocator());
                if (lensButton.isPresent()) {
                    lensButton.get().click();
                    if (isGalleryModeActivated) {
                        tapGalleryButton();
                        isGalleryModeActivated = false;
                    } else {
                        tapTakePhotoButton();
                    }
                    getElement(DialogPage.xpathConfirmOKButton,
                            "Picture selection confirmation has not been shown after the timeout", 5).click();
                }
            }
        }
        Thread.sleep(1500);
        ScreenOrientationHelper.getInstance().fixOrientation(getDriver());
    }

    public void tapGalleryButton() throws Exception {
        getElement(idGalleryButton, "Open gallery button is not visible", 15).click();
        isGalleryModeActivated = true;
    }

}
