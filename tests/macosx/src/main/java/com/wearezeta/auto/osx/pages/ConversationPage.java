package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.steps.CommonSteps;
import com.wearezeta.auto.osx.util.NSPoint;

public class ConversationPage extends OSXPage {
	private static final Logger log = ZetaLogger.getLog(ConversationPage.class.getSimpleName());
	
	static final String SOUNDCLOUD_BUTTON_ATT_TITLE = "AXTitle";
	static String SOUNDCLOUD_BUTTON_STATE;

	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement viewPager;

	private WebElement newMessageTextArea = findNewMessageTextArea();

	@FindBy(how = How.XPATH, using = OSXLocators.xpathMessageEntry)
	private List<WebElement> messageEntries;

	@FindBy(how = How.NAME, using = OSXLocators.nameSayHelloMenuItem)
	private WebElement sayHelloMenuItem;

	@FindBy(how = How.ID, using = OSXLocators.idAddImageButton)
	private WebElement addImageButton;

	@FindBy(how = How.ID, using = OSXLocators.idPeopleButton)
	private WebElement peopleButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathSoundCloudLinkButton)
	private WebElement soundCloudLinkButton;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathSoundCloudMediaContainer)
	private WebElement soundCloudMediaContainer;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarPlayPauseButton)
	private WebElement mediabarPlayPauseButton;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarCloseButton)
	private WebElement mediabarStopCloseButton;

	@FindBy(how = How.ID, using = OSXLocators.idMediaBarTitelButton)
	private WebElement mediabarBarTitle;

	@FindBy(how = How.XPATH, using = OSXLocators.xpathConversationViewScrollArea)
	private WebElement conversationView;

	public ConversationPage(String URL, String path)
			throws MalformedURLException {

		super(URL, path);
	}

	public Boolean isVisible() {

		return viewPager != null;
	}

	private WebElement findNewMessageTextArea() {
		List<WebElement> rows = driver.findElements(By
				.xpath(OSXLocators.xpathNewMessageTextArea));
		for (WebElement row : rows) {
			if (row.getText().equals("")) {
				return row;
			}
		}
		return null;
	}

	public void knock() {
		sayHelloMenuItem.click();
	}

	public boolean isMessageExist(String message) throws InterruptedException {
		if (message.contains(OSXLocators.YOU_ADDED_MESSAGE)) {
			for (int i = 0; i < 10; i++) {
				List<WebElement> els = driver.findElements(By
						.xpath(OSXLocators.xpathMessageEntry));
				Collections.reverse(els);
				for (WebElement el : els) {
					if (el.getText().contains(OSXLocators.YOU_ADDED_MESSAGE)) {
						return true;
					}
				}
				Thread.sleep(1000);
			}
		} else if (message.contains(OSXLocators.USER_ADDED_MESSAGE_FORMAT)) {
			for (int i = 0; i < 10; i++) {
				List<WebElement> els = driver.findElements(By
						.xpath(OSXLocators.xpathMessageEntry));
				Collections.reverse(els);
				for (WebElement el : els) {
					if (el.getText().contains(OSXLocators.USER_ADDED_MESSAGE_FORMAT)) {
						return true;
					}
				}
				Thread.sleep(1000);
			}
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatSpecificMessageEntry, message);
			WebElement el = driver.findElement(By.xpath(xpath));
			return (el != null);
		}
		return false;
	}

	public void writeNewMessage(String message) {
		int i = 0;
		while (newMessageTextArea == null) {
			newMessageTextArea = findNewMessageTextArea();
			if (++i > 10) {
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		newMessageTextArea.sendKeys(message + "\\n");
	}

	public void sendNewMessage() {
		newMessageTextArea.submit();
	}

	public void openConversationPeoplePicker() {
		peopleButton.click();
	}

	public void openChooseImageDialog() {
		if (addImageButton == null) {
			Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(10, TimeUnit.SECONDS)
					.pollingEvery(2, TimeUnit.SECONDS)
					.ignoring(NoSuchElementException.class);

			addImageButton = wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					return driver.findElement(By
							.name(OSXLocators.nameSignInButton));
				}
			});
		}
		addImageButton.click();
	}

	public int getNumberOfMessageEntries(String message) {
		String xpath = String.format(
				OSXLocators.xpathFormatSpecificMessageEntry, message);
		List<WebElement> messageEntries = driver.findElements(By.xpath(xpath));
		return messageEntries.size();
	}

	public int getNumberOfImageEntries() {
		List<WebElement> conversationImages = driver.findElements(By
				.xpath(OSXLocators.xpathConversationImageEntry));
		return conversationImages.size();
	}

	public boolean isMessageSent(String message) {
		boolean isSend = false;
		String xpath = String.format(
				OSXLocators.xpathFormatSpecificMessageEntry, message);
		DriverUtils.waitUntilElementAppears(driver, By.xpath(xpath));
		WebElement element = driver.findElement(By.xpath(xpath));
		if (element != null) {
			isSend = true;
		}
		return isSend;
	}

	public void tapOnSoundCloudMessage() {
		soundCloudLinkButton.click();
	}

	public boolean isSoundCloudContainerVisible() {

		return DriverUtils.waitUntilElementAppears(driver,
				By.xpath(OSXLocators.xpathSoundCloudMediaContainer));
	}

	public String getSoundCloudButtonState() {
		SOUNDCLOUD_BUTTON_STATE = soundCloudLinkButton
				.getAttribute(SOUNDCLOUD_BUTTON_ATT_TITLE);
		return SOUNDCLOUD_BUTTON_STATE;
	}

	public void scrollDownTilMediaBarAppears() throws Exception {

		NSPoint soundcloudPosition = NSPoint.fromString(soundCloudLinkButton
				.getAttribute("AXPosition"));
		NSPoint textInputPosition = NSPoint.fromString(newMessageTextArea
				.getAttribute("AXPosition"));

		// get scrollbar for conversation view
		WebElement conversationDecrementSB = null;

		WebElement scrollArea = driver.findElement(By
				.id(OSXLocators.idConversationScrollArea));

		if (soundcloudPosition.y() < textInputPosition.y()) {
			WebElement scrollBar = scrollArea.findElement(By
					.xpath("//AXScrollBar"));
			List<WebElement> scrollButtons = scrollBar.findElements(By
					.xpath("//AXButton"));
			for (WebElement scrollButton : scrollButtons) {
				String subrole = scrollButton.getAttribute("AXSubrole");
				if (subrole.equals("AXDecrementPage")) {
					conversationDecrementSB = scrollButton;
				}
			}
			while (soundcloudPosition.y() < textInputPosition.y()) {
				conversationDecrementSB.click();
				soundcloudPosition = NSPoint.fromString(soundCloudLinkButton
						.getAttribute("AXPosition"));
			}
		}
	}

	public void pressPlayPauseButton() {
		mediabarPlayPauseButton.click();
	}

	public void pressStopButton() {
		mediabarStopCloseButton.click();
	}

	public void pressMediaTitle() {
		mediabarBarTitle.click();
	}
	
	public String getLastConversationNameChangeMessage() {
		WebElement el = driver.findElement(By.xpath(OSXLocators.xpathConversationLastNewNameEntry));
		return el.getAttribute("AXValue");
	}
	
	public boolean isMediaBarVisible(){
		return mediabarBarTitle.isDisplayed();
	}
	
	public void waitForSoundcloudButtonState(String currentState, String wantedState) throws InterruptedException{
		Thread.sleep(1000);
		while(!currentState.equals(wantedState)){
	    	Assert.assertEquals(currentState, currentState);
	    	Thread.sleep(1000);
	    	currentState = CommonSteps.senderPages.getConversationPage().getSoundCloudButtonState();
	    }	
	}
}
