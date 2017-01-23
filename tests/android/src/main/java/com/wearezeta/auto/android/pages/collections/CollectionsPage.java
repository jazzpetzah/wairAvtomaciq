package com.wearezeta.auto.android.pages.collections;


import com.google.common.base.Strings;
import com.wearezeta.auto.android.pages.ConversationViewPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.misc.FunctionalInterfaces;
import com.wearezeta.auto.common.misc.Timedelta;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.concurrent.Future;
import java.util.function.Function;

public class CollectionsPage extends ConversationViewPage implements ISupportsCollectionTopToolbar {
    private static final Timedelta CATEGORY_ITEMS_VISIBILITY_CHECKING_INTRVAL = Timedelta.fromMilliSeconds(200);

    private static final int SHOW_ALL_LABEL_TOP_OFFSET = 10;

    //region Top toolbar
    private static final By idCloseActionButton = By.id("close");
    private static final By xpathToolBarNavigation =
            By.xpath(String.format("//*[@id='%s']/*[@value='' and count(*)=1]", "t_toolbar"));
    private static final String strIdToolbarName = "tv__collection_toolbar__name";
    private static final Function<String, String> xpathStrToolbarNameByValue = value ->
            String.format("//*[@id='%s'and @value='%s']", strIdToolbarName, value);

    private static final By xpathToolbarTimestamp =
            By.xpath("//*[@id='tv__collection_toolbar__timestamp' and string-length(@value) > 1]");
    //endregion

    private static final By idCollcectionsList = By.id("rv__collection");
    private static final By idItemUserName = By.id("ttv__collection_item__user_name");
    private static final By idItemTimestamp = By.id("ttv__collection_item__time");

    private static final By idNoItemPlaceholder = By.id("ll__collection__empty");

    private static final String strIdImage = "collection_image_view";
    private static final By idImage = By.id(strIdImage);

    private static final String strIdLinkPreviewContainer = "collection_link_preview_view";
    private static final By idLinkPreview = By.id(strIdLinkPreviewContainer);

    private static final String strIdFileContainer = "collection_file_view";
    private static final By idFile = By.id(strIdFileContainer);

    private static final FunctionalInterfaces.FunctionFor2Parameters<String, String, Integer> xpathItemByTypeAndNumber
            = (id, number) -> String.format("(//*[@id='%s'])[%d]", id, number);

    public CollectionsPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public boolean waitUntilItemIsVisible(String category) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), getLocatorByName(category));
    }

    public boolean waitUntilItemIsInvisible(String category) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), getLocatorByName(category));
    }

    public void tapOnItemByNumber(String tapType, int number, String itemType) throws Exception {
        WebElement el = getElement(getItemLocatorByNumber(itemType, number));
        switch (tapType.toLowerCase()) {
            case "tap":
                el.click();
                break;
            case "long tap":
                getDriver().longTap(el, DriverUtils.LONG_TAP_DURATION);
                break;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the tap type '%s'", tapType));
        }
    }

    public void tapShowAll(String categoryName) throws Exception {
        By locator = getItemLocatorByNumber(categoryName, 1);
        WebElement el = getElement(locator,
                String.format("The 'Show all' is only visible when you at least has '%s' items", categoryName));
        final Point elementLocation = el.getLocation();
        final int x = elementLocation.getX();
        final int y = elementLocation.getY() - SHOW_ALL_LABEL_TOP_OFFSET;
        getDriver().tap("tap", x, y);
    }

    public boolean waitUntilCountOfItemsByCategory(String category, int expectCount, Timedelta timeout) throws Exception {
        By locator = By.id(getItemLocationStr(category));
        return CommonUtils.waitUntilTrue(
                timeout,
                CATEGORY_ITEMS_VISIBILITY_CHECKING_INTRVAL,
                () -> getElements(locator).size() == expectCount
        );
    }

    public void tapOnButton(String buttonName) throws Exception {
        getElement(getButtonLocatorOnCollectionTopToolbar(buttonName)).click();
    }

    @Override
    public boolean waitUntilTopToolbarItemVisible(String itemType, String text) throws Exception {
        switch (itemType.toLowerCase()) {
            case "sender name":
                By locator = Strings.isNullOrEmpty(text) ? By.id(strIdToolbarName) : By.xpath(xpathStrToolbarNameByValue.apply(text));
                return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
            case "timestamp":
                return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), xpathToolbarTimestamp);
            default:
                throw new IllegalArgumentException(
                        String.format("Cannot identify the type '%s' on Collection top toolbar", itemType));
        }
    }

    protected static By getButtonLocatorOnCollectionTopToolbar(String buttonName) throws Exception {
        switch (buttonName.toLowerCase()) {
            case "close":
                return idCloseActionButton;
            case "back":
                return xpathToolBarNavigation;
            default:
                throw new IllegalArgumentException(
                        String.format("Cannot identify the button '%s' on Collection top toolbar", buttonName));
        }
    }

    private static String getItemLocationStr(String itemType) throws Exception {
        switch (itemType.toLowerCase()) {
            case "picture":
                return strIdImage;
            case "file sharing":
                return strIdFileContainer;
            case "link preview":
                return strIdLinkPreviewContainer;
            default:
                throw new IllegalArgumentException(String.format("Cannot identify the item type '%s'", itemType));
        }
    }

    private static By getItemLocatorByNumber(String itemType, int number) throws Exception {
        String strItemId = getItemLocationStr(itemType);
        return By.xpath(xpathItemByTypeAndNumber.apply(strItemId, number));
    }

    private By getLocatorByName(String categoryName) {
        switch (categoryName.toLowerCase()) {
            case "no item placeholder":
                return idNoItemPlaceholder;
            case "pictures category":
                return idImage;
            case "links category":
                return idLinkPreview;
            case "files category":
                return idFile;
            default:
                throw new IllegalArgumentException(String.format("Cannot find the locator for category '%s'",
                        categoryName));
        }
    }
}
