package com.wearezeta.auto.osx.pages.osx;

import com.wearezeta.auto.common.driver.ZetaOSXDriver;
import java.util.concurrent.Future;

import com.wearezeta.auto.osx.locators.OSXLocators;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ContactContextMenuPage extends OSXPage {

	private static final int CONTEXT_ARCHIVE_INDEX = 1;
	private static final int CONTEXT_SILENCEI_INDEX = 2;
	private static final int CONTEXT_DELETE_INDEX = 3;
	private static final int CONTEXT_BLOCK_INDEX = 4;
	private static final int CONTEXT_LEAVE_INDEX = 4;

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	@FindBy(xpath = OSXLocators.ContactListContextMenuPage.xpathContextBlock)
	private WebElement contextBlock;

	public ContactContextMenuPage(Future<ZetaOSXDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	/**
	 * It's not possible to locate elements inside of the context menu thus we
	 * have to implement it this way :/
	 *
	 * @throws Exception
	 */
	public void clickArchive() throws Exception {
		selectByIndex(CONTEXT_ARCHIVE_INDEX, 1000);
	}

	public void clickSilence() throws Exception {
		selectByIndex(CONTEXT_SILENCEI_INDEX, 1000);
	}

	public void clickNotify() throws Exception {
		selectByIndex(CONTEXT_SILENCEI_INDEX, 1000);
	}

	public void clickDelete() throws Exception {
		selectByIndex(CONTEXT_DELETE_INDEX, 1000);
	}

	public void clickBlock() throws Exception {
		selectByIndex(CONTEXT_BLOCK_INDEX, 1000);
	}

	public void clickLeave() throws Exception {
		selectByIndex(CONTEXT_LEAVE_INDEX, 1000);
	}

	private void selectByIndex(int index, long wait)
			throws InterruptedException {
		Thread.sleep(wait);
		for (int i = 0; i < index; i++) {
			robot.keyPress(KeyEvent.VK_DOWN);
			Thread.sleep(wait);
		}
		robot.keyPress(KeyEvent.VK_ENTER);
	}

}
