package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import org.openqa.selenium.By;

/**
 * This page appears when the user selects either the area code in front of the phone number,
 * or the country name above the phone number
 * @author deancook
 *
 */
public class AreaCodePage extends AndroidPage {
	
	public static final String idStrCode = "ttv_new_reg__signup__phone__country__row_code";

	public static final Function<String, String> xpathStrFindAreaCode = areaCode -> String
		.format("//*[@id='%s' and @value='%s']", idStrCode, areaCode);
	
	public AreaCodePage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}
	
	public void selectAreaCode(String areaCode) throws Exception {
		getElement(By.xpath(xpathStrFindAreaCode.apply(areaCode))).click();
	}

}
