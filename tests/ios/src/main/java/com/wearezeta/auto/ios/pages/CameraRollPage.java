package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public class CameraRollPage extends IOSPage {
    private static final By fbXpathCameraLibraryFirstFolder =
            FBBy.xpath("(//XCUIElementTypeTable)[last()]/XCUIElementTypeCell");

    private static final By fbXpathLibraryFirstPicture =
            FBBy.xpath("//XCUIElementTypeCollectionView[@name='PhotosGridView']/XCUIElementTypeCell");

    private static final By fbXpathLibraryLastPicture =
            FBBy.xpath("//XCUIElementTypeCollectionView[@name='PhotosGridView']/XCUIElementTypeCell[last()]");

    private static final By xpathCameraRolCell = By.xpath("//XCUIElementTypeCell[@name='Camera Roll']");

    private static final By nameCancelButton = MobileBy.AccessibilityId("Cancel");

    public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);

        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
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

    public void tapFirstLibraryFolder() throws Exception {
        final FBElement btn = (FBElement) getElementIfExists(fbXpathCameraLibraryFirstFolder, 3).orElseThrow(
                () -> new IllegalStateException("Cannot find a library to select")
        );
        this.tapAtTheCenterOfElement(btn);
    }

    public void tapFirstImage() throws Exception {
        final FBElement btn = (FBElement) getElementIfExists(fbXpathLibraryFirstPicture).orElseThrow(
                () -> new IllegalStateException("Cannot find an image to select")
        );
        this.tapAtTheCenterOfElement(btn);
    }

    public void tapLastImage() throws Exception {
        final FBElement btn = (FBElement) getElementIfExists(fbXpathLibraryLastPicture).orElseThrow(
                () -> new IllegalStateException("Cannot find an image to select")
        );
        this.tapAtTheCenterOfElement(btn);
    }

    private static final long COUNT_VISIBILITY_TIMEOUT_MS = 5000;

    public Integer getCameraRollPhotoCount() throws Exception {
        final WebElement countLabel = getElement(xpathCameraRolCell);
        final long msStarted = System.currentTimeMillis();
        do {
            final String value = countLabel.getAttribute("value");
            if (value != null) {
                final String number = value.replaceAll("[\\D]", "");
                if (!number.isEmpty()) {
                    return Integer.valueOf(number);
                }
            }
            Thread.sleep(1000);
        } while (System.currentTimeMillis() - msStarted <= COUNT_VISIBILITY_TIMEOUT_MS);
        throw new IllegalStateException("Cannot read photos count value");
    }

    public void tapCancelButton() throws Exception {
        getElement(nameCancelButton).click();
    }
}
