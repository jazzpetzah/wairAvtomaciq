package com.wearezeta.auto.android.pages.registration;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;


public class BackendSelectPage extends AndroidPage {

    private static final By idBackendSelectDialog = By.id("select_dialog_listview");

    private static final Function<String, String> xpathBackendSelectButton = text -> String
            .format("//*[@value='%s']", text.toLowerCase());

    public BackendSelectPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void waitForInitialScreen() throws Exception {
        if (DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idBackendSelectDialog, 30)) {
            selectBackend();
        }
    }

    private void selectBackend() throws Exception {
        String backEndType = CommonUtils.getBackendType(getClass());
        By locator = By.xpath(xpathBackendSelectButton.apply(backEndType.toLowerCase()));
        getElement(locator, String.format("Cannot select backend '%s'", backEndType), 10).click();
    }
}
