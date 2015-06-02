package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AddNamePage extends AndroidPage {

	public static final String idNameInput = "et__reg__name";
	@FindBy(id = idNameInput)
	private WebElement nameInput;
	
	public static final String idConfirmButton = "pcb__signup";
	@FindBy(id = idConfirmButton)
	private WebElement confirmButton;
	
	public AddNamePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void inputName(String name) throws Exception {
		nameInput.sendKeys(name);
	}
	
	public ProfilePicturePage clickConfirm() throws Exception {
		confirmButton.click();
		return new ProfilePicturePage(this.getLazyDriver());
	}

}
