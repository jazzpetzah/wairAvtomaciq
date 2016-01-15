package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.OtherUserPersonalInfoPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class ParticipantsPage extends AbstractConversationDetailsPage {
	public static final String xpathConfirmLeaveButton = "//*[@id='confirm' and @value='LEAVE']";
	@FindBy(xpath = xpathConfirmLeaveButton)
	private WebElement confirmLeaveButton;

	public static final Function<String, String> xpathParticipantAvatarByName = name -> String
			.format("//*[@id='pfac__participants']//*[@id='ttv__group__adapter' and @value='%s']/parent::*/parent::*",
					name.split("\\s+")[0]);

	private static final String idConvoNameInput = "taet__participants__header__editable";
	@FindBy(id = idConvoNameInput)
	private WebElement convoNameInput;

	private static final Function<String, String> xpathConvoNameInputByValue = value -> String
			.format("//*[@id='%s' and @value='%s']", idConvoNameInput, value);

	private static final Function<String, String> xpathSubheaderByValue = value -> String
			.format("//*[@id='ttv__participants__sub_header' and @value='%s']",
					value);

	private static final String xpathOpenConversationButton = OtherUserPersonalInfoPage.xpathLeftActionButton;
	@FindBy(xpath = xpathOpenConversationButton)
	private WebElement openConversationButton;

	public ParticipantsPage(Future<ZetaAndroidDriver> lazyDriver,
			GroupPopover container) throws Exception {
		super(lazyDriver, container);
	}

	public void tapConfirmLeaveButton() {
		confirmLeaveButton.click();
	}

	public boolean waitForParticipantAvatarVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public void tapParticipantAvatar(String name) throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		verifyLocatorPresence(locator, String.format("The avatar of '%s' is not visible", name));
		this.getDriver().findElement(locator).click();
	}

	public boolean waitForParticipantAvatarNotVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void renameConversation(String newName) throws Exception {
		convoNameInput.click();
		convoNameInput.clear();
		convoNameInput.sendKeys(newName);
		getDriver().tapSendButton();
	}

	public void tapOpenConversationButton() {
		openConversationButton.click();
	}

	public boolean waitUntilConversationNameVisible(String expectedName)
			throws Exception {
		final By locator = By.xpath(xpathConvoNameInputByValue
				.apply(expectedName));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

	public boolean waitUntilSubheaderIsVisible(String expectedText)
			throws Exception {
		final By locator = By.xpath(xpathSubheaderByValue.apply(expectedText));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
	}

}
