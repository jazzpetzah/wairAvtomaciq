package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;

public class CollectionPage extends IOSPage {

    private static final By classCollectionViewRoot = By.className("XCUIElementTypeCollectionView");

    public CollectionPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    public boolean isCollectionCategoryVisible(String categoryName) throws Exception {
        final WebElement category = getElement(classCollectionViewRoot).findElement(
                MobileBy.AccessibilityId(categoryName));
        return isElementVisible(category);
    }

    public boolean isCollectioncategoryInvisible(String categoryName) throws Exception {
        final WebElement category = getElement(classCollectionViewRoot).findElement(
                MobileBy.AccessibilityId(categoryName));
        return isElementInvisible(category);
    }
}
