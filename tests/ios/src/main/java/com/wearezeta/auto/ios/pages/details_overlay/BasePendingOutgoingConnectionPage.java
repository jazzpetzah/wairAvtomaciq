package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

public abstract class BasePendingOutgoingConnectionPage extends BaseUserDetailsOverlay
        implements IContainsProfilePicture {
    protected static final By xpathConnectOtherUserButton =
            By.xpath("(//XCUIElementTypeButton[@label='CONNECT'])[last()]");

    protected static final By nameArchiveRequestButton =
            MobileBy.AccessibilityId("archive connection");

    protected static final By xpathCancelRequestButton =
            By.xpath("(//XCUIElementTypeButton[@label='CANCEL REQUEST' or @name='cancel connection'])[last()]");

    public BasePendingOutgoingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public void tapButton(String name) throws Exception {
        super.tapButton(name);
        // Wait for animation
        Thread.sleep(2000);
    }

    @Override
    public BufferedImage getProfilePictureScreenshot() throws Exception {
        return super.getProfilePictureScreenshot();
    }
}
