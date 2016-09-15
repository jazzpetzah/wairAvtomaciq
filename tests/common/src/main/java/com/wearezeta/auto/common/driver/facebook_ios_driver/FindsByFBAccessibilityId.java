package com.wearezeta.auto.common.driver.facebook_ios_driver;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByFBAccessibilityId<T extends WebElement> {
    T findElementByFBAccessibilityId(String var1);

    List<T> findElementsByFBAccessibilityId(String var1);
}
