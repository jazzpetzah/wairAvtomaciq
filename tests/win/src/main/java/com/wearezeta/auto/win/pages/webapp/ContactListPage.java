package com.wearezeta.auto.win.pages.webapp;

import com.wearezeta.auto.win.pages.win.ContactContextMenuPage;
import com.wearezeta.auto.win.pages.win.WinPagesCollection;
import com.wearezeta.auto.common.driver.DriverUtils;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.locators.WebAppLocators;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

public class ContactListPage extends
		com.wearezeta.auto.web.pages.ContactListPage {

	private static final Logger LOG = ZetaLogger.getLog(ContactListPage.class
			.getSimpleName());

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	private final WinPagesCollection osxPagesCollection = WinPagesCollection
			.getInstance();

	@FindBy(how = How.CSS, using = WebAppLocators.ContactListPage.cssSelfProfileAvatar)
	private WebElement selfProfileAvatar;

	@FindBy(how = How.XPATH, using = WebAppLocators.ContactListPage.xpathActiveConversationEntry)
	private WebElement activeConversationEntry;

	public ContactListPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public ContactContextMenuPage openContextMenuForContact(String name)
			throws Exception {
		LOG.debug("Looking for contact with name '" + name + "'");
		if (name == null) {
			throw new Exception(
					"The name to look for in the conversation list was null");
		}
		name = fixDefaultGroupConvoName(name, false, false);
		final String locator = WebAppLocators.ContactListPage.cssContactListEntryByName
				.apply(name);
		DriverUtils.waitUntilLocatorIsDisplayed(this.getDriver(),
				By.cssSelector(locator));
		WebElement entry = getDriver().findElement(By.cssSelector(locator));
		new Actions(getDriver()).contextClick(entry).perform();
		return osxPagesCollection.getPage(ContactContextMenuPage.class);
	}

	public String getActiveConversationName() throws Exception {
		return activeConversationEntry.getText();
	}

	public int getActiveConversationIndex() throws Exception {
		return getItemIndex(activeConversationEntry.getText());
	}

	public void pressShortCutToMute() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_S);
		robot.keyRelease(KeyEvent.VK_ALT);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutToArchive() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_D);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForNextConv() throws Exception {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_UP);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public void pressShortCutForPrevConv() throws Exception {
		robot.keyPress(KeyEvent.VK_ALT);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_DOWN);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_ALT);
	}

	public boolean isConversationNotMuted(String conversationName)
			throws Exception {
		// moving focus from contact - to now show ... button
		// do nothing (safari workaround)
		if (WebAppExecutionContext.getBrowser()
				.isSupportingNativeMouseActions()) {
			DriverUtils.moveMouserOver(this.getDriver(), selfProfileAvatar);
		}
		return DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathMuteIconByContactName
								.apply(conversationName)));
	}

	public void waitUntilArchiveButtonIsNotVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorDissapears(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is still visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}

	public void waitUntilArchiveButtonIsVisible(int archiveBtnVisilityTimeout)
			throws Exception {
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(
						this.getDriver(),
						By.xpath(WebAppLocators.ContactListPage.xpathOpenArchivedConvosButton),
						archiveBtnVisilityTimeout) : "Open Archive button is not visible after "
				+ archiveBtnVisilityTimeout + " second(s)";
	}

	public void pressShortCutToSearch() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_N);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

}
