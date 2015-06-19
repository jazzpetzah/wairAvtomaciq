package com.wearezeta.auto.android.pages;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.registration.ProfilePicturePage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GiphyPreviewPage extends AndroidPage {
	public static final String idNameInput = "et__reg__name";
	@FindBy(id = idNameInput)
	private WebElement nameInput;
	
	public GiphyPreviewPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void inputName(String name) throws Exception {
		nameInput.sendKeys(name);
	}
	
}
