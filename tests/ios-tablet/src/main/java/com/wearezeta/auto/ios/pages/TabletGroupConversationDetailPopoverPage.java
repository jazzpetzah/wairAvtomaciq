package com.wearezeta.auto.ios.pages;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaIOSDriver;

public class TabletGroupConversationDetailPopoverPage extends GroupChatInfoPage {

	public TabletGroupConversationDetailPopoverPage(

	Future<ZetaIOSDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		// TODO Auto-generated constructor stub
	}

	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;

	private final String AQA_PICTURE_CONTACT = "AQAPICTURECONTACT";
	private final String AQA_AVATAR_CONTACT = "QAAVATAR";

	public static final String nameConversationMenu = "metaControllerRightButton";
	@FindBy(name = nameConversationMenu)
	private WebElement conversationMenuButton;

    public static final String nameRenameButtonEllipsisMenue = "RENAME";
    @FindBy(name = nameRenameButtonEllipsisMenue)
	private WebElement renameEllipsesButton;

    public static final String xpathPopoverAvatarCollectionView =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIACollectionView[1]";
    @FindBy(xpath = xpathPopoverAvatarCollectionView)
	private WebElement avatarPopoverCollectionView;

    public static final String xpathSilenceButtonEllipsisMenu =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='SILENCE']";
    @FindBy(xpath = xpathSilenceButtonEllipsisMenu)
	private WebElement silenceEllipsisButton;

    public static final String xpathNotifyButtonEllipsisMenu =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAButton[@name='NOTIFY']";
    @FindBy(xpath = xpathNotifyButtonEllipsisMenu)
	private WebElement notifyEllipsisButton;

    public static final String xpathGroupConvTotalNumber =
            "//UIAApplication[1]/UIAWindow[@name='ZClientMainWindow']/UIAPopover[1]/UIAStaticText[contains(@name,'PEOPLE')]";

    public static final String xpathPopover = "//UIAPopover[@visible='true']";

    public static final String namePeopleCountWord = " PEOPLE";

    public void openConversationMenuOnPopover() throws Exception {
		DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.name(nameConversationMenu));
		conversationMenuButton.click();
	}

	public boolean waitConversationInfoPopoverToClose() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(this.getDriver(), By.xpath(xpathPopover), 10);
	}
	
	public void dismissPopover() throws Exception {
		for (int i=0; i<3; i++) {
			tapOnTopLeftScreen();
			if (waitConversationInfoPopoverToClose()) {
				break;
			}
		}
	}

	public TabletOtherUserInfoPage selectUserByNameOniPadPopover(String name)
			throws Exception {
		DriverUtils.tapByCoordinates(this.getDriver(), getDriver()
				.findElementByName(name.toUpperCase()));

		return new TabletOtherUserInfoPage(this.getLazyDriver());
	}

	public void pressRenameEllipsesButton() {
		renameEllipsesButton.click();
	}

	public void exitGroupChatPopover() throws Exception {
		DriverUtils.tapByCoordinates(getDriver(), conversationMenuButton,
				50, 50);
	}

	public int numberOfPeopleInGroupConversationOniPad() throws Exception {
		int result = -1;
		List<WebElement> elements = getDriver().findElements(By.xpath(xpathGroupConvTotalNumber));
		for (WebElement element : elements) {
			String value = element.getText();
			if (value.contains(namePeopleCountWord)) {
				result = Integer.parseInt(value.substring(0, value.indexOf((namePeopleCountWord))));
			}
		}
		return result;
	}

	public boolean areParticipantAvatarCorrectOniPadPopover(String contact)
			throws IllegalStateException, Throwable {
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

	public void pressSilenceEllipsisButton() {
		silenceEllipsisButton.click();
	}

	public void pressNotifyEllipsisButton() {
		notifyEllipsisButton.click();
	}

}
