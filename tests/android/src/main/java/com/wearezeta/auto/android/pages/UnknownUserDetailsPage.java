package com.wearezeta.auto.android.pages;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class UnknownUserDetailsPage extends AndroidPage {	

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserFromGroupDetailsPage.CLASS_NAME, locatorKey = "idOtherUsersName")
	private WebElement otherUsersName;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserFromGroupDetailsPage.CLASS_NAME, locatorKey = "idConnectButton")
	private WebElement connectAndPendingButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.UnknownUserFromGroupDetailsPage.CLASS_NAME, locatorKey = "idCommonUsersLabel")
	private WebElement commonUsersLabel;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classEditText)
	private List<WebElement> classEditText;
	
	public UnknownUserDetailsPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws IOException {
		return null;
	}

	public boolean isConnectButtonVisible() throws Exception {
		refreshUITree();
		String connectButtonText = connectAndPendingButton.getText();
		return connectButtonText.equals("Connect");
	}
	
	public boolean isPendingButtonVisible() throws Exception {
		refreshUITree();
		String connectButtonText = connectAndPendingButton.getText();
		return connectButtonText.equals("Pending");
	}
	
	public String getOtherUsersName() {
		return otherUsersName.getText();
	}
	
	public ConnectToPage tapConnectAndPendingButton() throws Exception {
		refreshUITree();
		if (isConnectButtonVisible()) {
			connectAndPendingButton.click();
			return new ConnectToPage(this.getDriver(), this.getWait());
		}
		
		return null;
	}
}