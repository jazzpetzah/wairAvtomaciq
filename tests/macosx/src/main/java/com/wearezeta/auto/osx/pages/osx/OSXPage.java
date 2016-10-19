package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;

public abstract class OSXPage extends BasePage {

    public static final Logger LOG = ZetaLogger.getLog(OSXPage.class.getName());

    private String path = null;

    @Override
    protected ZetaOSXDriver getDriver() throws Exception {
        return (ZetaOSXDriver) super.getDriver();
    }

    public OSXPage(Future<ZetaOSXDriver> osxDriver) throws Exception {
        super(osxDriver);
    }

    public OSXPage(Future<ZetaOSXDriver> osxDriver, String path) throws Exception {
        super(osxDriver);
        this.path = path;
    }

    public void navigateTo() throws Exception {
        if (this.path == null) {
            throw new RuntimeException(String.format(
                    "The page %s does not support direct navigation", this
                    .getClass().getName()));
        }
        this.getDriver().navigate().to(this.path);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    public Optional<BufferedImage> getScreenshot() throws Exception {
        return DriverUtils.takeFullScreenShot(getDriver());
    }
}
