package com.wearezeta.auto.android_tablet.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android_tablet.pages.TabletSelfProfilePage;
import com.wearezeta.auto.android_tablet.pages.camera.SelfProfileCameraPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SelfProfilePageSteps {
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private TabletSelfProfilePage getSelfProfilePage() throws Exception {
		return pagesCollection.getPage(TabletSelfProfilePage.class);
	}

	private SelfProfileCameraPage getSelfProfileCameraPage() throws Exception {
		return pagesCollection.getPage(SelfProfileCameraPage.class);
	}

	/**
	 * Verify that self name is visible on Self Profile page
	 * 
	 * @step. ^I see my name on (?:the |\\s*)[Ss]elf [Pp]rofile page$"
	 * 
	 * @throws Exception
	 */
	@Then("^I see my name on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ISeeMyName() throws Exception {
		final String name = usrMgr.getSelfUserOrThrowError().getName();
		Assert.assertTrue(String.format(
				"Self name '%s' is not visible on Self Profile page", name),
				getSelfProfilePage().isNameVisible(name));
	}

	/**
	 * Tap Options button on Self Profile page
	 * 
	 * @step. ^I tap Options button on (?:the |\\s*)[Ss]elf [Pp]rofile page$"
	 * 
	 * @throws Exception
	 */
	@When("^I tap Options button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ITapOptionsButton() throws Exception {
		getSelfProfilePage().tapOptionsButton();
	}

	/**
	 * Select the corresponding item from Options menu
	 * 
	 * @step. ^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile
	 *        page$
	 * @param itemName
	 *            the name of existing Options menu item
	 * 
	 * @throws Exception
	 */
	@When("^I select \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ISelectOptionsMenuItem(String itemName) throws Exception {
		getSelfProfilePage().selectOptionsMenuItem(itemName);
	}

	/**
	 * Verify whether the corresponding item from Options menu is visible
	 * 
	 * @step. ^I see \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * @param itemName
	 *            the name of an menu item
	 * 
	 * @throws Exception
	 */
	@When("^I see \"(.*)\" menu item on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ISeeOptionsMenuItem(String itemName) throws Exception {
		Assert.assertTrue(
				String.format("'%s' menu item is not visible", itemName),
				getSelfProfilePage().waitUntilOptionsMenuItemVisible(itemName));
	}

	/**
	 * Tap the self name field on Self Profile page
	 * 
	 * @step. ^I tap my name field on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * @throws Exception
	 * 
	 */
	@When("^I tap my name field on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ITapMyName() throws Exception {
		getSelfProfilePage().tapSelfNameField();
	}

	/**
	 * Change self name to the passed one
	 * 
	 * @step. ^I change my name to (.*) on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * 
	 * @param newName
	 *            a new name
	 * @throws Exception
	 */
	@And("^I change my name to (.*) on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void IChangeMyNameTo(String newName) throws Exception {
		getSelfProfilePage().changeSelfNameTo(newName);
		usrMgr.getSelfUserOrThrowError().setName(newName);
	}

	/**
	 * Tap in the center of the screen on Self Profile page to switch to full
	 * color view
	 * 
	 * @step. ^I tap in the center of [Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap in the center of [Ss]elf [Pp]rofile page$")
	public void ITapInTheCenter() throws Exception {
		getSelfProfilePage().tapInTheCenter();
	}

	private BufferedImage savedProfileScreenshot;

	/**
	 * Stores the screenshot of current Self Profile screen for the further
	 * comparison
	 * 
	 * @step. ^I remember my current profile picture on [Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@When("^I remember my current profile picture on [Ss]elf [Pp]rofile page$")
	public void IRememberMyCurrentProfilePictureTablet() throws Exception {
		savedProfileScreenshot = getSelfProfilePage().getScreenshot();
	}

	/**
	 * Tap the Change Picture button
	 * 
	 * @step. ^I tap Change Picture button on (?:the |\\s*)[Ss]elf [Pp]rofile
	 *        page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Change Picture button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ITapChangePictureButton() throws Exception {
		getSelfProfileCameraPage().tapLensButton();
	}

	/**
	 * Verify whether the Take Photo button is visible
	 * 
	 * @step. ^I see Take Photo button on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@Then("^I see Take Photo button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ISeeTakePhotoButton() throws Exception {
		Assert.assertTrue(
				"Take Photo button is still not visible after the timeout",
				getSelfProfileCameraPage().waitUntilTakePhotoButtonVisible());
	}

	/**
	 * Tap Take Photo button on Self Profile page
	 * 
	 * @step. ^I tap Take Photo button on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@Then("^I tap Take Photo button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ITapTakePhotoButton() throws Exception {
		getSelfProfileCameraPage().tapTakePhotoButton();
	}

	/**
	 * Tap the Gallery button
	 * 
	 * @step. ^I tap Gallery button on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@And("^I tap Gallery button on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void ITapGalleryButton() throws Exception {
		getSelfProfileCameraPage().tapGalleryButton();
	}

	/**
	 * Tap the Confirm button to confirm the picture selection in the Gallery
	 * 
	 * @step. ^I confirm my picture on (?:the |\\s*)[Ss]elf [Pp]rofile page$
	 * 
	 * @throws Exception
	 */
	@And("^I confirm my picture on (?:the |\\s*)[Ss]elf [Pp]rofile page$")
	public void IConfirmMyPicture() throws Exception {
		getSelfProfileCameraPage().confirmPictureSelection();
	}

	private static final double MAX_SCREENSHOTS_OVERLAP_SCORE = 0.6;
	private static final long PROFILE_PICTURE_UPDATE_TIMEOUT_MILLISECONDS = 10000;

	/**
	 * Compare the previous Self Profile page screenshot with the current one to
	 * calculate the overlap score
	 * 
	 * @step. ^I verify that my current profile picture is different from the
	 *        previous one$
	 * 
	 * @throws Exception
	 */
	@Then("^I verify that my current profile picture is different from the previous one$")
	public void IVerifyMyPictureDiffersFromThePreviousOne() throws Exception {
		if (savedProfileScreenshot == null) {
			throw new IllegalStateException(
					"Please take a screenshot of the previous page state first");
		}
		final long millisecondsStarted = System.currentTimeMillis();
		double overlapScore = 1;
		do {
			final BufferedImage currentProfileScreenshot = getSelfProfilePage()
					.getScreenshot();
			overlapScore = ImageUtil.getOverlapScore(currentProfileScreenshot,
					savedProfileScreenshot, ImageUtil.RESIZE_NORESIZE);
			if (overlapScore <= MAX_SCREENSHOTS_OVERLAP_SCORE) {
				break;
			}
		} while (System.currentTimeMillis() - millisecondsStarted <= PROFILE_PICTURE_UPDATE_TIMEOUT_MILLISECONDS);
		Assert.assertTrue(
				String.format(
						"The overlap value between the previous and the current profile picture is greater than expected (%.2f > %.02f)",
						overlapScore, MAX_SCREENSHOTS_OVERLAP_SCORE),
				overlapScore <= MAX_SCREENSHOTS_OVERLAP_SCORE);
	}

	/**
	 * Verify whether chathead notification is visible
	 * 
	 * @step. ^I (do not )?see chathead notification$
	 * 
	 * @param shouldNotSee
	 *            equals to null if the notification should be visible
	 * @throws Exception
	 */
	@Then("^I (do not )?see chathead notification$")
	public void ISeeChatheadNotification(String shouldNotSee) throws Exception {
		if (shouldNotSee == null) {
			Assert.assertTrue("Chathead notification is not visible",
					getSelfProfilePage().waitUntilChatheadNotificationVisible());
		} else {
			Assert.assertTrue("Chathead notification is still visible",
					getSelfProfilePage()
							.waitUntilChatheadNotificationInvisible());
		}
	}

	/**
	 * Tap chathead notification as soon as it appears on the screen
	 * 
	 * @step. ^I tap the chathead$
	 * 
	 * @throws Exception
	 */
	@And("^I tap the chathead notification$")
	public void ITapChathead() throws Exception {
		getSelfProfilePage().tapChatheadNotification();
	}
}
