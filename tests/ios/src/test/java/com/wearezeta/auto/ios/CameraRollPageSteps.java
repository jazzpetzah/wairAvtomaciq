package com.wearezeta.auto.ios;

import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.DriverUtils;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.OtherUserPersonalInfoPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.pages.CameraRollPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CameraRollPageSteps {
	
	@When("^I press Camera Roll button$")
	public void IPressCameraRollButton() throws Throwable {
		PagesCollection.cameraRollPage.pressCameraRollButton();
	}
	
	@When("^I choose a picture from camera roll$")
	public void IChooseAPictureFromCameraRoll() throws Throwable {
		PagesCollection.cameraRollPage.openCameraRoll();
	}
	
	@When("^I press Confirm button$")
	public void IPressConfirmButton() throws Throwable {
		PagesCollection.cameraRollPage.pressConfirmButton();
	}
	



}
