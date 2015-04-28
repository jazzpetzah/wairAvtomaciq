package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

class ParticipantsListPopoverPage extends AbstractPopoverPage {

	private static final String TOOLTIP_ADD_PEOPLE_TO_CONVERSATION = "Add people to conversation";
	private static final String TOOLTIP_CHANGE_CONVERSATION_NAME = "Change conversation name";
	private static final String TOOLTIP_LEAVE_GROUP_CHAT = "Leave conversation";

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitle)
	private WebElement conversationTitle;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathConversationTitleInput)
	private WebElement conversationTitleInput;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantsListPage.xpathLeaveGroupChat)
	private WebElement leaveButton;

	public ParticipantsListPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	public void setConversationTitle(String newTitle) {
		conversationTitle.click();
		conversationTitleInput.clear();
		conversationTitleInput.sendKeys(newTitle + "\n");
	}

	public void clickLeaveGroupChat() {
		leaveButton.click();
	}

	public void clickOnParticipant(String name)
			throws Exception {
		final By locator = By
				.xpath(PopoverLocators.GroupPopover.ParticipantsListPage.xpathParticipantByName
						.apply(name));
		assert DriverUtils.isElementDisplayed(driver, locator, 3);
		WebElement participant = driver.findElement(locator);
		assert DriverUtils.waitUntilElementClickable(driver, participant);
		participant.click();
	}

	@Override
	protected String getXpathLocator() {
		return PopoverLocators.GroupPopover.ParticipantsListPage.xpathLeaveGroupChat;
	}

	@Override
	protected WebElement getSharedElement(String relativeXpath) {
		return super
				.getSharedElement(String
						.format("%s%s",
								PopoverLocators.GroupPopover.ParticipantsListPage.xpathPageRootLocator,
								relativeXpath));
	}

	private WebElement getAddPeopleElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathAddButton);
	}

	public void clickAddPeopleButton() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				getAddPeopleElement());
		getAddPeopleElement().click();
	}

	public boolean isAddPeopleButtonToolTipCorrect() throws Exception {
		return TOOLTIP_ADD_PEOPLE_TO_CONVERSATION.equals(getAddPeopleElement()
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}

	public boolean isParticipantVisible(String name) throws Exception {
		final By locator = By
				.xpath(PopoverLocators.GroupPopover.ParticipantsListPage.xpathParticipantByName
						.apply(name));
		return DriverUtils.isElementDisplayed(driver, locator, 3);
	}

	public String getConversationTitle() {
		return conversationTitle.getText();
	}

	public boolean isRenameConversationToolTipCorrect() {
		return TOOLTIP_CHANGE_CONVERSATION_NAME.equals(conversationTitle
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}

	public boolean isLeaveGroupChatButtonToolTipCorrect() {
		return TOOLTIP_LEAVE_GROUP_CHAT.equals(leaveButton
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}
}
