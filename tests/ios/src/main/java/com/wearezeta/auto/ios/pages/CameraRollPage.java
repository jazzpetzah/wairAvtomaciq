package com.wearezeta.auto.ios.pages;

import java.io.File;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;

import static com.wearezeta.auto.common.CommonUtils.getSimulatorImagesPathFromConfig;

public class CameraRollPage extends IOSPage {
    private static final String TESTING_IMAGE_NAME = "testing.jpg";

    private static final By nameCameraLibraryButton = MobileBy.AccessibilityId("cameraLibraryButton");

    private static final By xpathCameraLibraryFirstFolder = By.xpath("//UIATableCell[@name='Moments']");

    private static final By xpathLibraryFirstPicture = By.xpath("//UIACollectionCell[starts-with(@name, 'Photo,')]");

    private static final By xpathLibraryLastPicture = By.xpath("//UIACollectionView/UIACollectionCell[last()]");

    private static final By xpathConfirmPictureButton = By.xpath("//UIAButton[@name='OK' and @visible='true']");

    private static final By nameCameraRollSketchButton = MobileBy.AccessibilityId("editNotConfirmedImageButton");

    public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);

        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage(new File(
                    getSimulatorImagesPathFromConfig(getClass()) + File.separator + TESTING_IMAGE_NAME));
            // Let Simulator to update the lib
            Thread.sleep(3000);
        }
    }

    public void pressSelectFromLibraryButton() throws Exception {
        getElement(nameCameraLibraryButton).click();
    }

    public void selectImageFromLibrary() throws Exception {
        try {
            clickFirstLibraryFolder();
        } catch (IllegalStateException e) {
            // Ignore silently
        }

        clickFirstImage();
    }

    public void clickFirstLibraryFolder() throws Exception {
        DriverUtils.getElementIfPresentInDOM(getDriver(), xpathCameraLibraryFirstFolder).
                orElseThrow(() -> new IllegalStateException("Cannot find a library to select")).click();
    }

    public void clickFirstImage() throws Exception {
        DriverUtils.getElementIfPresentInDOM(getDriver(), xpathLibraryFirstPicture).
                orElseThrow(() -> new IllegalStateException("Cannot find an image to select")).click();
    }

    public void clickLastImage() throws Exception {
        DriverUtils.getElementIfPresentInDOM(getDriver(), xpathLibraryLastPicture).
                orElseThrow(() -> new IllegalStateException("Cannot find an image to select")).click();
    }

    public void pressConfirmButton() throws Exception {
        getElement(xpathConfirmPictureButton).click();
    }

    public void clickCameraRollSketchButton() throws Exception {
        getElement(nameCameraRollSketchButton).click();
    }

}
