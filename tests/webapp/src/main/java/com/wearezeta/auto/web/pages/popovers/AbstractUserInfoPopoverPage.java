package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.HREF_ATTRIBUTE_LOCATOR;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

abstract class AbstractUserInfoPopoverPage extends AbstractPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathRemoveButton)
	private WebElement removeButton;

	// Mail must be here to check if it's NOT present
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathEmailLabel)
	private WebElement mail;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathAvatar)
	private WebElement avatar;

	public AbstractUserInfoPopoverPage(ZetaWebAppDriver driver,
			WebDriverWait wait, PeoplePopoverContainer container)
			throws Exception {
		super(driver, wait, container);
	}

	@Override
	protected WebElement getSharedElement(String relativeXpath) {
		return super
				.getSharedElement(String
						.format("%s%s",
								PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathPageRootLocator,
								relativeXpath));
	}

	private WebElement getUserNameElement() {
		return this.getSharedElement(PopoverLocators.Shared.xpathUserName);
	}

	public String getUserName() {
		return getUserNameElement().getText();
	}

	public void clickRemoveFromGroupChat() throws Exception {
		DriverUtils.waitUntilElementClickable(driver, removeButton);
		removeButton.click();
	}

	public String getRemoveFromGroupChatButtonToolTip() {
		return removeButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
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

	public String getMailHref() {
		return mail.getAttribute(HREF_ATTRIBUTE_LOCATOR);
	}
}
