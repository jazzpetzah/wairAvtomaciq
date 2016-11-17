package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces.FunctionFor2Parameters;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class LikersPage extends IOSPage {

    private static final Function<String, String> xpathStrContactAvatarByName = name ->
            String.format("//XCUIElementTypeCell[.//XCUIElementTypeStaticText[@name='%s']]", name);

    private static final By nameCloseButton = MobileBy.AccessibilityId("BackButton");

    private static final FunctionFor2Parameters<String, Integer, String> xpathStrLikerByNameAndIndex = (index, name) ->
            String.format("//UIACollectionCell[%s][./UIAStaticText[@name='%s']]", index, name);

    public LikersPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean isLikerVisible(String name) throws Exception {
        final By locator = By.xpath(xpathStrContactAvatarByName.apply(name));
        return isLocatorDisplayed(locator);
    }

    public void tapCloseButton() throws Exception {
        getElement(nameCloseButton).click();
    }

    public boolean isLikerByPositionVisible(String name, int position) throws Exception {
        final By locator = By.xpath(xpathStrLikerByNameAndIndex.apply(position, name));
        return isLocatorDisplayed(locator);
    }
}
