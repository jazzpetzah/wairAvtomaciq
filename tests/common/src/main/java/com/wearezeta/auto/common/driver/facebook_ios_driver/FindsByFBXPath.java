package com.wearezeta.auto.common.driver.facebook_ios_driver;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByFBXPath<T extends WebElement> {
    T findElementByFBXPath(String var1);

    List<T> findElementsByFBXPath(String var1);
}
