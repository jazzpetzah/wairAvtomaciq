package com.wearezeta.auto.ios.pages;

import java.io.IOException;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatPage extends DialogPage {
	
	private String url;
	private String path;
	private String firstGroupChatMessage = "YOU ADDED %s, %s";

	public GroupChatPage(String URL, String path) throws IOException {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}
	
	public boolean isGroupChatDialogVisible(String name1, String name2){
		return DriverUtils.waitUntilElementAppears(driver, By.name(String.format(firstGroupChatMessage, name1.toUpperCase(), name2.toUpperCase())));
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
