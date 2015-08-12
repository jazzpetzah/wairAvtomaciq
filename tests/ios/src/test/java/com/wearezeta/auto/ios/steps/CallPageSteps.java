package com.wearezeta.auto.ios.steps;

import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.IncomingCallPage;
import com.wearezeta.auto.ios.pages.StartedCallPage;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CallPageSteps {

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private StartedCallPage getStartedCallPage() throws Exception {
		return (StartedCallPage) pagesCollecton.getPage(StartedCallPage.class);
	}

	private IncomingCallPage getIncomingCallPage() throws Exception {
		return (IncomingCallPage) pagesCollecton
				.getPage(IncomingCallPage.class);
	}

	/**
	 * Verify that calling UI is visible
	 * 
	 * @step. ^I see calling to contact (.*) message$
	 * @param contact
	 *            User name whom we call
	 * @throws Exception
	 */
	@When("^I see calling to contact (.*) message$")
	public void ISeeCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(getStartedCallPage().isCallingMessageVisible());
	}

	/**
	 * 
	 * Verify that calling UI buttons are visible
	 * 
	 * @step. ^I see mute call, end call and speakers buttons$
	 * @throws Exception
	 */
	@When("^I see mute call, end call and speakers buttons$")
	public void ISeeCallingPageButtons() throws Exception {
		Assert.assertTrue("End call button is not visible",
				getStartedCallPage().isEndCallVisible());
		Assert.assertTrue("Mute call button is not visible",
				getStartedCallPage().isMuteCallVisible());
		// Assert.assertTrue("Speakers button is not visible",
		// getStartedCallPage().isSpeakersVisible());
	}

	/**
	 * 
	 * Verify that calling UI buttons are visible (using it for iPad
	 * verification step as far speakers button is not shown there)
	 * 
	 * @step. ^I see mute call, end call buttons$
	 * @throws Exception
	 */
	@When("^I see mute call, end call buttons$")
	public void ISeeCallingPageButtonsOnIpad() throws Exception {
		Assert.assertTrue("End call button is not visible",
				(getStartedCallPage().isEndCallVisible()));
		Assert.assertTrue("Mute call button is not visible",
				(getStartedCallPage().isMuteCallVisible()));
	}

	/**
	 * Click on end call button
	 * 
	 * @step. ^I end started call$
	 * @throws Exception
	 */
	@When("^I end started call$")
	public void IEndStartedCall() throws Exception {
		getStartedCallPage().clickEndCallButton();
	}

	/**
	 * Verify that calling page is not visible
	 * 
	 * @step. ^I dont see calling page$
	 * @throws Exception
	 */
	@When("^I dont see calling page$")
	public void IDontSeeCallPage() throws Exception {
		Assert.assertFalse("Calling bar is visible", getStartedCallPage()
				.isCallingMessageVisible());
	}

	/**
	 * Verify that incoming call page is not visible
	 * 
	 * @step. ^I dont see incoming call page$
	 * @throws Exception
	 */
	@When("^I dont see incoming call page$")
	public void IDontSeeIncomingCallPage() throws Exception {
		Assert.assertFalse(getIncomingCallPage().isCallingMessageVisible());
	}

	/**
	 * Verify that incoming calling UI is visible
	 * 
	 * @step. ^I see incoming calling message for contact (.*)$
	 * @param contact
	 *            User name who calls
	 * @throws Exception
	 */
	@When("^I see incoming calling message for contact (.*)$")
	public void ISeeIncomingCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		// Assert.assertTrue(getIncomingCallPage().isUserCallingMessageShown(
		// contact.toUpperCase()));
		Assert.assertTrue(getIncomingCallPage().isCallingMessageVisible());
	}

	/**
	 * Verify that incoming calling from User is NOT visible
	 * 
	 * @step. ^I dont see incoming calling message from contact (.*)$
	 * @param contact
	 *            User name who calls
	 * @throws Exception
	 */
	@When("^I dont see incoming calling message from contact (.*)$")
	public void IDontSeeIncomingCallingMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertFalse(
				"User " + contact + " calling UI is shown",
				getIncomingCallPage().isUserCallingMessageShown(
						contact.toUpperCase()));
	}

	/**
	 * Click on ignore call button
	 * 
	 * @step. ^I ignore incoming call$
	 * @throws Exception
	 */
	@When("^I ignore incoming call$")
	public void IignoreIncomingCall() throws Exception {
		getIncomingCallPage().ignoreIncomingCallClick();
	}

	/**
	 * Accept incoming call by clicking accept button
	 * 
	 * @step. ^I accept incoming call$
	 * @throws Exception
	 */
	@When("^I accept incoming call$")
	public void IAcceptIncomingCall() throws Exception {
		getIncomingCallPage().acceptIncomingCallClick();
	}

	/**
	 * Verify that started call message is visible
	 * 
	 * @step. ^I see started call message for contact (.*)$
	 * @param contact
	 *            contact name with whom you have a call
	 * @throws Exception
	 */
	@When("^I see started call message for contact (.*)$")
	public void ISeeStartedCallMesage(String contact) throws Exception {
		contact = usrMgr.findUserByNameOrNameAlias(contact).getName();
		Assert.assertTrue(getStartedCallPage().isStartedCallMessageVisible(
				contact.toUpperCase()));
	}

	/**
	 * Verify is mute call button icon selected or not
	 * 
	 * @step. ^I see mute call button on calling bar is selected$
	 * 
	 * @throws Exception
	 */
	@When("^I see mute call button on calling bar is selected$")
	public void ISeeMuteCallButtonOnCallingBarIsSelected() throws Exception {
		Assert.assertTrue("Mute call button is not selected",
				getStartedCallPage().isMuteCallButtonSelected());
	}

	/**
	 * Verify that incoming group calling UI is visible
	 * 
	 * @step. ^I see incoming group calling message$
	 * @throws Exception
	 */
	@When("^I see incoming group calling message$")
	public void ISeeIncomingGroupCallingMessage() throws Exception {
		Assert.assertTrue(getIncomingCallPage().isGroupCallingMessageVisible());
	}

	/**
	 * Verifies the visibility of the Join Call bar
	 * 
	 * @step. ^I see Join Call bar$
	 * @throws Throwable
	 */
	@Then("^I see Join Call bar$")
	public void ISeeJoinCallBar() throws Throwable {
		boolean joinCallBarIsVisible = getIncomingCallPage()
				.isJoinCallBarVisible();
		Assert.assertTrue("Join Call bar is not visible", joinCallBarIsVisible);
	}

	/**
	 * Verifies that a second call is coming in alert is shown
	 * 
	 * @step. ^I see Accept second call alert$
	 * @throws Throwable
	 */
	@When("^I see Accept second call alert$")
	public void ISeeAcceptSecondCallAlert() throws Throwable {
		boolean secondCallAlertIsVisible = getIncomingCallPage()
				.isSecondCallAlertVisible();
		Assert.assertTrue("Second call Alert is not shown",
				secondCallAlertIsVisible);
	}

	/**
	 * Presses the end call button on the second incoming call alert
	 * 
	 * @step. ^I press End Call button on alert$
	 * @throws Throwable
	 */
	@When("^I press End Call button on alert$")
	public void IPressEndCallButtonOnAlert() throws Throwable {
		getIncomingCallPage().pressEndCallAlertButton();
	}

	/**
	 * Verifies a number of avatars in the group call bar
	 * 
	 * @step. ^I see (\\d+) avatars in the group call bar$
	 * @param numberOfAvatars
	 *            number of avatars in group call bar
	 * @throws Throwable
	 */
	@Then("^I see (\\d+) avatars in the group call bar$")
	public void ISeeAvatarsInTheGroupCallBar(int numberOfAvatars)
			throws Throwable {
		int actualNumberOfAvatars = getIncomingCallPage()
				.isNumberOfGroupCallAvatarCorrect();
		Assert.assertEquals(numberOfAvatars, actualNumberOfAvatars);
	}

}
