package com.wearezeta.auto.common.driver.facebook_ios_driver;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByFBPredicate<T extends WebElement> {
    T findElementByFBPredicate(String var1);

    List<T> findElementsByFBPredicate(String var1);
}
