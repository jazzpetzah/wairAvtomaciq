package com.wearezeta.auto.android.pages;

import java.io.IOException;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;

public class AboutPage extends AndroidPage {

	private String url;
	private String path;
	
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.AboutPage.CLASS_NAME, locatorKey = "idAboutLogo")
	private WebElement aboutLogo;
	@ZetaFindBy(how = How.ID, locatorsDb = AndroidLocators.AboutPage.CLASS_NAME, locatorKey = "idAboutVersion")
	private WebElement aboutVersion;
	
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
		return aboutLogo.isDisplayed();
		
	}

	public PersonalInfoPage tapOnVersion() throws Exception {
		aboutVersion.click();
		return new PersonalInfoPage(url, path);
	}

}
