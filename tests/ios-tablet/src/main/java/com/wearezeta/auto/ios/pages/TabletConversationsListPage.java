package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;


public class TabletConversationsListPage extends ConversationsListPage {
    protected static final Function<String, String> xpathStrConvoListTitleByName = name ->
            String.format("%s/XCUIElementTypeStaticText[@value='%s']", xpathStrContactListItems, name);

    public TabletConversationsListPage(Future<ZetaIOSDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
    }

    public enum EntrySide {
        LEFT, RIGHT
    }

    public BufferedImage getConversationEntryScreenshot(EntrySide side, String name) throws Exception {
        final By entryLocator = By.xpath(xpathStrConvoListEntryByName.apply(name));
        final WebElement entryElement = getElement(entryLocator,
                String.format("Conversation list entry '%s' is not visible", name));
        final Point entryLocation = entryElement.getLocation();
        final Dimension entryDimension = entryElement.getSize();
        final BufferedImage entryScreenshot =
                this.getElementScreenshot(entryElement).orElseThrow(IllegalStateException::new);
        final By titleLocator = By.xpath(xpathStrConvoListTitleByName.apply(name));
        final WebElement titleElement = getElement(titleLocator);
        final Point titleLocation = titleElement.getLocation();
        final Dimension titleDimension = titleElement.getSize();
//        ImageIO.write(entryScreenshot, "png", new File(System.getProperty("user.home")
//                + "/Desktop/screen_" + System.currentTimeMillis() + ".png"));
        switch (side) {
            case LEFT:
                return entryScreenshot.getSubimage(
                        0, titleLocation.y - entryLocation.y,
                        entryDimension.width / 4, titleDimension.height);
            case RIGHT:
                return entryScreenshot.getSubimage(
                        entryDimension.width * 3 / 4, titleLocation.y - entryLocation.y,
                        entryDimension.width / 4, titleDimension.height);
            default:
                throw new IllegalArgumentException(String.format("Unsupported side value '%s'", side.name()));
        }
    }
}
