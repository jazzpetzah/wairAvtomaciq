package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class OtherUserPersonalInfoPage extends AndroidPage {

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	
	@FindBy(how = How.ID, using = AndroidLocators.idOtherUserPersonalInfoName)
	private WebElement otherUserName;
	
	@FindBy(how = How.ID, using = AndroidLocators.idUserProfileConfirmationMenu)
	private WebElement confirmMenu;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> optionsButtons;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameFrameLayout)
	private WebElement frameLayout;
	
	@FindBy(how = How.ID, using = AndroidLocators.idConfirmBtn)
	private WebElement removeBtn;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path)
			throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public void clickRemoveBtn(){
		for(WebElement button : optionsButtons)
		 {
			 if (button.getText().equals(REMOVE_FROM_CONVERSATION_BUTTON))
			 {
				 button.click();
				 break;
			 }
		 }
	}
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
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

	@Override
	public AndroidPage swipeUp(int time) throws IOException
	{
		DriverUtils.swipeUp(driver, frameLayout, time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	public boolean isOtherUserNameVisible(String name) {
		wait.until(ExpectedConditions.visibilityOf(otherUserName));
		return otherUserName.getText().equals(name);
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmMenu.isDisplayed();
	}
	
	public GroupChatInfoPage pressRemoveBtn() throws IOException
	{
		removeBtn.click();
		return new GroupChatInfoPage(url, path);
	}
	

}
