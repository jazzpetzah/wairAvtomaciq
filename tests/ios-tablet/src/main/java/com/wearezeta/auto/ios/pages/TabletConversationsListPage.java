package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.facebook_ios_driver.FBBy;
import com.wearezeta.auto.common.driver.facebook_ios_driver.FBElement;
import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;


public class TabletConversationsListPage extends ConversationsListPage {
    public TabletConversationsListPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public enum EntrySide {
        LEFT, RIGHT
    }

    public BufferedImage getConversationEntryScreenshot(EntrySide side, String name) throws Exception {
        final By entryLocator = FBBy.xpath(xpathStrConvoListEntryByName.apply(name));
        final FBElement entryElement = (FBElement) getElement(entryLocator,
                String.format("Conversation list entry '%s' is not visible", name));
        final BufferedImage entryScreenshot =
                this.getElementScreenshot(entryElement).orElseThrow(IllegalStateException::new);
        switch (side) {
            case LEFT:
                return entryScreenshot.getSubimage(
                        0, 0,
                        entryScreenshot.getWidth() / 4, entryScreenshot.getHeight()
                );
            case RIGHT:
                return entryScreenshot.getSubimage(
                        entryScreenshot.getWidth() * 3 / 4, 0,
                        entryScreenshot.getWidth() / 4, entryScreenshot.getHeight()
                );
            default:
                throw new IllegalArgumentException(String.format("Unsupported side value '%s'", side.name()));
        }
    }
}
