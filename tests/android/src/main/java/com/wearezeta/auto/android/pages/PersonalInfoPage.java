package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.*;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class PersonalInfoPage extends AndroidPage
{
	@FindBy(how = How.ID, using = AndroidLocators.idEmailField)
	private WebElement emailField;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.classNameTextView)
	private List<WebElement> optionsButtons;
	
	public PersonalInfoPage(String URL, String path) throws IOException {
		super(URL, path);

	}
	
	public void waitForEmailFieldVisible(){
		
		wait.until(ExpectedConditions.visibilityOf(emailField));
	}
	
	public void tapOptionsButtonByText(String buttonText){
		
		 for(WebElement button : optionsButtons)
		 {
			 if (button.getText().equals(buttonText))
			 {
				 button.click();
				 break;
			 }
		 }
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) {
		
		AndroidPage page = null;
		switch (direction){
		case DOWN:
		{
			break;
		}
		case UP:
		{
			page = this;
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

}
