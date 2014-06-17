package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.common.SwipeDirection;

public class GroupChatPage  extends AndroidPage{

	@FindBy(how = How.ID, using = AndroidLocators.idDialogMessages)
	private WebElement message;
	
	
	public GroupChatPage(String URL, String path) throws IOException {
		super(URL, path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isGroupChatDialogVisible(){
		return DriverUtils.waitUntilElementAppears(driver, By.id(AndroidLocators.idDialogMessages));
	}
	
	public boolean isGroupChatDialogContainsNames(String name1, String name2)
	{
		return (message.getText().toLowerCase().contains(name1.toLowerCase()) && message.getText().toLowerCase().contains(name2.toLowerCase()));
	}

}
