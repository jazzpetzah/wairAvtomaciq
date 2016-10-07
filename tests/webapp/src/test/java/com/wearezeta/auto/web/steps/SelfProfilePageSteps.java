package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.web.common.TestContext;
import com.wearezeta.auto.web.pages.SelfProfilePage;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SelfProfilePageSteps {

	private final TestContext context;
        
    public SelfProfilePageSteps() {
        this.context = new TestContext();
    }

    public SelfProfilePageSteps(TestContext context) {
        this.context = context;
    }


	/**
	 * Set accent color on self profile page
	 * 
	 * @step. ^I set my accent color to (\\w+)$
	 * 
	 * @param colorName
	 *            one of these colors: StrongBlue, StrongLimeGreen,
	 *            BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Then("^I set my accent color to (\\w+)$")
	public void ISetMyAccentColorTo(String colorName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).selectAccentColor(
				colorName);
	}

	/*
	 * Verify my accent color in color picker is equal to expected color
	 * 
	 * @step. ^I verify my accent color in color picker is set to (\\w+) color$
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */
	@Then("^I verify my accent color in color picker is set to (\\w+) color$")
	public void IVerifyMyAccentColor(String colorName) throws Exception {
		final int expectedColorId = AccentColor.getByName(colorName).getId();
		final int actualColorId = context.getPagesCollection().getPage(
				SelfProfilePage.class).getCurrentAccentColorId();
		Assert.assertTrue("my actual accent color is not set",
				actualColorId == expectedColorId);
	}

	/**
	 * Click camera button on Self Profile page
	 * 
	 * @step. ^I click camera button$
	 * 
	 * @throws Exception
	 */
	@And("^I click camera button$")
	public void IClickCameraButton() throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class)
				.clickCameraButton();
	}

	/*
	 * Verify my avatar background color is set to expected color
	 * 
	 * @step. ^I verify my avatar background color is set to (\\w+) color$
	 * 
	 * @param colorName one of these colors: StrongBlue, StrongLimeGreen,
	 * BrightYellow, VividRed, BrightOrange, SoftPink, Violet
	 * 
	 * @throws Exception
	 */

	@Then("^I verify my avatar background color is set to (\\w+) color$")
	public void IVerifyMyAvatarColor(String colorName) throws Exception {
		final AccentColor expectedColor = AccentColor.getByName(colorName);
		final AccentColor avatarColor = context.getPagesCollection().getPage(
				SelfProfilePage.class).getCurrentAvatarAccentColor();
		Assert.assertTrue("my avatar background accent color is not set",
				avatarColor == expectedColor);
	}

	/**
	 * Emulates drop of existing picture into self profile
	 * 
	 * @step. ^I drop picture (.*) to self profile$
	 * 
	 * @param pictureName
	 *            the name of existing image in ~/Documents folder
	 * @throws Exception
	 */
	@When("^I drop picture (.*) to self profile$")
	public void IDropPicture(String pictureName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).dropPicture(
				pictureName);
	}

	/**
	 * Emulates upload of existing picture into self profile
	 * 
	 * @step. ^I upload picture (.*) to self profile$
	 * 
	 * @param pictureName
	 *            the name of existing image in ~/Documents folder
	 * @throws Exception
	 */
	@When("^I upload picture (.*) to self profile$")
	public void IUploadPicture(String pictureName) throws Exception {
		context.getPagesCollection().getPage(SelfProfilePage.class).uploadPicture(pictureName);
	}

}
