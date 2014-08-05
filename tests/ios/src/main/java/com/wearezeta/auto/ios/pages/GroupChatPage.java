package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.ios.locators.IOSLocators;


public class GroupChatPage extends DialogPage {
	
	private String url;
	private String path;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathLastGroupChatMessage)
	private WebElement lastMessage;
	
	@FindBy(how = How.XPATH, using = IOSLocators.xpathNewGroupConversationNameChangeTextField)
	private WebElement newGroupConversationNameChangeTextField;
	
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
	
	public boolean isGroupChatPageVisible(){
		
		return DriverUtils.waitUntilElementAppears(driver, By.name(IOSLocators.nameConversationCursorInput));
	}
	
	public boolean isConversationChangedInChat(){
		
		System.out.println(newGroupConversationNameChangeTextField.getText());
		System.out.println(PagesCollection.groupChatInfoPage.getConversationName());
		
		String groupChatName = PagesCollection.groupChatInfoPage.getConversationName();
		return newGroupConversationNameChangeTextField.getText().contains(groupChatName);
	}
	
	public boolean isYouHaveLeftVisible(String name)
	{
		return driver.findElement(By.name(name.toUpperCase() + " HAS LEFT")).isDisplayed();
	}
	
	public boolean isUserAddedContactVisible(String user, String contact){
		return driver.findElement(By.name(user.toUpperCase() + " ADDED " + contact.toUpperCase())).isDisplayed();
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
	public IOSPage swipeRight(int time) throws IOException
	{
		WebElement element =  driver.findElement(By.name(IOSLocators.nameLoginPage));
		
		Point coords = element.getLocation();
		Dimension elementSize = element.getSize();
		driver.swipe(coords.x +10, coords.y + 30, coords.x + elementSize.width / 2 + 20, coords.y + 30, time);
		return returnBySwipe(SwipeDirection.RIGHT);
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
				page = new GroupChatInfoPage(url, path);
				break;
			}
			case LEFT:
			{
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
