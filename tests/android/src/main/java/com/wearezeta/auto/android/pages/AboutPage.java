package com.wearezeta.auto.android.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;

import java.io.IOException;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class AboutPage extends AndroidPage {

	private String url;
	private String path;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.AboutPage.CLASS_NAME, locatorKey = "idAboutLogo")
	private WebElement aboutLogo;
	@AndroidFindBy(xpath=AndroidLocators.AboutPage.xpathAboutClose)
	private WebElement aboutClose;
	
	public AboutPage(String URL, String path) throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean aboutLogoIsVisible() {
		refreshUITree();
		return aboutLogo.isDisplayed();
		
	}

	public PersonalInfoPage tapOnVersion() throws Exception {
		aboutClose.click();
		return new PersonalInfoPage(url, path);
	}

}
