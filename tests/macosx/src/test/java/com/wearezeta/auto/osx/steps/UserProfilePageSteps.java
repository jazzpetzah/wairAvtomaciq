package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UserProfilePageSteps {
	@Given("I open picture settings")
	public void GivenIOpenPictureSettings() {
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
	public void WhenISelectImageFile(String imageFilename) {
		ChoosePicturePage choosePicturePage = CommonSteps.senderPages.getChoosePicturePage();
		Assert.assertTrue(choosePicturePage.isVisible());
		
		choosePicturePage.openImage(imageFilename);
		
		CommonSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}
	
	@When("I shoot picture using camera")
	public void WhenIShootPictureUsingCamera() {
		CommonSteps.senderPages.getUserProfilePage().doPhotoInCamera();
		CommonSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}

	@Then("I see changed user picture (.*)")
	public void ThenISeeChangedUserPicture(String filename) throws IOException {
		UserProfilePage userProfile = CommonSteps.senderPages.getUserProfilePage();
		userProfile.openPictureSettings();
		BufferedImage referenceImage = userProfile.takeScreenshot();

		BufferedImage templateImage = ImageUtil.readImageFromFile(OSXPage.imagesPath + filename);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		Assert.assertTrue(
				"Overlap between two images has no enough score. Expected >= 0.55, current = " + score,
				score >= 0.55d);
		
	}
}
