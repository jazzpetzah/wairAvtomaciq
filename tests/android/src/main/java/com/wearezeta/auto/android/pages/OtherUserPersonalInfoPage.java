package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.SwipeDirection;

public class OtherUserPersonalInfoPage extends AndroidPage {

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.80;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private List<WebElement> otherUserName;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoMail)
	private List<WebElement> otherUserMail;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoSingleName)
	private List<WebElement> otherUserSingleName;
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoSingleMail)
	private List<WebElement> otherUserSingleMail;
	
	@FindBy(how = How.ID, using = AndroidLocators.idUserProfileConfirmationMenu)
	private WebElement confirmMenu;
	
	@FindBy(how = How.ID, using = AndroidLocators.idLeaveConversationButton)
	private WebElement removeBtn;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameFrameLayout)
	private WebElement frameLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameLoginPage)
	private WebElement backGround;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private WebElement confirmBtn;
	
	@FindBy(how = How.ID, using = AndroidLocators.idAddContactBtn)
	private WebElement addContactBtn;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path)
			throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public void clickRemoveBtn(){
		removeBtn.click();
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		AndroidPage page = null;
		switch (direction){
			case DOWN:
			{
				page = new PeoplePickerPage(url, path);
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
				break;
			}
		}	
		return page;
	}

	/*@Override
	public AndroidPage swipeUp(int time) throws IOException
	{
		DriverUtils.swipeUp(driver, frameLayout, time);
		return returnBySwipe(SwipeDirection.UP);
	}*/
	
	public boolean isOtherUserNameVisible(String name) {
		refreshUITree();//workaround to refresh UI tree
		String text;
		if(otherUserName.size() > 0){
			text = otherUserName.get(0).getText().toLowerCase();
		}
		else{
			text = otherUserSingleName.get(0).getText().toLowerCase();
		}
		return text.equals(name.toLowerCase());
	}
	
	public boolean isOtherUserMailVisible(String mail) {
		refreshUITree();//workaround to refresh UI tree
		String text;
		if(otherUserName.size() > 0){
			text = otherUserMail.get(0).getText().toLowerCase();
		}
		else{
			text = otherUserSingleMail.get(0).getText().toLowerCase();
		}
		return text.equals(mail.toLowerCase());
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmMenu.isDisplayed();
	}
	
	public GroupChatInfoPage pressRemoveConfirmBtn() throws Exception
	{
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
		return new GroupChatInfoPage(url, path);
	}

	public void tapAddContactBtn() {
		addContactBtn.click();
		
	}

	public boolean isBackGroundImageCorrect(String imageName) throws IOException {
		BufferedImage bgImage = null;
		boolean flag = false;
		bgImage = getElementScreenshot(backGround);
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		BufferedImage realImage = ImageUtil.readImageFromFile(path+imageName);
		double score = ImageUtil.getOverlapScore(realImage, bgImage);
		System.out.println(score);
		if (score >= MIN_ACCEPTABLE_IMAGE_VALUE) {
			flag = true;
		}
		return flag;
	}

}
