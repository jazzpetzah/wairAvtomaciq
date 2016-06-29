package com.wearezeta.auto.web.pages;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public class HistoryInfoPage extends WebPage {

    public static final String cssConfirmButton = "[data-uie-name='do-history-confirm']";

    @FindBy(css = cssConfirmButton)
    WebElement confirmButton;

    public HistoryInfoPage(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public boolean isConfirmButtonVisible() throws Exception {
        return DriverUtils.waitUntilElementClickable(getDriver(), confirmButton);
    }

    public void clickConfirmButton() {
        confirmButton.click();
    }
}
