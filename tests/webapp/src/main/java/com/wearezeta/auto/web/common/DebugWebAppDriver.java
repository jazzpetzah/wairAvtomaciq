package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;

public class DebugWebAppDriver extends ZetaWebAppDriver {

    public DebugWebAppDriver(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    protected WebElement findElement(String by, String using) {
        final WebElement el = super.findElement(by, using);
        WebCommonUtils.highlightElement(this, el, 500, TimeUnit.MILLISECONDS);
        return el;
    }

}
