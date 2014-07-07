package com.wearezeta.auto.android;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	
	@When("^I tap options button$")
	public void WhenITapOptionsButton() throws Throwable {
		PagesCollection.personalInfoPaga.tapOptionsButton();
	}
	
	@When("^I tap on my name$")
	public void WhenITapOnMyName() throws Throwable {
		PagesCollection.personalInfoPaga.tapOnMyName();
	}

	@When("^I tap sign out button$")
	public void WhenITapSignOutButton() throws Throwable {
		PagesCollection.personalInfoPaga.tapSignOutBtn();
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
	
	@When("^I change (.*) to (.*)$")
	public void IChangeNameTo(String name, String newName) throws Throwable {
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		PagesCollection.personalInfoPaga.changeName(name, newName);
	}

	@Then("^I see my new name (.*) and return old (.*)$")
	public void ISeeMyNewName(String name, String oldName) throws Throwable {
	    Assert.assertTrue(name.equals(PagesCollection.personalInfoPaga.getUserName()));
	    oldName = CommonUtils.retrieveRealUserContactPasswordValue(oldName);
		PagesCollection.personalInfoPaga.changeName(name, oldName);
	    
	}
	
	@Then("I see changed user picture")
	public void ThenISeeChangedUserPicture() throws IOException {
		PagesCollection.personalInfoPaga.waitForConfirmBtn();
		BufferedImage referenceImage = PagesCollection.personalInfoPaga.takeScreenshot();
		String path = CommonUtils.getResultImagePath(PersonalInfoPageSteps.class);
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.75, current = " + score,
				score >= 0.75d);
		
	}
	
}
