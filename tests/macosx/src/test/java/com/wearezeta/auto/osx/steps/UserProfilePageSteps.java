package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UserProfilePageSteps {
	
	private BufferedImage userProfileBefore = null;
	
	@Given("I open picture settings")
	public void GivenIOpenPictureSettings() throws IOException {
		userProfileBefore = CommonSteps.senderPages.getUserProfilePage().takeScreenshot();
		CommonSteps.senderPages.getUserProfilePage().openPictureSettings();
	}
	
	@When("I choose to select picture from image file")
	public void WhenIChooseToSelectPictureFromImageFile() throws MalformedURLException, IOException {
		CommonSteps.senderPages.getUserProfilePage().openChooseImageFileDialog();
		CommonSteps.senderPages.setChoosePicturePage(new ChoosePicturePage(
				 CommonUtils.getUrlFromConfig(UserProfilePageSteps.class),
				 CommonUtils.getAppPathFromConfig(UserProfilePageSteps.class)
				 ));
	}
	
	@When("I choose to select picture from camera")
	public void WhenIChooseToSelectPictureFromCamera() {
		CommonSteps.senderPages.getUserProfilePage().openCameraDialog();
	}
	
	@When("I select image file (.*)")
	public void WhenISelectImageFile(String imageFilename) throws InterruptedException {
		ChoosePicturePage choosePicturePage = CommonSteps.senderPages.getChoosePicturePage();
		Assert.assertTrue(choosePicturePage.isVisible());
		
		choosePicturePage.openImage(imageFilename);
		
		CommonSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}
	
	@When("I shoot picture using camera")
	public void WhenIShootPictureUsingCamera() throws InterruptedException {
		CommonSteps.senderPages.getUserProfilePage().doPhotoInCamera();
		CommonSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}

	@Then("I see changed user picture from image (.*)")
	public void ThenISeeChangedUserPictureFromImage(String filename) throws IOException {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.openPictureSettings();
		BufferedImage referenceImage = userProfile.takeScreenshot();

		BufferedImage templateImage = ImageUtil.readImageFromFile(OSXPage.imagesPath + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.55, current = " + score,
				score >= 0.55d);
	}
	
	@Then("^I see changed user picture$")
	public void ThenISeeChangedUserPicture() throws IOException {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		BufferedImage userProfileAfter = userProfile.takeScreenshot();

		double score = ImageUtil.getOverlapScore(userProfileAfter, userProfileBefore, ImageUtil.RESIZE_NORESIZE);
		Assert.assertFalse(
				"Overlap between two images has no enough score. Expected >= 0.95, current = " + score,
				score >= 0.95d);
	}
	
	@When("I select to remove photo")
	public void ISelectToRemovePhoto() {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.chooseToRemovePhoto();
	}
	
	@When("I confirm photo removing")
	public void IConfirmPhotoRemoving() {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.confirmPhotoRemoving();
	}
	
	@When("I cancel photo removing")
	public void ICancelPhotoRemoving() {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.cancelPhotoRemoving();
	}
	
	@Then("I see user profile picture is not set")
	public void ISeeUserProfilePictureIsNotSet() throws IOException {
		ThenISeeChangedUserPicture();
	}
	
	@When("I see photo in User profile")
	public void ISeePhotoInUserProfile() throws IOException {
		userProfileBefore = CommonSteps.senderPages.getUserProfilePage().takeScreenshot();
	}
	

    @Then("I see name (.*) in User profile")
    public void ISeeNameInUserProfile(String name) {
    	name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.selfProfileNameEquals(name);
    }
    
    @Then("I see email of (.*) in User profile")
    public void ISeeEmailOfUserInUserProfile(String name) {
    	name = CommonUtils.retrieveRealUserContactPasswordValue(name);
    	String email = null;
    	for (ClientUser user : CommonUtils.yourUsers) {
			if (user.getName().toLowerCase().equals(name.toLowerCase())) {
				email = user.getEmail();
				break;
			}
		}
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.selfProfileEmailEquals(email);
    	
    }
}
