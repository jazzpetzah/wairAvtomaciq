package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.tools.IOSSimulatorHelper;
import org.openqa.selenium.By;

import java.util.concurrent.Future;

public class CameraRollPage extends IOSPage {
    private static final By xpathCameraLibraryFirstFolder = By.xpath("(//UIATableView)[last()]/UIATableCell");

    private static final By xpathLibraryFirstPicture = By.xpath("//UIACollectionView/UIACollectionCell");

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
}
