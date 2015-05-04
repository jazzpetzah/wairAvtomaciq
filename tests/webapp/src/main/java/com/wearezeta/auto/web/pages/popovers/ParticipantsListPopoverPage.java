package com.wearezeta.auto.web.pages.popovers;

import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

class ParticipantsListPopoverPage extends AbstractPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitle)
	private WebElement conversationTitle;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitleInput)
	private WebElement conversationTitleInput;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathLeaveGroupChat)
	private WebElement leaveButton;

	public ParticipantsListPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	public void setConversationTitle(String newTitle) {
		conversationTitle.click();
		conversationTitleInput.clear();
		conversationTitleInput.sendKeys(newTitle + "\n");
	}

	public void clickLeaveGroupChat() {
		leaveButton.click();
	}

	public void clickOnParticipant(String name) throws Exception {
		final By locator = By
				.xpath(PopoverLocators.GroupPopover.ParticipantsListPage.xpathParticipantByName
						.apply(name));
		assert DriverUtils.isElementDisplayed(this.getDriver(), locator, 3);
		WebElement participant = getDriver().findElement(locator);
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				participant);
		participant.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ParticipantsListPage.xpathLeaveGroupChat;
	}

	@Override
	protected WebElement getSharedElement(String relativeXpath) throws Exception {
		return super
				.getSharedElement(String
						.format("%s%s",
								PopoverLocators.GroupPopover.ParticipantsListPage.xpathPageRootLocator,
								relativeXpath));
	}

	private WebElement getAddPeopleElement() throws Exception {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddPeopleElement());
		getAddPeopleElement().click();
	}

	public String getAddPeopleButtonToolTip() throws Exception {
		return getAddPeopleElement().getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public boolean isParticipantVisible(String name) throws Exception {
		final By locator = By
				.xpath(PopoverLocators.GroupPopover.ParticipantsListPage.xpathParticipantByName
						.apply(name));
		return DriverUtils.isElementDisplayed(this.getDriver(), locator, 3);
	}

	public String getConversationTitle() {
		return conversationTitle.getText();
	}

	public String getRenameConversationToolTip() {
		return conversationTitle.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public String getLeaveGroupChatButtonToolTip() {
		return leaveButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}
}
