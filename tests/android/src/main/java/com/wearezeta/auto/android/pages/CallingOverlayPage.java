package com.wearezeta.auto.android.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class CallingOverlayPage extends AndroidPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingOverlayContainer")
	private WebElement callingOverlayContainer;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idOngoingCallMicrobar")
	private WebElement ongoingCallMicrobar;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idOngoingCallMinibar")
	private WebElement ongoingCallMinibar;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idIncominCallerAvatar")
	private WebElement incominCallerAvatar;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idIgnoreButton")
	private WebElement ignoreButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idAcceptButton")
	private WebElement acceptButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlayPage.CLASS_NAME, locatorKey = "idCallingUsersName")
	private WebElement callingUsersName;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallMessage")
	private WebElement callMessage;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingDismiss")
	private WebElement callingDismiss;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingSpeaker")
	private WebElement callingSpeaker;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CallingOverlay.CLASS_NAME, locatorKey = "idCallingMicMute")
	private WebElement callingMicMute;
	
	public CallingOverlayPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(this.getDriver(), callingOverlayContainer);
	}

	public void muteConversation() {
		ignoreButton.click();
	}

	public String getCallersName() {
		return callingUsersName.getText();
	}

	public void acceptCall() {
		acceptButton.click();
	}

	public boolean incominCallerAvatarIsVisible() throws Exception {
        refreshUITree();
		return isVisible(callMessage);
	}
	
	public boolean callingMessageIsVisible() throws Exception {
        refreshUITree();
		return isVisible(callMessage);
	}

	public boolean callingDismissIsVisible() throws Exception {
		refreshUITree();
		return isVisible(callingDismiss);
	}

	public boolean callingSpeakerIsVisible() throws Exception {
		refreshUITree();
		return isVisible(callingSpeaker);
	}

	public boolean callingMicMuteIsVisible() throws Exception {
		refreshUITree();
		return isVisible(callingMicMute);
	}

	public boolean callingOverlayIsVisible() throws Exception {
		refreshUITree();
		return  isVisible(callingOverlayContainer);
	}

	public boolean ongoingCallMicrobarIsVisible() throws Exception {
		refreshUITree();
		return  isVisible(ongoingCallMicrobar);
	}
	
	public boolean ongoingCallMinibarIsVisible() throws Exception {
		refreshUITree();
		return  isVisible(ongoingCallMinibar);
	}
}


