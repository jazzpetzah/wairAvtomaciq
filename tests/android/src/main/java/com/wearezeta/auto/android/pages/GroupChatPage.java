package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class GroupChatPage  extends AndroidPage{

	public static final String I_LEFT_CHAT_MESSAGE = "YOU HAVE LEFT";
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classListView)
	private WebElement container;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idMessage")
	private WebElement message;

	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.CLASS_NAME, locatorKey = "idMessage")
	private List<WebElement> messages;
	
	private String url;
	private String path;

	public GroupChatPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage swipeUp(int time) throws Exception
	{
		dialogsPagesSwipeUp(time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception{

		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			page = new GroupChatInfoPage(url,path);
			break;
		}
		case LEFT:
		{
			
			break;
		}
		case RIGHT:
		{
			page = new ContactListPage(url,path);
			break;
		}
		}	
		return page;
	}

	public boolean isGroupChatDialogVisible(){
		return DriverUtils.waitUntilElementAppears(driver, By.id(AndroidLocators.idMessage));
	}

	public boolean isGroupChatDialogContainsNames(String name1, String name2)
	{
		return (message.getText().toLowerCase().contains(name1.toLowerCase()) && message.getText().toLowerCase().contains(name2.toLowerCase()));
	}
	
	public boolean isMessageExists(String messageText){
		boolean flag = false;
		refreshUITree();
		for(WebElement element : messages)
		{
			String text = element.getText();
			if(text.equals(messageText))
			{
				flag = true;
				break;
			}
		}
		return flag;
	}
}
