package com.wearezeta.auto.android;

import java.io.IOException;

import com.wearezeta.auto.android.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {

	@When("^I pull up for options$")
	public void WhenIPullUpForOptions() throws IOException {
		PagesCollection.personalInfoPaga.swipeUp(1000);
	}

	@When("^I press options button (.*)$")
	public void WhenIPressOptionsButton(String buttonName) throws Throwable {
		PagesCollection.personalInfoPaga.tapOptionsButtonByText(buttonName);
	}
	
	@When("^I tap on personal info screen$")
	public void WhenITapOnPersonalInfoScreen() throws Throwable {
		PagesCollection.personalInfoPaga.clickOnPage();
	}

	@When("^I tap change photo button$")
	public void WhenITapChangePhotoButton() throws Throwable {
		PagesCollection.personalInfoPaga.tapChangePhotoButton();
	}

	@When("^I press Gallery button$")
	public void WhenIPressGalleryButton() throws Throwable {
		PagesCollection.personalInfoPaga.tapGalleryButton();
	}

	@When("^I select picture$")
	public void WhenISelectPicture() throws Throwable {
		PagesCollection.personalInfoPaga.selectPhoto();
	}
	
	@When("^I press Confirm button$")
	public void WhenIPressConfirmButton() throws Throwable {
		PagesCollection.personalInfoPaga.tapConfirmButton();
	}
	
}
