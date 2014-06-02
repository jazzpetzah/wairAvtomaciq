package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.*;

import com.wearezeta.auto.common.AndroidLocators;
import com.wearezeta.auto.common.SwipeDirection;

public class InstructionsPage extends AndroidPage {

	@FindBy(how = How.ID, using = AndroidLocators.idInstructions)
	private WebElement instructions;
	
	private String url;
	private String path;
	
	public InstructionsPage(String URL, String path) throws IOException {
		super(URL, path);
		url = URL;
		this.path = path;				
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws IOException {
		
		AndroidPage page = null;
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
				page = new PersonalInfoPaga(url, path);
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
