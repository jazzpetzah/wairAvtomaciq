package com.wearezeta.auto.web.pages.popovers;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SendInvitationPage extends AbstractPopoverPage {
	@FindBy(how = How.XPATH, using = PopoverLocators.SendInvitationPopover.SendInvitationPage.xpathInvitationText)
	private WebElement invitationText;

	public SendInvitationPage(Future<ZetaWebAppDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.SendInvitationPopover.SendInvitationPage.xpathInvitationText;
	}

	public String getInvitationText() {
		return invitationText.getText();
	}

	public void copyToClipboard() throws Exception {
		final ClassLoader classLoader = WebCommonUtils.class.getClassLoader();
		String srcScriptPath = null;
		InputStream scriptStream = null;
		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			scriptStream = classLoader.getResourceAsStream(String.format(
					"%s/%s", WebAppConstants.Scripts.RESOURCES_SCRIPTS_ROOT,
					WebAppConstants.Scripts.PRESS_CTRL_C_WIN));
			srcScriptPath = String.format("%s/%s", WebAppConstants.TMP_ROOT,
					WebAppConstants.Scripts.PRESS_CTRL_C_WIN);
		} else {
			scriptStream = classLoader.getResourceAsStream(String.format(
					"%s/%s", WebAppConstants.Scripts.RESOURCES_SCRIPTS_ROOT,
					WebAppConstants.Scripts.PRESS_CMD_C_MAC));
			srcScriptPath = String.format("%s/%s", WebAppConstants.TMP_ROOT,
					WebAppConstants.Scripts.PRESS_CMD_C_MAC);
		}
		final String dstScriptPath = srcScriptPath;
		try {
			FileUtils.copyInputStreamToFile(scriptStream, new File(
					srcScriptPath));
		} finally {
			scriptStream.close();
		}
		try {
			WebCommonUtils.putFileOnExecutionNode(getDriver().getNodeIp(),
					srcScriptPath, dstScriptPath);
		} finally {
			new File(srcScriptPath).delete();
		}

		if (WebAppExecutionContext.isCurrentPlatfromWindows()) {
			WebCommonUtils.executeVBScriptFileOnNode(getDriver().getNodeIp(),
					dstScriptPath);
		} else {
			WebCommonUtils.executeAppleScriptFileOnNode(
					getDriver().getNodeIp(), dstScriptPath);
		}
	}
}
