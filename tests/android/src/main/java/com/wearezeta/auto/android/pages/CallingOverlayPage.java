package com.wearezeta.auto.android.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class CallingOverlayPage extends AndroidPage {

	By callingOverlayContainer = By.id(PACKAGE_NAME
			+ ":id/coc__calling__overlay_container");
	
	By ignoreButton = By.id(PACKAGE_NAME + 
			":id/cib__calling_mute");
	
	By acceptButton = By.id(PACKAGE_NAME
			+ ":id/gtv__conversation_list__sticky_menu__trigger_startui");
	
	By callingMessage = By.id(PACKAGE_NAME + 
			":id/ttv__calling__message");

	public CallingOverlayPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isVisible() throws Exception {
		List<WebElement> callbars = driver
				.findElements(callingOverlayContainer);
		if (callbars.size() > 0) {
			return true;
		}
		return false;
	}

	public void muteConversation() {
		driver.findElement(ignoreButton).click();
	}

	public String getCallersName() {
		return driver.findElement(callingMessage).getText();
	}

}
