package com.wearezeta.auto.osx.steps;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;

import org.junit.Assert;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.OSXPage;
import com.wearezeta.auto.osx.pages.UserProfilePage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class UserProfilePageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private BufferedImage userProfileBefore = null;
	private BufferedImage userProfileAfter = null;
	
	@Given("I open picture settings")
	public void GivenIOpenPictureSettings() throws IOException {
		CommonOSXSteps.senderPages.getUserProfilePage().openPictureSettings();
	}

	@When("I choose to select picture from image file")
	public void WhenIChooseToSelectPictureFromImageFile()
			throws MalformedURLException, IOException {
		CommonOSXSteps.senderPages.getUserProfilePage()
				.openChooseImageFileDialog();
		CommonOSXSteps.senderPages
				.setChoosePicturePage(new ChoosePicturePage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(UserProfilePageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(UserProfilePageSteps.class)));
	}

	@When("I choose to select picture from camera")
	public void WhenIChooseToSelectPictureFromCamera() {
		CommonOSXSteps.senderPages.getUserProfilePage().openCameraDialog();
	}

	@When("I select image file (.*)")
	public void WhenISelectImageFile(String imageFilename)
			throws InterruptedException {
		ChoosePicturePage choosePicturePage = CommonOSXSteps.senderPages
				.getChoosePicturePage();
		Assert.assertTrue(choosePicturePage.isVisible());

		choosePicturePage.openImage(imageFilename);

		CommonOSXSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}

	@When("I shoot picture using camera")
	public void WhenIShootPictureUsingCamera() throws InterruptedException {
		CommonOSXSteps.senderPages.getUserProfilePage().doPhotoInCamera();
		CommonOSXSteps.senderPages.getUserProfilePage().confirmPictureChoice();
	}

	@Then("I see changed user picture from image (.*)")
	public void ThenISeeChangedUserPictureFromImage(String filename)
			throws IOException {
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.openPictureSettings();
		BufferedImage referenceImage = userProfile.takeScreenshot();

		final double minOverlapScore = 0.55d;
		BufferedImage templateImage = ImageUtil
				.readImageFromFile(OSXPage.imagesPath + filename);
		final double score = ImageUtil.getOverlapScore(referenceImage,
				templateImage);
		Assert.assertTrue(
				String.format(
						"Overlap between two images has no enough score. Expected >= %f, current = %f",
						minOverlapScore, score), score >= minOverlapScore);
	}

	@Then("^I see changed user picture$")
	public void ThenISeeChangedUserPicture() throws IOException {
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		if (userProfileAfter != null) {
			userProfileBefore = userProfileAfter;
		}
		userProfileAfter = userProfile.takeScreenshot();

		final double minOverlapScore = 0.985d;
		final double score = ImageUtil.getOverlapScore(userProfileAfter,
				userProfileBefore, ImageUtil.RESIZE_NORESIZE);
		Assert.assertTrue(
				String.format(
						"Overlap between two images is larger than expected. Picture was not changed. Expected <= %f, current = %f",
						minOverlapScore, score), score <= minOverlapScore);
	}

	@When("I select to remove photo")
	public void ISelectToRemovePhoto() {
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.chooseToRemovePhoto();
	}

	@When("I confirm photo removing")
	public void IConfirmPhotoRemoving() {
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.confirmPhotoRemoving();
	}

	@When("I cancel photo removing")
	public void ICancelPhotoRemoving() {
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.cancelPhotoRemoving();
	}

	@Then("I see user profile picture is not set")
	public void ISeeUserProfilePictureIsNotSet() throws IOException {
		ThenISeeChangedUserPicture();
	}

	@When("I see photo in User profile")
	public void ISeePhotoInUserProfile() throws IOException {
		userProfileBefore = CommonOSXSteps.senderPages.getUserProfilePage()
				.takeScreenshot();
	}

	@Then("I see name (.*) in User profile")
	public void ISeeNameInUserProfile(String name) throws Exception {
		name = usrMgr.findUserByNameOrNameAlias(name).getName();
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.selfProfileNameEquals(name);
	}

	@Then("I see email of (.*) in User profile")
	public void ISeeEmailOfUserInUserProfile(String name) throws Exception {
		ClientUser dstUser = usrMgr.findUserByNameOrNameAlias(name);
		name = dstUser.getName();
		String email = dstUser.getEmail();
		UserProfilePage userProfile = CommonOSXSteps.senderPages
				.getUserProfilePage();
		userProfile.selfProfileEmailEquals(email);
	}
}
