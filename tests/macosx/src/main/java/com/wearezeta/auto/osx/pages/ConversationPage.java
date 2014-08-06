package com.wearezeta.auto.osx.pages;

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import com.wearezeta.auto.osx.locators.OSXLocators;
import com.wearezeta.auto.osx.util.NSPoint;

public class ConversationPage extends OSXPage {

	@FindBy(how = How.ID, using = OSXLocators.idMainWindow)
	private WebElement viewPager;

	// @FindBy(how = How.ID, using = OSXLocators.xpathNewMessageTextArea)
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

	public boolean isMessageExist(String message) {
		boolean isExist = false;
		if (message.contains(OSXLocators.YOU_ADDED_MESSAGE)) {
			for (int i = 0; i < 10; i++) {
				List<WebElement> els = driver.findElements(By
						.xpath(OSXLocators.xpathMessageEntry));
				Collections.reverse(els);
				for (WebElement el : els) {
					if (el.getText().contains(OSXLocators.YOU_ADDED_MESSAGE)) {
						isExist = true;
					}
				}
				if (isExist)
					break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
		} else {
			String xpath = String.format(
					OSXLocators.xpathFormatSpecificMessageEntry, message);
			WebElement el = driver.findElement(By.xpath(xpath));
			if (el == null)
				isExist = false;
			else
				isExist = true;
		}
		return isExist;
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
		newMessageTextArea.sendKeys(message);
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
		String soundCloudButtonState = soundCloudLinkButton
				.getAttribute("AXTitle");
		return soundCloudButtonState;
	}

	public void scrollDownTilMediaBarAppears() throws Exception {

		boolean mediaBarButtonIsShown = mediabarPlayPauseButton.isDisplayed();

		NSPoint soundcloudPosition = NSPoint.fromString(soundCloudLinkButton
				.getAttribute("AXPosition"));
		NSPoint textInputPosition = NSPoint.fromString(newMessageTextArea
				.getAttribute("AXPosition"));

		// get scrollbar for conversation view
		WebElement conversationDecrementSB = null;
		WebElement conversationIncrementSB = null;

		WebElement scrollArea = driver.findElement(By.id(OSXLocators.idConversationScrollArea));

		if (soundcloudPosition.y() < textInputPosition.y()) {
			WebElement scrollBar = scrollArea.findElement(By.xpath("//AXScrollBar"));
			List<WebElement> scrollButtons = scrollBar.findElements(By.xpath("//AXButton"));
			for (WebElement scrollButton : scrollButtons) {
				String subrole = scrollButton.getAttribute("AXSubrole");
				if (subrole.equals("AXDecrementPage")) {
					conversationDecrementSB = scrollButton;
				}
				if (subrole.equals("AXIncrementPage")) {
					conversationIncrementSB = scrollButton;
				}
			}
			while (soundcloudPosition.y() < textInputPosition.y()) {
				conversationDecrementSB.click();
				soundcloudPosition = NSPoint.fromString(soundCloudLinkButton.getAttribute("AXPosition"));
			}

		}

		// Assert.assertTrue(mediabarPlayPauseButton.isDisplayed());
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

}
