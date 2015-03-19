package com.wearezeta.auto.web.pages;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.common.WebAppExecutionContext;
import com.wearezeta.auto.web.common.WebCommonUtils;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.popovers.GroupPopoverContainer;
import com.wearezeta.auto.web.pages.popovers.PeoplePopoverContainer;
import com.wearezeta.auto.web.pages.popovers.SingleUserPopoverContainer;

public class ConversationPage extends WebPage {

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class
			.getSimpleName());

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathTextMessageEntry)
	private List<WebElement> textMessageEntries;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathImageMessageEntry)
	private List<WebElement> imageMessageEntries;

	@FindBy(how = How.ID, using = WebAppLocators.ConversationPage.idConversationInput)
	private WebElement conversationInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathShowParticipantsButton)
	private WebElement showParticipants;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathSendImageInput)
	private WebElement imagePathInput;

	@FindBy(how = How.XPATH, using = WebAppLocators.ConversationPage.xpathPingButton)
	private WebElement pingButton;

	@FindBy(how = How.CLASS_NAME, using = WebAppLocators.ConversationPage.classPingMessage)
	private WebElement pingMessage;

	public ConversationPage(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void writeNewMessage(String message) {
		conversationInput.sendKeys(message);
	}

	public void sendNewMessage() {
		conversationInput.sendKeys(Keys.ENTER);
	}

	public boolean isActionMessageSent(final String message) throws Exception {
		final By locator = By
				.xpath(WebAppLocators.ConversationPage.xpathActionMessageEntries);
		assert DriverUtils.waitUntilElementAppears(this.getDriver(), locator);
		final List<WebElement> actionMessages = this.getDriver()
				.findElements(locator).stream().filter(x -> x.isDisplayed())
				.collect(Collectors.toList());
		// Get the most recent action message only
		final String actionMessageInUI = actionMessages.get(
				actionMessages.size() - 1).getText();
		return actionMessageInUI.toUpperCase().contains(message.toUpperCase());
	}

	public boolean isMessageSent(String message) throws Exception {
		final By locator = By
				.xpath(String
						.format(WebAppLocators.ConversationPage.xpathFormatSpecificTextMessageEntry,
								message));
		return DriverUtils.isElementDisplayed(driver, locator, 5);
	}

	public PeoplePopoverContainer clickPeopleButton(boolean isGroup)
			throws Exception {
		DriverUtils.waitUntilElementClickable(driver, showParticipants);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.INTERNET_EXPLORER)) {
			driver.executeScript(String
					.format("$('.%s').click();",
							WebAppLocators.ConversationPage.classNameShowParticipantsButton));
		} else {
			showParticipants.click();
		}
		if (isGroup) {
			return new GroupPopoverContainer(this.getDriver(), this.getWait());
		} else {
			return new SingleUserPopoverContainer(this.getDriver(),
					this.getWait());
		}
	}

	public PeoplePickerPage clickShowParticipantsButton() throws Exception {
		showParticipants.click();
		return new PeoplePickerPage(this.getDriver(), this.getWait());
	}

	private static final String TMP_ROOT = "/tmp";

	/**
	 * Workaround for https://code.google.com/p/selenium/issues/detail?id=4220
	 * 
	 * @param pictureName
	 *            the path to the original picture to be uploaded into
	 *            conversation
	 * @throws Exception
	 */
	private static void sendPictureInSafari(String pictureName)
			throws Exception {
		final ClassLoader classLoader = ConversationPage.class.getClassLoader();
		final InputStream scriptStream = classLoader.getResourceAsStream(String
				.format("%s/%s",
						WebAppConstants.Scripts.RESOURCES_SCRIPTS_ROOT,
						WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT));
		final String srcScriptPath = String.format("%s/%s", TMP_ROOT,
				WebAppConstants.Scripts.SAFARI_SEND_PICTURE_SCRIPT);
		final File srcImage = new File(pictureName);
		assert srcImage.exists() : "There's no image by path "
				+ srcImage.getCanonicalPath() + " on your local file system";
		final File dstImage = new File(String.format("%s/%s", TMP_ROOT,
				srcImage.getName()));
		try {
			WebCommonUtils.formatTextInFileAndSave(scriptStream, srcScriptPath,
					new String[] { dstImage.getParent(), dstImage.getName() });
		} finally {
			if (scriptStream != null) {
				scriptStream.close();
			}
		}
		final String dstScriptPath = srcScriptPath;
		try {
			WebCommonUtils.putFileOnExecutionNode(
					WebAppExecutionContext.seleniumNodeIp,
					srcImage.getAbsolutePath(), dstImage.getAbsolutePath());
			WebCommonUtils.putFileOnExecutionNode(
					WebAppExecutionContext.seleniumNodeIp, srcScriptPath,
					dstScriptPath);
		} finally {
			new File(srcScriptPath).delete();
		}

		WebCommonUtils.executeAppleScriptFileOnNode(
				WebAppExecutionContext.seleniumNodeIp, dstScriptPath);
	}

	public void sendPicture(String pictureName) throws Exception {
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		final String showImageLabelJScript = "$('"
				+ WebAppLocators.ConversationPage.cssRightControlsPanel
				+ "').css({'opacity': '100'});";
		driver.executeScript(showImageLabelJScript);
		final String showPathInputJScript = "$('"
				+ WebAppLocators.ConversationPage.cssSendImageLabel
				+ "').find('"
				+ WebAppLocators.ConversationPage.cssSendImageInput
				+ "').css({'left': '0'});";
		driver.executeScript(showPathInputJScript);
		assert DriverUtils.isElementDisplayed(driver,
				By.xpath(WebAppLocators.ConversationPage.xpathSendImageInput),
				10);
		if (WebAppExecutionContext.browserName
				.equals(WebAppConstants.Browser.SAFARI)) {
			sendPictureInSafari(picturePath);
		} else {
			imagePathInput.sendKeys(picturePath);
		}
	}

	public boolean isPictureSent(String pictureName) throws Exception {
		@SuppressWarnings("unused")
		final String picturePath = WebCommonUtils
				.getFullPicturePath(pictureName);
		// TODO: Add comparison of the original and sent pictures
		final boolean isAnyPictureMsgFound = DriverUtils
				.waitUntilElementAppears(
						driver,
						By.xpath(WebAppLocators.ConversationPage.xpathImageMessageEntry),
						40);
		return isAnyPictureMsgFound && (imageMessageEntries.size() > 0);
	}

	public void clickPingButton() {
		try {
			DriverUtils.moveMouserOver(driver, conversationInput);
		} catch (WebDriverException e) {
			// do nothing (safari workaround)
		}
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		pingButton.click();
	}

	private static final int PING_MESSAGE_TIMEOUT = 3; // seconds

	public boolean isPingMessageVisible(String message) throws Exception {
		final By locator = By
				.className(WebAppLocators.ConversationPage.classPingMessage);
		assert DriverUtils.isElementDisplayed(driver, locator,
				PING_MESSAGE_TIMEOUT) : "Ping message has not been shown within "
				+ PING_MESSAGE_TIMEOUT + " second(s) timeout";
		return pingMessage.getText().toLowerCase()
				.contains(message.toLowerCase());
	}

	public int numberOfPingMessagesVisible() {

		return driver.findElementsByClassName(
				WebAppLocators.ConversationPage.classPingMessage).size() - 1;
	}
}
