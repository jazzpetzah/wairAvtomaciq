package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PagesCollection;
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
	 * @throws Exception
	 */
	@When("^I tap options button$")
	public void WhenITapOptionsButton() throws Exception {
		PagesCollection.personalInfoPage.tapOptionsButton();
	}

	/**
	 * Taps on the current users name
	 * 
	 * @step. ^I tap on my name$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on my name$")
	public void WhenITapOnMyName() throws Exception {
		PagesCollection.personalInfoPage.tapOnMyName();
	}

	/**
	 * Taps on the sign out button in the options menu
	 * 
	 * @step. ^I tap sign out button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap sign out button$")
	public void WhenITapSignOutButton() throws Exception {
		PagesCollection.personalInfoPage.tapSignOutBtn();
	}

	/**
	 * Taps on the settings button in the options menu
	 * 
	 * @step. ^I tap settings button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap settings button$")
	public void WhenITapSettingsButton() throws Exception {
		PagesCollection.settingsPage = PagesCollection.personalInfoPage
				.tapSettingsButton();
	}

	/**
	 * Taps the about button in the options menu
	 * 
	 * @step. ^I swipe right to contact list$
	 * 
	 * @throws Exception
	 */
	@When("^I tap about button$")
	public void WhenITapAboutButton() throws Exception {
		PagesCollection.aboutPage = PagesCollection.personalInfoPage
				.tapAboutButton();
	}

	/**
	 * Taps on the centre of the screen to bring up the current user's photo in
	 * colour
	 * 
	 * @step. ^I tap on personal info screen$
	 * 
	 * @throws Exception
	 */
	@When("^I tap on personal info screen$")
	public void WhenITapOnPersonalInfoScreen() throws Exception {
		PagesCollection.personalInfoPage.tapOnPage();
	}

	/**
	 * Taps on the photo button to show the user posibilities to change their
	 * profile picture
	 * 
	 * @step. ^I tap change photo button$
	 * 
	 * @throws Exception
	 */
	@When("^I tap change photo button$")
	public void WhenITapChangePhotoButton() throws Exception {
		PagesCollection.personalInfoPage.tapChangePhotoButton();
	}

	/**
	 * Presses on the gallery button to select a photo from the phone's storage
	 * 
	 * @step. ^I press Gallery button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Gallery button$")
	public void WhenIPressGalleryButton() throws Exception {
		PagesCollection.personalInfoPage.tapGalleryButton();
	}

	/**
	 * Selects a picture from the gallery
	 * 
	 * @step. ^I select picture$
	 * 
	 * @throws Exception
	 */
	@When("^I select picture$")
	public void WhenISelectPicture() throws Exception {
		PagesCollection.personalInfoPage.selectFirstGalleryPhoto();
	}

	/**
	 * Confirms the selected picture
	 * 
	 * @step. ^I press Confirm button$
	 * 
	 * @throws Exception
	 */
	@When("^I press Confirm button$")
	public void WhenIPressConfirmButton() throws Exception {
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
	 * @throws Exception
	 */
	@When("^I change (.*) to (.*)$")
	public void IChangeNameTo(String name, String newName) throws Exception {
		try {
			name = usrMgr.findUserByNameOrNameAlias(name).getName();
		} catch (NoSuchUserException e) {
			// Ignore silently
		}
		PagesCollection.personalInfoPage.changeName(name, newName);
		usrMgr.getSelfUser().setName(newName);
	}

	/**
	 * Swipe right to contact list
	 * 
	 * @step. ^I swipe right to contact list$
	 * 
	 * @throws Exception
	 */
	@When("^I close Personal Info Page$")
	public void IClosePersonalInfoPage() throws Exception {
		PagesCollection.contactListPage = (ContactListPage) PagesCollection.personalInfoPage
				.pressCloseButton();
	}

	/**
	 * Confirms that the current user's name is as given, and then changes it
	 * back to the old one (Why not reuse the old step IChangeNameTo(String,
	 * String))
	 * 
	 * @step. ^I see my new name (.*) and return old (.*)$
	 * 
	 * @param name
	 *            The current (newly given) name of the current user
	 * @throws Exception
	 */
	@Then("^I see my new name (.*)$")
	public void ISeeMyNewName(String name) throws Exception {
		Assert.assertTrue(name.equals(PagesCollection.personalInfoPage
				.getUserName()));
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
	 * Verify that Settings page is visible
	 * 
	 * @step. ^I see Settings$
	 *
	 * @throws Exception
	 */
	@Then("^I see Settings$")
	public void ThenISeeSettings() throws Exception {
		Assert.assertTrue(PagesCollection.personalInfoPage.isSettingsVisible());
	}

	/**
	 * Checks that app menu options are not reachable after the user taps on
	 * self name
	 * 
	 * @step. ^Menu options are unreachable$
	 *
	 * @throws Exception
	 */
	@Then("^Menu options are unreachable$")
	public void ThenMenuOptionsAreUnreachable() throws Exception {
		PagesCollection.personalInfoPage.tapOptionsButton();
		Assert.assertTrue("Options menu still exists",
				PagesCollection.personalInfoPage
						.waitForOptionsMenuToDisappear());
	}

	/**
	 * Checks that self name edit field is showed
	 * 
	 * @step. ^I see edit name field$
	 *
	 * @throws Exception
	 */
	@Then("^I see edit name field$")
	public void ThenISeeEditNameField() throws Exception {
		Assert.assertTrue(PagesCollection.personalInfoPage.isNameEditVisible());
	}

	/**
	 * Clear self name edit field
	 * 
	 * @step. ^I clear name field$
	 *
	 * @throws Exception
	 */
	@When("^I clear name field$")
	public void IClearNameField() throws Exception {
		PagesCollection.personalInfoPage.clearSelfName();
	}

	private BufferedImage previousProfilePicture = null;

	/**
	 * Takes the screenshot of current screen and saves it to the internal
	 * variable
	 * 
	 * @step. ^I remember my current profile picture$
	 * 
	 * @throws Exception
	 */
	@When("^I remember my current profile picture$")
	public void IRememberCurrentPicture() throws Exception {
		previousProfilePicture = PagesCollection.personalInfoPage
				.takeScreenshot().orElseThrow(AssertionError::new);
	}

	private static final int PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS = 60;
	private static final double MAX_OVERLAP_SCORE = 0.50;

	/**
	 * Verify that profile picture is different from the previously snapshotted
	 * one within the predefined timeout (use special step to create a snapshot)
	 * 
	 * @step. ^I verify that my current profile picture is different from the
	 *        previous one$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify that my current profile picture is different from the previous one$")
	public void IVerifyMyCurrentPuictureIsDifferent() throws Exception {
		if (previousProfilePicture == null) {
			throw new IllegalStateException(
					"This step requires to remember the previous profile picture first!");
		}
		final long millisecondsStarted = System.currentTimeMillis();
		double score = -1;
		do {
			final BufferedImage currentProfilePicture = PagesCollection.personalInfoPage
					.takeScreenshot().orElseThrow(AssertionError::new);
			score = ImageUtil.getOverlapScore(currentProfilePicture,
					previousProfilePicture);
			Thread.sleep(3000);
		} while (score > MAX_OVERLAP_SCORE
				&& System.currentTimeMillis() - millisecondsStarted <= PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS * 1000);
		Assert.assertTrue(
				String.format(
						"Profile picture has not been updated properly after %s seconds timeout (current overlap score value is %2.2f)",
						PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS, score),
				score <= MAX_OVERLAP_SCORE);
	}
}