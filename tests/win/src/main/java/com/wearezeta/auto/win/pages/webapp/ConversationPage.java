package com.wearezeta.auto.win.pages.webapp;

import com.wearezeta.auto.common.driver.ZetaOSXWebAppDriver;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import static com.wearezeta.auto.win.common.WinConstants.Scripts.PASTE_SCRIPT;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.concurrent.Future;

import org.apache.log4j.Logger;

public class ConversationPage extends
		com.wearezeta.auto.web.pages.ConversationPage {

	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger
			.getLog(com.wearezeta.auto.web.pages.ConversationPage.class
					.getName());

	// TODO hide behind driver impl
	private final Robot robot = new Robot();

	public ConversationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	@Override
	public void pressShortCutForPing() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_K);
		robot.keyRelease(KeyEvent.VK_K);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForUndo() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_Z);
		robot.keyRelease(KeyEvent.VK_Z);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForRedo() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_SHIFT);
		robot.keyPress(KeyEvent.VK_Z);
		robot.keyRelease(KeyEvent.VK_Z);
		robot.keyRelease(KeyEvent.VK_SHIFT);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForSelectAll() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_A);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForCut() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_X);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	public void pressShortCutForPaste() throws Exception {
		String script = new String(Files.readAllBytes(Paths.get(getClass()
				.getResource(PASTE_SCRIPT).toURI())));
		((ZetaOSXWebAppDriver) getDriver()).getOsxDriver()
				.executeScript(script);
	}

	public void pressShortCutForCopy() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_C);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}

	@Override
	public void pressShortCutForCall() throws Exception {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_R);
		robot.keyRelease(KeyEvent.VK_R);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
}
