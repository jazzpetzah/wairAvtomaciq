package com.wearezeta.auto.ios.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatInfoPage extends IOSPage{
	private String url;
	private String path;

	public GroupChatInfoPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
		this.url = URL;
		this.path = path;
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
