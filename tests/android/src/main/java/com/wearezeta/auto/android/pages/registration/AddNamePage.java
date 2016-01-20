package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AddNamePage extends AndroidPage {

	public static final By idNameInput = By.id("et__reg__name");
	
	public static final By idConfirmButton = By.id("pcb__signup");
	
	public AddNamePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void inputName(String name) throws Exception {
		getElement(idNameInput).sendKeys(name);
	}
	
	public void clickConfirm() throws Exception {
		getElement(idConfirmButton).click();
	}
}
