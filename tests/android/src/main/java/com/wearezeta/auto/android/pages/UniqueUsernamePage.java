package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class UniqueUsernamePage extends AndroidPage {

    private static final By idUsernameEdit = By.id("acet__change_username");

    private static final Function<String, By> usernameEditWithValue = (expectedValue)
            -> By.xpath(String.format("//*[@id='acet__change_username' and @value='%s']", expectedValue));

    private static final By idOkButton = By.id("tv__ok_button");

    public UniqueUsernamePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isUsernameEditVisible() throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), idUsernameEdit);
    }

    public boolean isUsernameEditInvisible() throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), idUsernameEdit);
    }

    public boolean isUsernameEditVisible(String expectedValue) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), usernameEditWithValue.apply(expectedValue));
    }

    public void enterNewUsername(String username) throws Exception {
        WebElement edit = getElement(idUsernameEdit);
        edit.clear();
        edit.sendKeys(username);
    }

    public String enterNewRandomUsername(int count) throws Exception {
        String newUniqueName = CommonUtils.generateRandomAlphanumericPlusUnderscoreString(count).toLowerCase();
        enterNewUsername(newUniqueName);
        return newUniqueName;
    }

    public void tapOkButton() throws Exception {
        getElement(idOkButton).click();
    }
}
