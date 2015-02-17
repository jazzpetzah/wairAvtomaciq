package com.wearezeta.auto.android;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@When("^I tap options button$")
	public void WhenITapOptionsButton() throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButton();
	}

	@When("^I tap on my name$")
	public void WhenITapOnMyName() throws Throwable {
		PagesCollection.personalInfoPage.tapOnMyName();
	}

	@When("^I tap sign out button$")
	public void WhenITapSignOutButton() throws Throwable {
		PagesCollection.personalInfoPage.tapSignOutBtn();
	}

	@When("^I tap settings button$")
	public void WhenITapSettingsButton() throws Throwable {
		PagesCollection.settingsPage = PagesCollection.personalInfoPage
				.tapSettingsButton();
	}

	@When("^I tap on personal info screen$")
	public void WhenITapOnPersonalInfoScreen() throws Throwable {
		PagesCollection.personalInfoPage.clickOnPage();
	}

	@When("^I tap change photo button$")
	public void WhenITapChangePhotoButton() throws Throwable {
		PagesCollection.personalInfoPage.tapChangePhotoButton();
	}

	@When("^I press Gallery button$")
	public void WhenIPressGalleryButton() throws Throwable {
		PagesCollection.personalInfoPage.tapGalleryButton();
	}

	@When("^I select picture$")
	public void WhenISelectPicture() throws Throwable {
		PagesCollection.personalInfoPage.selectPhoto();
	}

	@When("^I press Confirm button$")
	public void WhenIPressConfirmButton() throws Throwable {
		PagesCollection.personalInfoPage.tapConfirmButton();
	}

	@When("^I change (.*) to (.*)$")
	public void IChangeNameTo(String name, String newName) throws Throwable {
		try{
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.changeName(name, newName);
		usrMgr.getSelfUser().setName(newName);
	}

	@When("^I swipe right to contact list$")
	public void ISwipeRightToContactList() throws Exception {

		PagesCollection.contactListPage = (ContactListPage) PagesCollection.personalInfoPage
				.navigateBack();
				//.swipeRight(1000);
	}

	@When("^I tap about button$")
	public void WhenITapAboutButton() throws Throwable {
		PagesCollection.aboutPage = PagesCollection.personalInfoPage
				.tapAboutButton();
	}

	@Then("^I see my new name (.*) and return old (.*)$")
	public void ISeeMyNewName(String name, String oldName) throws Throwable {
		Assert.assertTrue(name.equals(PagesCollection.personalInfoPage
				.getUserName()));
		try{
			oldName = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.tapOnMyName();
		PagesCollection.personalInfoPage.changeName(name, oldName);
	}

	@Then("^I see personal info page$")
	public void ISeePersonalInfoPage() {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isPersonalInfoVisible());
	}

	@Then("I see changed user picture")
	public void ThenISeeChangedUserPicture() throws Exception {
		BufferedImage referenceImage = PagesCollection.personalInfoPage
				.takeScreenshot();
		String path = CommonUtils
				.getResultImagePath(PersonalInfoPageSteps.class);
		BufferedImage templateImage = ImageUtil.readImageFromFile(path);
		double score = ImageUtil.getOverlapScore(referenceImage, templateImage);
		Assert.assertTrue(
				"Overlap between two images has not enough score. Expected >= 0.75, current = "
						+ score, score >= 0.75d);
	}

	@Then("^I see Settings$")
	public void ThenISeeSettings() throws Throwable {
		Assert.assertTrue(PagesCollection.personalInfoPage.isSettingsVisible());
	}

	@Then("^Settings button is unreachable$")
	public void ThenSettingsButtonIsUnreachable() throws Throwable {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isSettingsButtonNotVisible());
	}
}
