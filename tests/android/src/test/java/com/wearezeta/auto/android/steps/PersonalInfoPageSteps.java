package com.wearezeta.auto.android.steps;

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

	/**
	 * Taps on the options button
	 * 
	 * @step. ^I tap options button$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap options button$")
	public void WhenITapOptionsButton() throws Throwable {
		PagesCollection.personalInfoPage.tapOptionsButton();
	}

	/**
	 * Taps on the current users name
	 * 
	 * @step. ^I tap on my name$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap on my name$")
	public void WhenITapOnMyName() throws Throwable {
		PagesCollection.personalInfoPage.tapOnMyName();
	}

	/**
	 * Taps on the sign out button in the options menu
	 * 
	 * @step. ^I tap sign out button$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap sign out button$")
	public void WhenITapSignOutButton() throws Throwable {
		PagesCollection.personalInfoPage.tapSignOutBtn();
	}

	/**
	 * Taps on the settings button in the options menu
	 * 
	 * @step. ^I tap settings button$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap settings button$")
	public void WhenITapSettingsButton() throws Throwable {
		PagesCollection.settingsPage = PagesCollection.personalInfoPage
				.tapSettingsButton();
	}

	/**
	 * Taps the about button in the options menu
	 * 
	 * @step. ^I swipe right to contact list$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap about button$")
	public void WhenITapAboutButton() throws Throwable {
		PagesCollection.aboutPage = PagesCollection.personalInfoPage
				.tapAboutButton();
	}

	/**
	 * Taps on the centre of the screen to bring up the current user's photo in
	 * colour
	 * 
	 * @step. ^I tap on personal info screen$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap on personal info screen$")
	public void WhenITapOnPersonalInfoScreen() throws Throwable {
		PagesCollection.personalInfoPage.clickOnPage();
	}

	/**
	 * Taps on the photo button to show the user posibilities to change their
	 * profile picture
	 * 
	 * @step. ^I tap change photo button$
	 * 
	 * @throws Throwable
	 */
	@When("^I tap change photo button$")
	public void WhenITapChangePhotoButton() throws Throwable {
		PagesCollection.personalInfoPage.tapChangePhotoButton();
	}

	/**
	 * Presses on the gallery button to select a photo from the phone's storage
	 * 
	 * @step. ^I press Gallery button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Gallery button$")
	public void WhenIPressGalleryButton() throws Throwable {
		PagesCollection.personalInfoPage.tapGalleryButton();
	}

	/**
	 * Selects a picture from the gallery
	 * 
	 * @step. ^I select picture$
	 * 
	 * @throws Throwable
	 */
	@When("^I select picture$")
	public void WhenISelectPicture() throws Throwable {
		PagesCollection.personalInfoPage.selectPhoto();
	}

	/**
	 * Confirms the selected picture
	 * 
	 * @step. ^I press Confirm button$
	 * 
	 * @throws Throwable
	 */
	@When("^I press Confirm button$")
	public void WhenIPressConfirmButton() throws Throwable {
		PagesCollection.personalInfoPage.tapConfirmButton();
	}

	/**
	 * Changes the current user's name to a new one
	 * 
	 * 
	 * @step. ^I change (.*) to (.*)$
	 * 
	 * @param name
	 *            the current user's name (is this necessary? - is there not an
	 *            id that can find the current name no matter what it is?)
	 * @param newName
	 *            the new name for the current user
	 * @throws Throwable
	 */
	@When("^I change (.*) to (.*)$")
	public void IChangeNameTo(String name, String newName) throws Throwable {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.changeName(name, newName);
		usrMgr.getSelfUser().setName(newName);
	}

	/**
	 * -unused
	 * 
	 * @step. ^I swipe right to contact list$
	 * 
	 * @throws Exception
	 */
	@When("^I swipe right to contact list$")
	public void ISwipeRightToContactList() throws Exception {

		PagesCollection.contactListPage = (ContactListPage) PagesCollection.personalInfoPage
				.navigateBack();
		// .swipeRight(1000);
	}

	/**
	 * Confirms that the current user's name is as given, and then changes it
	 * back to the old one (Why not reuse the old step
	 * IChangeNameTo(String, String))
	 * 
	 * @step. ^I see my new name (.*) and return old (.*)$
	 * 
	 * @param name
	 *            The current (newly given) name of the current user
	 * @param oldName
	 *            The original name of the current user
	 * @throws Throwable
	 */
	@Then("^I see my new name (.*) and return old (.*)$")
	public void ISeeMyNewName(String name, String oldName) throws Throwable {
		Assert.assertTrue(name.equals(PagesCollection.personalInfoPage
				.getUserName()));
		try {
			oldName = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.tapOnMyName();
		PagesCollection.personalInfoPage.changeName(name, oldName);
	}

	/**
	 * Checks to see that the personal info page is visible
	 * 
	 * @step. ^I see personal info page$
	 *
	 * @throws Exception
	 */
	@Then("^I see personal info page$")
	public void ISeePersonalInfoPage() throws Exception {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isPersonalInfoVisible());
	}

	/**
	 * Checks to see that the user's profile picture has been changed to some
	 * new picture The image to be checked should be placed in the default
	 * images path
	 * 
	 * @step. ^I see changed user picture$
	 *
	 * @throws Exception
	 */
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

	/**
	 * -unused
	 * 
	 * @step. ^I see Settings$
	 *
	 * @throws Throwable
	 */
	@Then("^I see Settings$")
	public void ThenISeeSettings() throws Throwable {
		Assert.assertTrue(PagesCollection.personalInfoPage.isSettingsVisible());
	}

	/**
	 * Checks to see that no settings button is visible when the user taps on
	 * their own name to edit it
	 * 
	 * @step. ^Settings button is unreachable$
	 *
	 * @throws Throwable
	 */
	@Then("^Settings button is unreachable$")
	public void ThenSettingsButtonIsUnreachable() throws Throwable {
		Assert.assertTrue(PagesCollection.personalInfoPage
				.isSettingsButtonNotVisible());
	}
}
