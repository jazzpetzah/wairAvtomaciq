package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.usrmgmt.PhoneNumber;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;


public class WelcomePage extends AndroidPage {
    public static final By idScreenRoot = By.id("fl_main_content");

    private static final By idPhoneInputField = By.id("et__reg__phone");

    private static final By xpathSignInTab = By.xpath("//*[@id='til__app_entry']//*[*][1]");

    private static final By idAreaCodeSelector = By.id("tv__country_code");

    private static final By idPhoneConfirmationButton = By.id("pcb__signup");

    private static final By idBackendSelectDialog = By.id("select_dialog_listview");

    private static final Function<String, String> xpathBackendSelectButton = text -> String
            .format("//*[@value='%s']", text.toLowerCase());

    public WelcomePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void inputPhoneNumber(PhoneNumber number) throws Exception {
        final WebElement phoneInputField = getElement(idPhoneInputField);
        phoneInputField.clear();
        phoneInputField.sendKeys(number.withoutPrefix());
    }

    public void clickConfirm() throws Exception {
        getElement(idPhoneConfirmationButton).click();
    }

    public void tapSignInTab() throws Exception {
        final WebElement signInTab = getElement(xpathSignInTab, "Sign In tab is not visible", 30);
        signInTab.click();
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), EmailSignInPage.idLoginInput)) {
            signInTab.click();
        }
    }

    public boolean waitForInitialScreen() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(), idScreenRoot, 30);
    }

    public void clickAreaCodeSelector() throws Exception {
        final WebElement areaCodeSelector = getElement(idAreaCodeSelector, "Area code selector is not visible");
        areaCodeSelector.click();
        if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), AreaCodePage.idCode)) {
            areaCodeSelector.click();
        }
    }
}
