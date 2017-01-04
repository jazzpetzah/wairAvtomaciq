package com.wearezeta.auto.ios.pages;

import java.util.Arrays;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.Dimension;

public class TabletConversationViewPage extends ConversationViewPage {

    private static final By nameOpenConversationDetails =
            MobileBy.AccessibilityId("ComposeControllerConversationDetailButton");

    private static final By fbXpathUploadMenu =
            FBBy.xpath("//*[@name='PopoverDismissRegion']/following-sibling::" +
                    "*[ .//XCUIElementTypeStaticText[@name='Record a video'] ]");

    public TabletConversationViewPage(Future<ZetaIOSDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void tapConversationDetailsIPadButton() throws Exception {
        getElement(nameOpenConversationDetails).click();
    }

    @Override
    public void tapFileTransferMenuItem(String itemName) throws Exception {
        itemName = expandFileTransferItemName(itemName);
        final FBElement uploadMenu = (FBElement) getElement(fbXpathUploadMenu);
        final Dimension menuSize = uploadMenu.getSize();
        // FIXME: Workaround for menu items positions
        final int itemIdx = Arrays.asList(UPLOAD_MENU_ITEMS).indexOf(itemName);
        if (itemIdx < 0) {
            throw new IllegalArgumentException(String.format("Unknown upload menu item '%s'", itemName));
        }
        uploadMenu.tap(menuSize.getWidth() / 8,
                menuSize.getHeight() / UPLOAD_MENU_ITEMS.length * itemIdx +
                        menuSize.getHeight() / UPLOAD_MENU_ITEMS.length / 2);
    }
}
