package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.IOSLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatPage extends DialogPage {
	
	private String url;
	private String path;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathLastGroupChatMessage)
	private WebElement lastMessage;
	
	@FindBy(how = How.NAME, using = IOSLocators.nameYouHaveLeft)
	private WebElement youLeft;

	public GroupChatPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public boolean areRequiredContactsAddedToChat(String name1, String name2){
		
		return lastMessage.getText().contains(name1) && lastMessage.getText().contains(name2);
	}
	
	public boolean isYouHaveLeftVisible()
	{
		return youLeft.isDisplayed();
	}
	
	public boolean isContactAvailableInChat(String contact) {
		WebElement el = null;
		boolean result = false;
		
		try {
			el = driver.findElementByName(contact);
		}
		catch (NoSuchElementException ex)
		{
			el = null;
		}
		finally {
			result = el != null;
		}
		
		return result;
	}
	
	public boolean waitForContactToDisappear(String contact) {
		
		return DriverUtils.waitUntilElementDissapear(driver, By.name(contact));
	}
	
	@Override
	public IOSPage returnBySwipe(SwipeDirection direction) throws IOException {
		IOSPage page = null;
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
				page = new GroupChatInfoPage(url, path);
				break;
			}
			case RIGHT:
			{
				page = new ContactListPage(url, path);
				break;
			}
		}	
		return page;
	}

}
