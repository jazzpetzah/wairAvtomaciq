package com.wearezeta.auto.osx.pages;

import java.io.IOException;
import java.net.MalformedURLException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.osx.locators.OSXLocators;

public class MainMenuPage extends OSXPage {
	
	@FindBy(how = How.NAME, using = OSXLocators.nameQuitZClientMenuItem)
	private WebElement quitZClientMenuItem;

	@FindBy(how = How.NAME, using = OSXLocators.nameSignOutMenuItem)
	private WebElement signOutMenuItem;
	
	public MainMenuPage(String URL, String path) throws MalformedURLException {
		super(URL, path);
	}

	public void SignOut() {
		signOutMenuItem.click();
		driver.navigate().to("ZClient");
	}
	
	@Override
	public void Close() throws IOException {
		try {
			SignOut();
			quitZClientMenuItem.click();
		} catch (Exception e) { }
		super.Close();
	}
}
