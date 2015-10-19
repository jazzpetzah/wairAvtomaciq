package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletConversationViewPage extends AndroidTabletPage {
	public static final String idRootLocator = "pfac__conversation__list_view_container";

	public static final Function<String, String> xpathSystemMessageByContent = content -> String
			.format("//*[@id='ltv__row_conversation__message' and contains(@value, '%s')]",
					content);

	public static final Function<String, String> xpathChatHeaderMessageByContent = content -> String
			.format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
					content);

	private static final Function<String, String> xpathOutgoingInvitationMessageByContent = content -> String
			.format("//*[@id='ttv__connect_request__first_message' and @value='%s']",
					content);

	private static final Function<String, String> xpathSystemConnectionMessageByContent = content -> String
			.format("//*[@id='ttv__row_conversation__connect_request__chathead_footer__label' and contains(@value, '%s')]",
					content);

	private static final Function<String, String> xpathSystemConvoNameMessageByContent = content -> String
			.format("//*[@id='ttv__row_conversation__new_conversation_name' and @value='%s']",
					content);

	@FindBy(id = DialogPage.idParticipantsBtn)
	private WebElement showDetailsButton;

	private static final String idMissedCallImage = "sci__conversation__missed_call__image";

	private static final String idShowToolsButton = "cursor_button_open";
	@FindBy(id = idShowToolsButton)
	private WebElement showToolsButton;

	private static final String idCloseToolsButton = "cursor_button_close";
	@FindBy(id = idCloseToolsButton)
	private WebElement closeToolsButton;

	private static final String idCaret = "caret";
	@FindBy(id = idCaret)
	private WebElement caret;

	@FindBy(id = idEditText)
	private WebElement inputField;

	public static final Function<String, String> xpathInputFieldByValue = value -> String
			.format("//*[@id='%s' and @value='%s']", idEditText, value);

	public static final Function<String, String> xpathConversationMessageByValue = value -> String
			.format("//*[@id='ltv__row_conversation__message' and @value='%s']",
					value);

	public TabletConversationViewPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private DialogPage getDialogPage() throws Exception {
		return this.getAndroidPageInstance(DialogPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idRootLocator));
	}

	public void tapShowDetailsButton() throws Exception {
		showDetailsButton.click();
	}

	public boolean waitForSystemMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathSystemMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitForChatHeaderMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathChatHeaderMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapTextInput() throws Exception {
		// FIXME: Scroll to the bottom if cursor input is not visible
		this.scrollToTheBottom();

		caret.click();
	}

	public void typeMessage(String message) {
		inputField.sendKeys(message);
	}

	public void sendMessage() throws Exception {
		getDriver().tapSendButton();
	}

	public boolean waitUntilMessageIsVisible(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathConversationMessageByValue
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void swipeLeftOnTextInput() throws Exception {
		getDialogPage().swipeOnCursorInput();
	}

	public void tapPingButton() throws Exception {
		getDialogPage().tapPingBtn();
	}

	public boolean waitUntilPingMessageIsVisible(String expectedMessage)
			throws Exception {
		return getDialogPage().waitForPingMessageWithText(expectedMessage);
	}

	public boolean waitUntilAPictureAppears() throws Exception {
		final By locator = By.id(DialogPage.idDialogImages);
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapShowInstrumentsButton() throws Exception {
		// FIXME: Workaround for incorrectly positioned cursor
		scrollToTheBottom();

		showToolsButton.click();
	}

	public void tapCloseInstrumentsButton() {
		closeToolsButton.click();
	}

	public boolean waitUntilGCNIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idMissedCallImage));
	}

	public boolean waitUntilMessageIsNotVisible(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathConversationMessageByValue
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean waitForOutgoingInvitationMessage(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathOutgoingInvitationMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilPingMessageIsInvisible(String expectedMessage)
			throws Exception {
		return getDialogPage().waitForPingMessageWithTextDisappears(
				expectedMessage);
	}

	public void doSwipeRight() throws Exception {
		DriverUtils.swipeByCoordinates(getDriver(), 1000, 10, 50, 90, 50);
	}

	public void scrollToTheBottom() throws Exception {
		getDialogPage().tapDialogPageBottom();
	}

	public Optional<BufferedImage> getRecentPictureScreenshot()
			throws Exception {
		return getDialogPage().getRecentPictureScreenshot();
	}

	public Optional<BufferedImage> getPreviewPictureScreenshot()
			throws Exception {
		return getDialogPage().getPreviewPictureScreenshot();
	}

	public void tapRecentPicture() throws Exception {
		getDialogPage().clickLastImageFromDialog();
	}

	public boolean waitForSystemConnectionMessageContains(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathSystemConnectionMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitForConversationNameSystemMessage(String expectedMessage)
			throws Exception {
		final By locator = By.xpath(xpathSystemConvoNameMessageByContent
				.apply(expectedMessage));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idRootLocator));
	}

	public boolean waitUntilPicturesNotVisible() throws Exception {
		final By locator = By.id(DialogPage.idDialogImages);
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public boolean waitUntilUnsentIndicatorIsVisible(String msg)
			throws Exception {
		return getDialogPage().waitForUnsentIndicator(msg);
	}

	public boolean waitUntilUnsentIndicatorIsVisibleForAPicture()
			throws Exception {
		return getDialogPage().waitForAPictureWithUnsentIndicator();
	}

	public void tapPlayPauseButton() throws Exception {
		getDialogPage().tapPlayPauseBtn();
	}

	public boolean waitUntilClosePicturePreviewButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(idCloseImageBtn));
	}

	public boolean waitUntilClosePicturePreviewButtonInvisible()
			throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(idCloseImageBtn));
	}

	public void tapClosePicturePreviewButton() throws Exception {
		getDriver().findElement(By.id(idCloseImageBtn)).click();
	}

	public boolean waitUntilGiphyButtonVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(giphyPreviewButtonId));
	}

	public boolean waitUntilGiphyButtonInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(giphyPreviewButtonId));
	}

	public void tapGiphyButton() throws Exception {
		this.getDriver().findElement(By.id(giphyPreviewButtonId)).click();
	}

	public void tapSketchButton() throws Exception {
		getDialogPage().tapSketchBtn();
	}

	public Optional<BufferedImage> getImageScreenshotInFullScreen()
			throws Exception {
		return getDialogPage().getLastImageInFullScreen();
	}

	public void tapSketchButtonOnPicturePreview() throws Exception {
		getDialogPage().tapSketchOnImageButton();
	}
}
