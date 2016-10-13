package com.wearezeta.auto.web.pages;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AboutPage extends WebPage {

    private static final Logger log = ZetaLogger.getLog(AboutPage.class.getSimpleName());

    @FindBy(css = "[data-uie-name='go-support']")
    private WebElement supportLink;

    public AboutPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickSupport() throws Exception {
        // remove target="_blank" from link
        String script = "arguments[0].target='';";
        Object element = getDriver().executeScript(script, supportLink);
        supportLink.click();
    }
}
