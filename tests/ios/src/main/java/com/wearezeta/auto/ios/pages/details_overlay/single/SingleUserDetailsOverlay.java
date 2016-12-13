package com.wearezeta.auto.ios.pages.details_overlay.single;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.pages.details_overlay.BaseUserDetailsOverlay;
import com.wearezeta.auto.ios.pages.details_overlay.IContainsProfilePicture;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

public abstract class SingleUserDetailsOverlay extends BaseUserDetailsOverlay implements IContainsProfilePicture {
    public SingleUserDetailsOverlay(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    @Override
    public BufferedImage getProfilePictureScreenshot() throws Exception {
        return super.getProfilePictureScreenshot();
    }
}
