package com.wearezeta.auto.ios.steps;

import org.junit.Assert;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.reporter.IOSLogListener;
import com.wearezeta.auto.ios.reporter.IOSPerfReportModel;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.performance.PerformanceHelpers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {

	private final IOSPagesCollection pagesCollection = IOSPagesCollection
			.getInstance();
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final int DEFAULT_SWIPE_TIME = 500;
	private static final int MAX_MSGS_IN_CONVO_WINDOW = 50;

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollection.getPage(DialogPage.class);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollection.getPage(ContactListPage.class);
	}

	/**
	 * Send multiple messages from one of my contacts using the backend
	 * 
	 * @step. ^I receive (\\d+) messages? from contact (.*)
	 * 
	 * @param msgsCount
	 *            count of messages to send. This should be greater or equal to
	 *            the maximum count of messages in convo window (which is
	 *            currently equal to 50 for iOS)
	 * @param asContact
	 *            from which contact should we send these messages
	 * @throws Exception
	 */
	@Given("^I receive (\\d+) messages? from contact (.*)")
	public void IReceiveXMessagesFromContact(int msgsCount, String asContact)
			throws Exception {
		assert msgsCount >= MAX_MSGS_IN_CONVO_WINDOW : String.format(
				"The count of messages to send (%d) should be greater or equal to the max "
						+ "count of messages in conversation window (%d)",
				msgsCount, MAX_MSGS_IN_CONVO_WINDOW);
		asContact = usrMgr.findUserByNameOrNameAlias(asContact).getName();
		perfCommon.sendMultipleMessagesIntoConversation(asContact, msgsCount);
	}

	private void waitUntilConversationsListIsFullyLoaded() throws Exception {
		final int maxTries = 3;
		final long millisecondsDelay = 20000;
		int ntry = 1;
		do {
			try {
				boolean isLoaded = getContactListPage()
						.waitForContactListToLoad();
				if (!isLoaded) {
					getDialogPage().navigateBack(500);
				}
				isLoaded = getContactListPage().waitForContactListToLoad();
				Assert.assertTrue("Contact list didn't load", isLoaded);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(millisecondsDelay);
			ntry++;
		} while (getContactListPage().GetVisibleContacts().size() != 0
				&& ntry <= maxTries);
		Assert.assertTrue(
				"No conversations are visible in the conversations list, but some are expected",
				getContactListPage().GetVisibleContacts().size() > 0);
	}

	private void visitConversationWhenAvailable(final String destConvoName)
			throws Exception {
		final int maxRetries = 15;
		int ntry = 1;
		do {
			try {
				getContactListPage().tapOnName(destConvoName);
			} catch (IllegalStateException | WebDriverException e) {
				if (ntry >= maxRetries) {
					throw e;
				} else {
					e.printStackTrace();
				}
			}
			Thread.sleep(3000);
			ntry++;
		} while (!getDialogPage().isCursorInputVisible() && ntry <= maxRetries);
		assert getDialogPage().isCursorInputVisible() : "The conversation has not been opened after "
				+ maxRetries + " retries";
		getDialogPage().tapOnCursorInput();
		getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
	}

	/**
	 * Starts standard actions loop (read messages/send messages) to measure
	 * application performance
	 * 
	 * @step. ^I start test cycle for (\\d+) minutes? with messages received
	 *        from (.*)
	 * 
	 * @param timeout
	 *            number of minutes to run the loop
	 * @param fromContact
	 *            contact name/alias, from which I received messages
	 * @throws Exception
	 */
	@When("^I start test cycle for (\\d+) minutes? with messages received from (.*)")
	public void WhenIStartTestCycleForNMinutes(int timeout, String fromContact)
			throws Exception {
		waitUntilConversationsListIsFullyLoaded();
		final String destConvoName = usrMgr.findUserByNameOrNameAlias(
				fromContact).getName();
		String firstConvoName = getContactListPage().getFirstDialogName();
		int ntry = 1;
		do {
			// This contact, which received messages, should be the first
			// contact in the visible convo list now
			if (destConvoName.equals(firstConvoName)) {
				break;
			} else {
				Thread.sleep(10000);
			}
			firstConvoName = getContactListPage().getFirstDialogName();
			ntry++;
		} while (ntry <= 3);
		assert destConvoName.equals(firstConvoName) : String
				.format("The very first conversation name '%s' is not the same as expected one ('%s')",
						firstConvoName, destConvoName);

		// Visit the conversation for the first time
		visitConversationWhenAvailable(destConvoName);

		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				visitConversationWhenAvailable(destConvoName);
			}
		}, timeout);
	}

	/**
	 * Generates iOS performance report
	 * 
	 * @step. ^I generate performance report for (\\d+) users?$
	 * 
	 * @throws Exception
	 */
	@Then("^I generate performance report for (\\d+) users?$")
	public void ThenIGeneratePerformanceReport(int usersCount) throws Exception {
		final IOSPerfReportModel dataModel = new IOSPerfReportModel();
		dataModel.setContactsCount(usersCount - 1);
		final String logOutput = IOSLogListener.getInstance().getStdOut();
		dataModel.loadDataFromLog(logOutput);
		PerformanceHelpers.storeWidgetDataAsJSON(dataModel,
				IOSCommonUtils.getPerfReportPathFromConfig(getClass()));
	}

	@Before("@performance")
	public void StartLogListener() {
		// for Jenkins slaves we should define that environment has display
		CommonUtils.defineNoHeadlessEnvironment();
		try {
			IOSLogListener.getInstance().start();
		} catch (Exception e) {
		}
	}

	@After("@performance")
	public void StopLogListener() throws Exception {
		IOSLogListener.forceStopAll();
		IOSLogListener.writeDeviceLogsToConsole(IOSLogListener.getInstance());
	}
}
