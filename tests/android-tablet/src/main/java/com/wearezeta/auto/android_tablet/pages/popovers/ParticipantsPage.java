package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

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
			.format("//*[@id='%s']//*[@value='%s']/parent::*/parent::*",
					GroupPopover.idRootLocator, name.toUpperCase());

	private static final String idOpenConversationButton = "gtv__participants__left__action";
	@FindBy(id = idOpenConversationButton)
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
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator);
		this.getDriver().findElement(locator).click();
	}

	public boolean waitForParticipantAvatarNotVisible(String name)
			throws Exception {
		final By locator = By.xpath(xpathParticipantAvatarByName.apply(name));
		return DriverUtils.waitUntilLocatorDissapears(getDriver(), locator);
	}

	public void tapOpenConversationButton() {
		openConversationButton.click();
	}

}
