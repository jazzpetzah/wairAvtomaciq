package com.wearezeta.auto.ios.pages;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CollectionPage extends IOSPage {
    private static final By classCollectionViewRoot = By.className("XCUIElementTypeCollectionView");

    private static final By nameNoItemsPlaceholder = MobileBy.AccessibilityId("no items");

    private static final By nameBackButton = MobileBy.AccessibilityId("back");
    private static final By nameXbutton = MobileBy.AccessibilityId("close");

    private static final By fbNameFullScreenPage = FBBy.AccessibilityId("fullScreenPage");

    private static final BiFunction<String, Integer, String> xpathStrCollectionItemBuIndex = (categoryName, idx) ->
            String.format("//XCUIElementTypeStaticText[@name='%s']/" +
                    "ancestor::XCUIElementTypeCollectionView/XCUIElementTypeCell[%s]", categoryName, idx);

    private static final Function<Integer, String> xpathStrTilesByCount = count ->
            String.format("//XCUIElementTypeCollectionView[count(XCUIElementTypeCell)=%s]", count + 1);

    private static final Function<String, String> xpathStrShowAllByCategoryName = name ->
            String.format("//XCUIElementTypeStaticText[@name='%s']/" +
                    "preceding-sibling::XCUIElementTypeButton[@name='open all']", name);

    private static final By fbClassCollectionViewRoot = FBBy.className("XCUIElementTypeCollectionView");

    public CollectionPage(Future<ZetaIOSDriver> driver) throws Exception {
        super(driver);
    }

    public boolean isCollectionCategoryVisible(String categoryName) throws Exception {
        return isLocatorDisplayed(getElement(classCollectionViewRoot), MobileBy.AccessibilityId(categoryName));
    }

    public boolean isCollectioncategoryInvisible(String categoryName) throws Exception {
        return isLocatorDisplayed(getElement(classCollectionViewRoot), MobileBy.AccessibilityId(categoryName));
    }

    public boolean isNoItemsPlaceholderVisible() throws Exception {
        return isLocatorDisplayed(nameNoItemsPlaceholder);
    }

    public void tapCategoryItemByIndex(String categoryName, int index) throws Exception {
        final By locator = By.xpath(xpathStrCollectionItemBuIndex.apply(categoryName, index));
        getElement(locator).click();
    }

    public boolean isFullScreenImagePreviewVisible() throws Exception {
        return isLocatorDisplayed(fbNameFullScreenPage);
    }

    private static By getButtonLocatorByName(String name) {
        switch (name.toLowerCase()) {
            case "back":
                return nameBackButton;
            case "x":
                return nameXbutton;
            default:
                throw new IllegalArgumentException(String.format("Unknown button name '%s'", name));
        }
    }

    public void tapButton(String name) throws Exception {
        final By locator = getButtonLocatorByName(name);
        getElement(locator).click();
    }

    public boolean isTilesCountEqualTo(int expectedCount) throws Exception {
        final By locator = By.xpath(xpathStrTilesByCount.apply(expectedCount));
        return isLocatorDisplayed(locator);
    }

    public void tapShowAllLabel(String categoryName) throws Exception {
        final By locator = By.xpath(xpathStrShowAllByCategoryName.apply(categoryName));
        getElement(locator).click();
    }

    public void scroll(String direction) throws Exception {
        final FBElement root = (FBElement) getElement(fbClassCollectionViewRoot);
        switch (direction.toLowerCase()) {
            case "up":
                root.scrollUp();
                break;
            case "down":
                root.scrollDown();
                break;
            default:
                throw new IllegalArgumentException(String.format("'%s' scroll direction is not supported", direction));
        }
    }

    public void swipeOnImageFullscreen(String direction) throws Exception {
        final FBElement image = (FBElement) getElement(fbNameFullScreenPage);
        switch (direction.toLowerCase()) {
            case "right":
                image.scrollLeft();
                return;
            case "left":
                image.scrollRight();
                return;
            default:
                throw new IllegalArgumentException(String.format("Unknown swipe direction '%s'", direction));
        }
    }
}
