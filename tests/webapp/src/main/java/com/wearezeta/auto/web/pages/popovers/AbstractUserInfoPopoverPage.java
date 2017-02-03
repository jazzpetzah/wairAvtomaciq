package com.wearezeta.auto.web.pages.popovers;

import java.awt.image.BufferedImage;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

import static com.wearezeta.auto.web.locators.WebAppLocators.Common.HREF_ATTRIBUTE_LOCATOR;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

abstract class AbstractUserInfoPopoverPage extends AbstractPopoverPage {

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathRemoveButton)
	private WebElement removeButton;

	// Mail must be here to check if it's NOT present
	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathEmailLabel)
	private WebElement mail;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathAvatar)
	private WebElement avatar;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathUserName)
	private WebElement username;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathUniqueUserName)
	private WebElement uniqueUsername;

	@FindBy(how = How.XPATH, using = PopoverLocators.GroupPopover.ParticipantInfoPage.xpathCommonFriends)
		private WebElement commonFriends;

	public AbstractUserInfoPopoverPage(Future<ZetaWebAppDriver> lazyDriver,
			PeoplePopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	@Override
	protected WebElement getSharedElement(String relativeXpath) throws Exception {
		return super
				.getSharedElement(String
						.format("%s%s",
								PopoverLocators.SingleUserPopover.SingleUserInfoPage.xpathPageRootLocator,
								relativeXpath));
	}

	private WebElement getUserNameElement() throws Exception {
		return this.getSharedElement(PopoverLocators.Shared.xpathUserName);
	}

	public String getUserName() throws Exception {
		return getUserNameElement().getText();
	}

	public void clickRemoveFromGroupChat() throws Exception {
		DriverUtils.waitUntilElementClickable(this.getDriver(), removeButton);
		removeButton.click();
	}

	public String getRemoveFromGroupChatButtonToolTip() {
		return removeButton.getAttribute(TITLE_ATTRIBUTE_LOCATOR);
	}

	public boolean isRemoveButtonVisible() {
		return removeButton.isDisplayed();
	}

	public boolean isRemoveButtonInvisible() throws Exception{
		final By locator = By.xpath(PopoverLocators.GroupPopover.ParticipantInfoPage.xpathRemoveButton);
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
	}

	public boolean isAvatarVisible() {
		return avatar.isDisplayed();
	}

	public BufferedImage getAvatar() throws Exception {
		return this.getElementScreenshot(avatar).orElseThrow(IllegalStateException::new);
	}
       
    public boolean isMailInvisible() throws Exception {
        final By locator = By.xpath(PopoverLocators.GroupPopover.ParticipantInfoPage.xpathEmailLabel);
        return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), locator);
    }
    
    public boolean isMailVisible() throws Exception {
        final By locator = By.xpath(PopoverLocators.GroupPopover.ParticipantInfoPage.xpathEmailLabel);
        return DriverUtils.waitUntilLocatorAppears(this.getDriver(), locator);
    }

	public String getMailText() {
		return mail.getText();
	}

	public String getMailHref() {
		return mail.getAttribute(HREF_ATTRIBUTE_LOCATOR);
	}

	public String getUniqueUsername() {
		return uniqueUsername.getText();
	}

	public String getCommonFriends() {
		return commonFriends.getText();
	}
}
