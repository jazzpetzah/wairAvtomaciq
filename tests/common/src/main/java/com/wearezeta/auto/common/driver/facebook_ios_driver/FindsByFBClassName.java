package com.wearezeta.auto.common.driver.facebook_ios_driver;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByFBClassName<T extends WebElement> {
    T findElementByFBClassName(String var1);

    List<T> findElementsByFBClassName(String var1);
}
