package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.Function;

public class CollectionPage extends IOSPage {
    private static final Function<String, String> xpathStrCollectionCategoryHeaderByName = categoryName ->
            String.format("//XCUIElementTypeCollectionView/XCUIElementTypeOther/XCUIElementTypeStaticText[@name='%s']", categoryName);

    public CollectionPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    public boolean isCollectionCategoryVisible(String categoryName) throws Exception {
        final By locator = By.xpath(xpathStrCollectionCategoryHeaderByName.apply(categoryName));
        return isLocatorDisplayed(locator);
    }

    public boolean isCollectioncategoryInvisible(String categoryName) throws Exception {
        final By locator = By.xpath(xpathStrCollectionCategoryHeaderByName.apply(categoryName));
        return isLocatorInvisible(locator);
    }
}
