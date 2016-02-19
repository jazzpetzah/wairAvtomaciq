package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class TabletContactListPage extends ContactListPage {
	private static final By xpathConversationListPage =
			By.xpath("//UIAApplication[1]/UIAWindow[2]/UIACollectionView[1]");

    protected static final Function<String, String> xpathStrConvoListTitleByName = name ->
            String.format("%s/UIAStaticText[@value='%s']", xpathStrContactListItems, name);

	public TabletContactListPage(Future<ZetaIOSDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public void swipeDown(int time) throws Exception {
        DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationListPage), time,
                20, 15, 20, 90);
	}

	@Override
	public void swipeUp(int time) throws Exception {
		DriverUtils.swipeElementPointToPoint(this.getDriver(), getElement(xpathConversationListPage), time,
				20, 90, 20, 10);
	}

    @Override
    public BufferedImage getConversationEntryScreenshot(String name) throws Exception {
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
        return entryScreenshot.getSubimage(
                0, titleLocation.y - entryLocation.y,
                entryDimension.width / 2, titleDimension.height);
        //ImageIO.write(resultImage, "png", new File("/Users/elf/Desktop/screen_" + System.currentTimeMillis() + ".png"));
        // return resultImage;
    }
}
