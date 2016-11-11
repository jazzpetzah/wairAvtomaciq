package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public class TakePicturePage extends AndroidPage {
    public static final By xpathTakePhotoButton = By
            .xpath("//*[@id='gtv__camera_control__take_a_picture' and @shown='true']");

    private static final By idCloseTakePictureViewButton = By.id("gtv__camera_control__take_a_picture");

    public static final By idChangePhotoBtn = By.id("gtv__camera_control__change_image_source");

    private static final By idGalleryCameraBtn = By.id("gtv__camera_control__gallery");

    private static final By idSwitchCameraButton = By.id("gtv__camera__top_control__change_camera");

    public static final By xpathConfirmOKButton = By.xpath("//*[@id='ttv__confirmation__confirm' and @value='OK']");

    public static final By xpathCancelButton = By.xpath("//*[@id='ttv__confirmation__cancel' and @value='CANCEL']");

    private static final By idSketchImagePaintButton = By.id("gtv__preview__drawing_button__sketch");
    private static final By idSketchEmojiPaintButton = By.id("gtv__preview__drawing_button__emoji");
    private static final By idSketchTextPaintButton = By.id("gtv__preview__drawing_button__text");

    public TakePicturePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapOnButton(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "take photo":
                takePhoto();
                break;
            case "change photo":
                tapChangePhotoButton();
                break;
            case "confirm":
                confirm();
                break;
            case "cancel":
                cancel();
                break;
            case "gallery camera":
                openGalleryFromCameraView();
                break;
            case "image close":
                closeFullScreenImage();
                break;
            case "close":
                tapCloseTakePictureViewButton();
                break;
            case "switch camera":
                if (!tapSwitchCameraButton()) {
                    throw new IllegalArgumentException(
                            "Device under test does not have front camera. " + "Skipping all the further verification...");
                }
                break;
            case "sketch image paint":
                tapSketchOnImageButton();
                break;
            case "sketch emoji paint":
                tapSketchEmojiOnImageButton();
                break;
            case "sketch text paint":
                tapSketchTextOnImageButton();
                break;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name: '%s'", buttonName));
        }
    }

    private void tapCloseTakePictureViewButton() throws Exception {
        getElement(idCloseTakePictureViewButton).click();
    }

    private void tapChangePhotoButton() throws Exception {
        getElement(idChangePhotoBtn).click();
    }

    public boolean isTakePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathTakePhotoButton);
    }

    public boolean isTakePhotoButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTakePhotoButton);
    }

    public boolean isChangePhotoButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idChangePhotoBtn);
    }

    public boolean isChangePhotoButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idChangePhotoBtn);
    }

    /**
     * @return false if Take Photo button is not visible after Switch Camera button is clicked
     * @throws Exception
     */
    private boolean tapSwitchCameraButton() throws Exception {
        getElement(idSwitchCameraButton).click();
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), xpathTakePhotoButton);
    }

    private void openGalleryFromCameraView() throws Exception {
        getElement(idGalleryCameraBtn, "Gallery within camera is still not visible").click();
    }

    private void closeFullScreenImage() throws Exception {
        // Sometimes X button is opened automatically after some timeout
        final int MAX_TRIES = 4;
        int ntry = 1;
        while (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idCloseImageBtn, 4) && ntry <= MAX_TRIES) {
            this.tapOnCenterOfScreen();
            ntry++;
        }
        getElement(idCloseImageBtn).click();
    }

    private void takePhoto() throws Exception {
        final WebElement btn = getElement(xpathTakePhotoButton, "Take Photo button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), btn)) {
            throw new IllegalStateException("Take Photo button is not clickable");
        }
        btn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathTakePhotoButton)) {
            throw new IllegalStateException("Take Photo button is still visible after being clicked");
        }
    }

    private void confirm() throws Exception {
        final WebElement okBtn = getElement(xpathConfirmOKButton, "OK button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), okBtn)) {
            throw new IllegalStateException("OK button is not clickable");
        }
        okBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathConfirmOKButton)) {
            okBtn.click();
        }
    }

    private void cancel() throws Exception {
        final WebElement cancelBtn = getElement(xpathCancelButton, "Cancel button is not visible");
        if (!DriverUtils.waitUntilElementClickable(getDriver(), cancelBtn)) {
            throw new IllegalStateException("Cancel button is not clickable");
        }
        cancelBtn.click();
        if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), xpathCancelButton)) {
            cancelBtn.click();
        }
    }

    private void tapSketchOnImageButton() throws Exception {
        getElement(idSketchImagePaintButton, "Draw sketch on image button is not visible").click();
    }

    private void tapSketchEmojiOnImageButton() throws Exception {
        getElement(idSketchEmojiPaintButton, "Draw emoji sketch on image button is not visible").click();
    }

    private void tapSketchTextOnImageButton() throws Exception {
        getElement(idSketchTextPaintButton, "Draw text sketch on image button is not visible").click();
    }

    public boolean isGalleryCameraButtonVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idGalleryCameraBtn);
    }

    public boolean isGalleryCameraButtonInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idGalleryCameraBtn);
    }

}
