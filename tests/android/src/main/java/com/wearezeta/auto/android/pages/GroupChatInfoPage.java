package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends AndroidPage {

	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	private static final String AVATAR_WITH_IMAGE = "avatarPictureTestAndroid.png";
	private static final String AVATAR_NO_IMAGE = "avatarTestAndroid.png";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.95;
	
	@FindBy(how = How.ID, using = AndroidLocators.idLeaveConversationButton)
	private WebElement leaveConversationButton;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private WebElement confirmButton;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLinearLayout)
	private List<WebElement> linearLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameGridView)
	private WebElement groupChatUsersGrid;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private WebElement groupChatName;
	
	@FindBy(how = How.ID, using = AndroidLocators.idParticipantsSubHeader)
	private WebElement participantsSubHeader;
	
	private String url;
	private String path;
	
	public GroupChatInfoPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			break;
		}
		case LEFT:
		{
			break;
		}
		case RIGHT:
		{
			page = new GroupChatPage(url,path);
			break;
		}
		}	
		return page;
	}

	public void pressLeaveConversationButton() {
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(leaveConversationButton));
		leaveConversationButton.click();

	}

	public void pressConfirmButton() {
		refreshUITree();//TODO workaround
		confirmButton.click();
	}

	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	
	public OtherUserPersonalInfoPage selectContactByName(String contactName) throws Exception, InterruptedException {
		boolean flag = false;
		refreshUITree();
		
		for(WebElement user : linearLayout)
		{
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.classNameTextView));
			for(WebElement element : elements){
					if(element.getText() != null && element.getText().equals((contactName.toUpperCase()))){
						user.click();
						flag = true;
						break;
					}
			}
			if(flag){
				break;
			}
		}

		return new OtherUserPersonalInfoPage(url, path);
	}

	public GroupChatPage tabBackButton() throws Exception {
		driver.navigate().back();
		return new GroupChatPage(url, path);
	}

	public boolean isParticipantAvatars() throws IOException {
		BufferedImage avatarIcon = null;
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		for(int i = 1; i < linearLayout.size()+1;i++){
			avatarIcon = getElementScreenshot(driver.findElement(By.xpath(String.format(AndroidLocators.xpathGroupChatInfoContacts, i,1))));
			String avatarName = driver.findElement(By.xpath(String.format(AndroidLocators.xpathGroupChatInfoContacts, i,2))).getText();
			if(avatarName.equalsIgnoreCase("AQAPICTURECONTACT")){
				BufferedImage realImage = ImageUtil.readImageFromFile(path+AVATAR_WITH_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
			}
			if(avatarName.equalsIgnoreCase("AQAAVATAR TESTCONTACT")){
				//must be a yellow user with initials AT
				BufferedImage realImage = ImageUtil.readImageFromFile(path+AVATAR_NO_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
			}
		}
		return true;
	}
	
	public BufferedImage getElementScreenshot(WebElement element) throws IOException{
		BufferedImage screenshot = takeScreenshot();
		Point elementLocation = element.getLocation();
		Dimension elementSize = element.getSize();
		return screenshot.getSubimage(elementLocation.x, elementLocation.y, elementSize.width, elementSize.height);
	}

	public String getSubHeader() {
		return participantsSubHeader.getText();
	}

	public String getConversationName() {
		refreshUITree();
		return groupChatName.getText();
	}	
}
