package com.wearezeta.auto.common.driver.facebook_ios_driver;

import org.openqa.selenium.WebElement;

import java.util.List;

public interface FindsByFBClassChain<T extends WebElement> {
    T findElementByFBClassChain(String var1);

    List<T> findElementsByFBClassChain(String var1);
}
