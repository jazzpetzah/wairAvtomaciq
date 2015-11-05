package com.wearezeta.auto.android.steps;

import java.awt.image.BufferedImage;

import org.junit.Assert;

import com.wearezeta.auto.android.pages.PersonalInfoPage;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PersonalInfoPageSteps {
    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
            .getInstance();

    private PersonalInfoPage getPersonalInfoPage() throws Exception {
        return (PersonalInfoPage) pagesCollection
                .getPage(PersonalInfoPage.class);
    }

    /**
     * Taps on the options button
     *
     * @throws Exception
     * @step. ^I tap options button$
     */
    @When("^I tap options button$")
    public void WhenITapOptionsButton() throws Exception {
        getPersonalInfoPage().tapOptionsButton();
    }

    /**
     * Taps on the current users name
     *
     * @throws Exception
     * @step. ^I tap on my name$
     */
    @When("^I tap on my name$")
    public void WhenITapOnMyName() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        getPersonalInfoPage().tapOnMyName(self.getName());
    }

    /**
     * Taps on the settings button in the options menu
     *
     * @throws Exception
     * @step. ^I tap settings button$
     */
    @When("^I tap settings button$")
    public void WhenITapSettingsButton() throws Exception {
        getPersonalInfoPage().tapSettingsButton();
    }

    /**
     * Taps the about button in the options menu
     *
     * @throws Exception
     * @step. ^I swipe right to contact list$
     */
    @When("^I tap about button$")
    public void WhenITapAboutButton() throws Exception {
        getPersonalInfoPage().tapAboutButton();
    }

    /**
     * Taps on the centre of the screen to bring up the current user's photo in
     * colour
     *
     * @throws Exception
     * @step. ^I tap on personal info screen$
     */
    @When("^I tap on personal info screen$")
    public void WhenITapOnPersonalInfoScreen() throws Exception {
        getPersonalInfoPage().tapOnPage();
    }

    /**
     * Taps on the photo button to show the user posibilities to change their
     * profile picture
     *
     * @throws Exception
     * @step. ^I tap change photo button$
     */
    @When("^I tap change photo button$")
    public void WhenITapChangePhotoButton() throws Exception {
        getPersonalInfoPage().tapChangePhotoButton();
    }

    /**
     * Takes photo for new avatar. Front camera is opened by default
     *
     * @throws Exception
     * @step. ^I take new avatar picture$
     */
    @When("^I take new avatar picture$")
    public void ITakePhoto() throws Exception {
        getPersonalInfoPage().tapTakePhotoButton();
    }

    /**
     * Presses on the gallery button to select a photo from the phone's storage
     *
     * @throws Exception
     * @step. ^I press Gallery button$
     */
    @When("^I press Gallery button$")
    public void WhenIPressGalleryButton() throws Exception {
        getPersonalInfoPage().tapGalleryButton();
    }

    /**
     * Confirms the selected picture
     *
     * @throws Exception
     * @step. ^I press Confirm button$
     */
    @When("^I press Confirm button$")
    public void WhenIPressConfirmButton() throws Exception {
        getPersonalInfoPage().tapConfirmButton();
    }

    /**
     * Changes the current user's name to a new one
     *
     * @param newName the new name for the current user
     * @throws Exception
     * @step. ^I change my name to (.*)$
     */
    @When("^I change my name to (.*)$")
    public void IChangeNameTo(String newName) throws Exception {
        getPersonalInfoPage().changeSelfNameTo(newName);
        usrMgr.getSelfUser().setName(newName);
    }

    /**
     * Swipe right to contact list
     *
     * @throws Exception
     * @step. ^I swipe right to contact list$
     */
    @When("^I close Personal Info Page$")
    public void IClosePersonalInfoPage() throws Exception {
        getPersonalInfoPage().pressCloseButton();
    }

    /**
     * Confirms that the current user's name is as given, and then changes it
     * back to the old one (Why not reuse the old step IChangeNameTo(String,
     * String))
     *
     * @param name The current (newly given) name of the current user
     * @throws Exception
     * @step. ^I see my new name (.*)$
     */
    @Then("^I see my new name (.*)$")
    public void ISeeMyNewName(String name) throws Exception {
        Assert.assertTrue(
                String.format("The new name '%s' is not visible", name),
                getPersonalInfoPage().waitUntilNameIsVisible(name));
    }

    /**
     * Checks to see that the personal info page is visible
     *
     * @throws Exception
     * @step. ^I see personal info page$
     */
    @Then("^I see personal info page$")
    public void ISeePersonalInfoPage() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        Assert.assertTrue("Personal info page is not visible",
                getPersonalInfoPage().waitUntilNameIsVisible(self.getName()));
    }

    /**
     * Verify that Settings page is visible
     *
     * @throws Exception
     * @step. ^I see Settings$
     */
    @Then("^I see Settings$")
    public void ThenISeeSettings() throws Exception {
        Assert.assertTrue("Settings are not visible", getPersonalInfoPage()
                .isSettingsVisible());
    }

    /**
     * Checks that self name edit field containing user self name is visible
     *
     * @throws Exception
     * @step. ^I see edit name field with my name$
     */
    @Then("^I see edit name field with my name$")
    public void ThenISeeEditNameField() throws Exception {
        final ClientUser self = usrMgr.getSelfUserOrThrowError();
        Assert.assertTrue("Name edit field is not visible",
                getPersonalInfoPage()
                        .waitUntilNameEditIsVisible(self.getName()));
    }

    /**
     * Clear self name edit field
     *
     * @throws Exception
     * @step. ^I clear name field$
     */
    @When("^I clear name field$")
    public void IClearNameField() throws Exception {
        getPersonalInfoPage().clearSelfName();
    }

    private BufferedImage previousProfilePicture = null;

    /**
     * Takes the screenshot of current screen and saves it to the internal
     * variable
     *
     * @throws Exception
     * @step. ^I remember my current profile picture$
     */
    @When("^I remember my current profile picture$")
    public void IRememberCurrentPicture() throws Exception {
        previousProfilePicture = getPersonalInfoPage().takeScreenshot()
                .orElseThrow(AssertionError::new);
    }

    private static final int PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS = 60;
    private static final double MAX_OVERLAP_SCORE = 0.65;

    /**
     * Verify that profile picture is different from the previously snapshotted
     * one within the predefined timeout (use special step to create a snapshot)
     *
     * @throws Exception
     * @step. ^I verify that my current profile picture is different from the
     * previous one$
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
            final BufferedImage currentProfilePicture = getPersonalInfoPage()
                    .takeScreenshot().orElseThrow(AssertionError::new);
            score = ImageUtil.getOverlapScore(currentProfilePicture,
                    previousProfilePicture, ImageUtil.RESIZE_NORESIZE);
            if (score <= MAX_OVERLAP_SCORE) {
                break;
            }
            Thread.sleep(3000);
        } while (score > MAX_OVERLAP_SCORE
                && System.currentTimeMillis() - millisecondsStarted <= PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS * 1000);
        Assert.assertTrue(
                String.format(
                        "Profile picture has not been updated properly after %s seconds timeout (current overlap score value is %2.2f)",
                        PROFILE_IMAGE_CHANGE_TIMEOUT_SECONDS, score),
                score <= MAX_OVERLAP_SCORE);
    }

    /**
     * Tap the Light Bulb button on self profile page to change the current UI theme
     *
     * @throws Exception
     * @step. ^I tap Light Bulb button$
     */
    @And("^I tap Light Bulb button$")
    public void ITapLightBulbButton() throws Exception {
        getPersonalInfoPage().tapLightBulbButton();
    }
}