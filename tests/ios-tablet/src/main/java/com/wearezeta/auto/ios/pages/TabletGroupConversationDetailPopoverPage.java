package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;
import com.wearezeta.auto.ios.locators.IOSLocators;
import com.wearezeta.auto.ios.tablet.locators.IOSTabletLocators;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {

	public TabletGroupConversationDetailPopoverPage(

	Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}
	
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;

	private final String AQA_PICTURE_CONTACT = "AQAPICTURECONTACT";
	private final String AQA_AVATAR_CONTACT = "QAAVATAR";

	@FindBy(how = How.NAME, using = IOSLocators.nameConversationMenu)
	private WebElement conversationMenuButton;
	
	@FindBy(how = How.NAME, using = IOSTabletLocators.TabletGroupConversationDetailPopoverPage.nameRenameButtonEllipsisMenue)
	private WebElement renameEllipsesButton;
	
	@FindBy(how = How.XPATH, using = IOSTabletLocators.TabletGroupConversationDetailPopoverPage.xpathPopoverAvatarCollectionView)
	private WebElement avatarPopoverCollectionView;

	public void openConversationMenuOnPopover() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(IOSLocators.nameConversationMenu));
		conversationMenuButton.click();
	}
	
	public TabletOtherUserInfoPage selectUserByNameOniPadPopover(String name) throws Exception{
		DriverUtils.mobileTapByCoordinates(this.getDriver(), getDriver()
				.findElementByName(name.toUpperCase()));

		return new TabletOtherUserInfoPage(this.getLazyDriver());
	}
	
	public boolean waitForContactToDisappearOniPadPopover(String name) throws Exception{
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
				By.name(name));
	}
	
	public void pressRenameEllipsesButton(){
		renameEllipsesButton.click();
	}
	
	public void exitGroupChatPopover() throws Exception{
		DriverUtils.mobileTapByCoordinates(getDriver(), conversationMenuButton, 50, 50);
	}
	
	public int numberOfPeopleInGroupConversationOniPad() throws Exception{
		int result = -1;
		List<WebElement> elements = getDriver().findElements(By.xpath(IOSTabletLocators.TabletGroupConversationDetailPopoverPage.xpathGroupConvTotalNumber));
		for (WebElement element : elements) {
			String value = element.getText();
			if (value.contains(IOSTabletLocators.TabletGroupConversationDetailPopoverPage.namePeopleCountWord)) {
				result = Integer.parseInt(value.substring(0,
						value.indexOf((IOSTabletLocators.TabletGroupConversationDetailPopoverPage.namePeopleCountWord))));
			}
		}
		return result;
	}
	
	public boolean areParticipantAvatarCorrectOniPadPopover(String contact) throws IllegalStateException, Throwable{
		String name = "", picture = "";
		if (contact.toLowerCase().contains(AQA_PICTURE_CONTACT.toLowerCase())) {
			name = AQA_PICTURE_CONTACT;
			picture = "pictureAvatariPad.png";
		} else {
			name = AQA_AVATAR_CONTACT;
			picture = "initialAvatariPad.png";
		}
		List<WebElement> participantAvatars = getCurrentParticipantsOnPopover();
		BufferedImage avatarIcon = null;
		boolean flag = false;
		for (WebElement avatar : participantAvatars) {
			avatarIcon = CommonUtils.getElementScreenshot(avatar,
					this.getDriver()).orElseThrow(IllegalStateException::new);

			List<WebElement> avatarText = avatar.findElements(By
					.className("UIAStaticText"));

			for (WebElement text : avatarText) {
				String avatarName = text.getAttribute("name");
				if (avatarName.equalsIgnoreCase(name)) {
					BufferedImage realImage = ImageUtil
							.readImageFromFile(IOSPage.getImagesPath()
									+ picture);

					double score = ImageUtil.getOverlapScore(realImage,
							avatarIcon, ImageUtil.RESIZE_NORESIZE);
					if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
						return false;
					} else {
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	
	public List<WebElement> getCurrentParticipantsOnPopover() {
		return avatarPopoverCollectionView.findElements(By
				.className("UIACollectionCell"));
	}



}
