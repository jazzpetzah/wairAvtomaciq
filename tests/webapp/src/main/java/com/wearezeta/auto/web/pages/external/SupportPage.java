package com.wearezeta.auto.web.pages.external;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class SupportPage extends WebPage {

    @FindBy(css = ".writeSupport")
    private WebElement askSupportLink;

    public SupportPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isAskSupportVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(getDriver(), askSupportLink);
    }
}
