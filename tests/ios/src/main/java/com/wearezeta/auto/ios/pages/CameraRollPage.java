package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraRollPage extends IOSPage {
    private static final By xpathCameraLibraryFirstFolder =
            By.xpath("(//XCUIElementTypeTable)[last()]/XCUIElementTypeCell");

    private static final By xpathLibraryFirstPicture = By.xpath("//XCUIElementTypeCollectionView/XCUIElementTypeCell");

    private static final By xpathCameraRolCell = By.xpath("//XCUIElementTypeCell[@name='Camera Roll']");

    private static final By nameCancelButton = MobileBy.AccessibilityId("Cancel");

    public CameraRollPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);

        if (CommonUtils.getIsSimulatorFromConfig(getClass())) {
            IOSSimulatorHelper.uploadImage();
        }
    }

    public void selectFirstPicture() throws Exception {
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
