package com.wearezeta.auto.web.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.backend.AccentColor;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.usrmgmt.NoSuchUserException;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager.FindBy;
import com.wearezeta.auto.web.pages.PagesCollection;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;

public class SelfProfilePageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	public SelfProfilePageSteps() {
	}

	/**
	 * Clicks the gear button on Self Profile page
	 * 
	 * @step. ^I click gear button on self profile page$
	 * @throws Exception
	 */
	@And("^I click gear button on self profile page$")
	public void IClickGearButton() throws Exception {
		PagesCollection.selfProfilePage.clickGearButton();
	}

	/**
	 * Clicks the corresponding item from "gear" menu
	 * 
	 * @step. ^I select (.*) menu item on self profile page$
	 * 
	 * @param name
	 *            the name of menu item
	 * @throws Exception
	 */
	@And("^I select (.*) menu item on self profile page$")
	public void ISelectGearMenuItem(String name) throws Exception {
		PagesCollection.selfProfilePage.selectGearMenuItem(name);
	}

	/**
	 * Verifies that correct user name is shown on self profile page
	 * 
	 * @step. ^I see user name on self profile page (.*)$
	 * 
	 * @param name
	 *            name of the user
	 * @throws Exception
	 */
	@And("^I see user name on self profile page (.*)$")
	public void ISeeUserNameOnSelfProfilePage(String name) throws Exception {
		name = usrMgr.replaceAliasesOccurences(name, FindBy.NAME_ALIAS);
		boolean nameCorrect = PagesCollection.selfProfilePage
				.checkNameInSelfProfile(name);
		Assert.assertTrue(nameCorrect);
	}

	/**
	 * Verifies that correct user email is shown on self profile page
	 * 
	 * @step. ^I see user email on self profile page (.*)$
	 * 
	 * @param email
	 *            email of the user
	 * 
	 * @throws NoSuchUserException
	 */
	@And("^I see user email on self profile page (.*)$")
	public void ISeeUserEmailOnSelfProfilePage(String email)
			throws NoSuchUserException {
		try {
			email = usrMgr.findUserByEmailOrEmailAlias(email).getEmail();
		} catch (NoSuchUserException e) {

		}

		String actualEmail = PagesCollection.selfProfilePage.getUserMail();
		Assert.assertEquals(email, actualEmail);
	}

	/**
	 * Set new username on self profile page
	 * 
	 * @step. ^I change username to (.*)
	 * 
	 * @param name
	 *            new username string
	 */
	@And("^I change username to (.*)")
	public void IChangeUserNameTo(String name) {
		PagesCollection.selfProfilePage.setUserName(name);
		usrMgr.getSelfUser().setName(name);
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
		PagesCollection.selfProfilePage.selectAccentColor(colorName);
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
	public void IVerifyMyAccentColor(String colorName) {
		final int expectedColorId = AccentColor.getByName(colorName).getId();
		final int actualColorId = PagesCollection.selfProfilePage
				.getCurrentAccentColorId();
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
		PagesCollection.profilePicturePage = PagesCollection.selfProfilePage
				.clickCameraButton();
	}
}
