package com.wearezeta.auto.ios.pages.details_overlay;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

public abstract class BasePendingIncomingConnectionPage extends BaseUserDetailsOverlay
        implements IContainsProfilePicture {
    public BasePendingIncomingConnectionPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
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
