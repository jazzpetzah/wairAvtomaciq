package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.device_helpers.IOSSimulatorHelpers;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraRollPage extends IOSPage {
    private static final By xpathCameraLibraryFirstFolder =
            By.xpath("(//XCUIElementTypeTable)[last()]/XCUIElementTypeCell");

    private static final By xpathLibraryFirstPicture =
            By.xpath("//XCUIElementTypeCollectionView[@name='PhotosGridView']/XCUIElementTypeCell");

    private static final By xpathLibraryLastPicture =
            By.xpath("//XCUIElementTypeCollectionView[@name='PhotosGridView']/XCUIElementTypeCell[last()]");

    private static final By xpathCameraRolCell =
            By.xpath("//XCUIElementTypeCell[@name='Camera Roll' and boolean(string(@value))]");

    private static final By nameCancelButton = MobileBy.AccessibilityId("Cancel");

    public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);

        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelpers.uploadImage();
        }
    }

    public void selectAnyPicture() throws Exception {
        boolean shouldSelectLastPicture = true;
        try {
            tapFirstLibraryFolder();
            // Wait for the animation
            Thread.sleep(1000);
        } catch (IllegalStateException e) {
            shouldSelectLastPicture = false;
        }

        if (shouldSelectLastPicture) {
            tapLastImage();
        } else {
            tapFirstImage();
        }
        // Wait for the animation
        Thread.sleep(2000);
    }

    private void tapFirstLibraryFolder() throws Exception {
        getElementIfExists(xpathCameraLibraryFirstFolder, 3).orElseThrow(
                () -> new IllegalStateException("Cannot find a library to select")
        ).click();
    }

    private void tapFirstImage() throws Exception {
        getElementIfExists(xpathLibraryFirstPicture).orElseThrow(
                () -> new IllegalStateException("Cannot find an image to select")
        ).click();
    }

    private void tapLastImage() throws Exception {
        getElementIfExists(xpathLibraryLastPicture).orElseThrow(
                () -> new IllegalStateException("Cannot find an image to select")
        ).click();
    }

    private String getCameraRollCellValue() throws Exception {
        return getElement(xpathCameraRolCell).getAttribute("value");
    }

    public Integer getCameraRollPhotoCount() throws Exception {
        String countToParse = getCameraRollCellValue();
        String count = countToParse.replaceAll("[\\D]", "");
        return Integer.valueOf(count);
    }

    public void tapCancelButton() throws Exception {
        getElement(nameCancelButton).click();
    }
}
