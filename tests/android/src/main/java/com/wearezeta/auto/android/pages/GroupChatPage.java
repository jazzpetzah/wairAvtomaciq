package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatPage  extends AndroidPage{

	public static final String I_LEFT_CHAT_MESSAGE = "YOU HAVE LEFT";
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classListView)
	private WebElement container;
	
	@FindBy(how = How.ID, using = AndroidLocators.idMessage)
	private WebElement message;

	@FindBy(how = How.ID, using = AndroidLocators.idMessage)
	private List<WebElement> messages;
	
	private String url;
	private String path;

	public GroupChatPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage swipeUp(int time) throws IOException
	{
		dialogsPagesSwipeUp(time);
		return returnBySwipe(SwipeDirection.UP);
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws IOException{

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
		
		for(WebElement element : messages)
		{
			String text = element.getText();
			if(text.equals(messageText))
			{
				flag = true;
			}
		}
		return flag;
	}
}
