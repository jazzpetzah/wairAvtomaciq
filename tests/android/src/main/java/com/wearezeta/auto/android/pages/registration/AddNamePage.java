package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class AddNamePage extends AndroidPage {

	public static final String idNameInput = "et__reg__name";
	public static final String idConfirmButton = "pcb__signup";
	
	public AddNamePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void inputName(String name) throws Exception {
		this.getDriver().findElementById(idNameInput).sendKeys(name);
	}
	
	public ProfilePicturePage clickConfirm() throws Exception {
		this.getDriver().findElementById(idConfirmButton).click();
		return new ProfilePicturePage(this.getLazyDriver());
	}

}
