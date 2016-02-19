package com.wearezeta.auto.web.steps;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.email.AccountDeletionMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.WireMessage;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import com.wearezeta.auto.web.pages.external.DeleteAccountPage;

import cucumber.api.java.en.Then;

public class DeleteAccountPageSteps {
	
	public static final Logger log = ZetaLogger.getLog(CommonWebAppSteps.class
			.getSimpleName());
	
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	
	private static final int DELETION_RECEIVING_TIMEOUT = 120;
	
	private String deleteLink = null;
	
	/**
	 * Verify that invitation email exists in user's mailbox
	 *
	 * @param alias user name/alias
	 * @throws Throwable
	 * @step. ^I verify user (.*) has received (?:an |\s*)email invitation$
	 */
	@Then("^I delete account of user (.*) via email on (.*)$")
	public void IDeleteAccountViaEmaiOn(String alias, String agent) throws Throwable {
		final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
		expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
		AccountDeletionMessage message = new AccountDeletionMessage(mbox.getMessage(expectedHeaders,
				DELETION_RECEIVING_TIMEOUT, 0).get());
		final String url = message.extractAccountDeletionLink() + "&agent=" + agent;
		log.info("URL: " + url);
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		deleteAccountPage.setUrl(url);
		deleteAccountPage.navigateTo();
		deleteAccountPage.clickDeleteAccountButton();
		assertTrue("Delete account page does not show success message", deleteAccountPage.isSuccess());
	}
	
	/**
	 * Navigates to the deletion utility page with changed checksums
	 *
	 * @param alias  user name/alias
	 *        agent  platform on which the link is opened (iphone/android/osx/windows)
	 *        part   checksum to change
	 * @throws Throwable
	 * @step. ^I navigate to delete account page of user (.*) on (.*) with changed (.*) checksum$
	 */
	@Then("^I navigate to delete account page of user (.*) on (.*) with changed (.*) checksum$")
	public void IDeleteAccountWithWrongChecksum(String alias, String agent, String part) throws Throwable {
		String key = "key=";
		String code = "code=";
		String newUrl = "";
		int position = 0;
		
		final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
		expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
		AccountDeletionMessage message = new AccountDeletionMessage(mbox.getMessage(expectedHeaders,
				DELETION_RECEIVING_TIMEOUT, 0).get());
		final String url = message.extractAccountDeletionLink() + "&agent=" + agent;
		
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		
		switch (part) {
			case "key":
				position = url.indexOf(key) + 4;
				newUrl = url.substring(0, position) + "_WRONG-KEY-CHECKSUM_" + url.substring(position, url.length());
				log.info("URL: " + newUrl);
				deleteAccountPage.setUrl(newUrl);
				deleteAccountPage.navigateTo();
				break;
			case "code":
				position = url.indexOf(code) + 5;
				newUrl = url.substring(0, position) + "_WRONG-CODE-CHECKSUM_" + url.substring(position, url.length());
				log.info("URL: " + newUrl);
				deleteAccountPage.setUrl(newUrl);
				deleteAccountPage.navigateTo();
				break;
			default: break;
		}
	}
	
	@Then("^I click delete account button$")
	public void IClickDeleteAccountButton() throws Exception{
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		deleteAccountPage.clickDeleteAccountButton();
	}
	
	@Then("^I see error message for wrong (.*) checksum$")
	public void ISeeErrorMessageForWrongChecksum(String checksum) throws Exception{
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		if (checksum == "key") {
			assertTrue("Delete account page shows success message", deleteAccountPage.isWrongKey());
		} else if (checksum == "code") {
			assertTrue("Delete account page shows success message", deleteAccountPage.isWrongCode());
		}
	}
		
	@Then("^I remember delete link of user (.*)$")
	public void IRememberDeleteLinkOfUser(String alias) throws Throwable {
		final String email = usrMgr.findUserByNameOrNameAlias(alias).getEmail();
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<>();
		expectedHeaders.put(MessagingUtils.DELIVERED_TO_HEADER, email);
		expectedHeaders.put(WireMessage.ZETA_PURPOSE_HEADER_NAME, AccountDeletionMessage.MESSAGE_PURPOSE);
		AccountDeletionMessage message = new AccountDeletionMessage(mbox.getMessage(expectedHeaders,
				DELETION_RECEIVING_TIMEOUT, 0).get());
		deleteLink = message.extractAccountDeletionLink();
	}
	
	/**
	 * Navigates to the deletion utility page witch removed checksums
	 *
	 * @param agent  platform on which the link is opened (iphone/android/osx/windows)
	 *        part   checksum to remove
	 * @throws Throwable
	 * @step. ^I use remembered link on (.*) without (.*) checksum$
	 */
	@Then("^I use remembered link on (.*) without (.*) checksum$")
	public void IUseRememberedLinkWithoutKeyChecksum(String agent, String deletion) throws Throwable{
		if (deleteLink == null) {
			throw new RuntimeException(
					"Invitation link has not been remembered before!");
		}
		
		String url = deleteLink + "&agent=" + agent;
		log.info("URL: " + url);
		int startKey = url.indexOf("key=");
		int startCode = url.indexOf("code=");
		int startAgent = url.indexOf("agent=");
		StringBuffer newUrlBuffer = new StringBuffer(url);
		
		switch (deletion) {
			case "key":
				newUrlBuffer.replace(startKey, startCode, "");
				break;
			case "code":
				newUrlBuffer.replace(startCode, startAgent, "");
				break;
			case "both":
				newUrlBuffer.replace(startKey, startAgent, "");
				break;
			default: break;
		}
		
		String newUrl = newUrlBuffer.toString();
		log.info("New URL: " + newUrl);
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		deleteAccountPage.setUrl(newUrl);
		deleteAccountPage.navigateTo();
	}
	
	@Then("^I see error message for missing checksum$")
	public void ISeeErrorMessageForMissingChecksum() throws Exception{
		DeleteAccountPage deleteAccountPage = WebappPagesCollection.getInstance()
				.getPage(DeleteAccountPage.class);
		assertFalse("Delete button is visible", deleteAccountPage.isButtonVisible());
		assertTrue("Delete account page shows success message", deleteAccountPage.isErrorMessage());
	}
}

