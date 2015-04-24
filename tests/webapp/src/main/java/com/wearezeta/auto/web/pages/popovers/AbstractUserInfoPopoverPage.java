package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class AbstractUserInfoPopoverPage extends AbstractPopoverPage {

	private static final String TOOLTIP_REMOVE_FROM_CONVERSATION = "Remove from conversation";

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathRemoveButton)
	private WebElement removeButton;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathEmailLabel)
	private WebElement mail;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathAvatar)
	private WebElement avatar;

	public AbstractUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	public void clickRemoveFromGroupChat() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, removeButton);
		removeButton.click();
	}

	public boolean isRemoveFromGroupChatButtonToolTipCorrect() {
		return TOOLTIP_REMOVE_FROM_CONVERSATION.equals(removeButton
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}

	public boolean isRemoveButtonVisible() {
		return removeButton.isDisplayed();
	}

	public boolean isAvatarVisible() {
		return avatar.isDisplayed();
	}

	public String getMailText() {
		return mail.getText();
	}
}
