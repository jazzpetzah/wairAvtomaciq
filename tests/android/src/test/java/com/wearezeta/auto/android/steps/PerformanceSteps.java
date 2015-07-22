package com.wearezeta.auto.android.steps;

import java.util.List;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.AndroidLoggingUtils;
import com.wearezeta.auto.android.common.reporter.AndroidPerfReportModel;
import com.wearezeta.auto.android.common.reporter.AndroidPerformanceHelpers;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	private static final int DEFAULT_SWIPE_TIME = 500;
	private static final int MAX_MSGS_IN_CONVO_WINDOW = 100;

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollection.getPage(ContactListPage.class);
	}

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollection.getPage(DialogPage.class);
	}

	/**
	 * Send multiple messages from one of my contacts using the backend
	 * 
	 * @step. ^I receive (\\d+) messages? from contact (.*)
	 * 
	 * @param msgsCount
	 *            count of messages to send
	 * @param asContact
	 *            from which contact should we send these messages
	 * @throws Exception
	 */
	@Given("^I receive (\\d+) messages? from contact (.*)")
	public void IReceiveXMessagesFromContact(int msgsCount, String asContact)
			throws Exception {
		assert msgsCount >= MAX_MSGS_IN_CONVO_WINDOW * 2 : String
				.format("The count of messages to send (%d) should be greater or equal to the max "
						+ "count of messages in conversation window multiplied by 2 (%d)",
						msgsCount, MAX_MSGS_IN_CONVO_WINDOW * 2);
		asContact = usrMgr.findUserByNameOrNameAlias(asContact).getName();
		perfCommon.sendMultipleMessagesIntoConversation(asContact,
				msgsCount * 2);
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
		final String destConvoName = usrMgr.findUserByNameOrNameAlias(
				fromContact).getName();
		String firstConvoName = getContactListPage()
				.getFirstVisibleConversationName();
		int ntry = 1;
		do {
			// This contact, which received messages, should be the first
			// contact in the visible convo list now
			if (destConvoName.equals(firstConvoName)) {
				break;
			} else {
				Thread.sleep(10000);
			}
			firstConvoName = getContactListPage()
					.getFirstVisibleConversationName();
			ntry++;
		} while (ntry <= 3);
		assert destConvoName.equals(firstConvoName) : String
				.format("The very first conversation name '%s' is not the same as expected one ('%s')",
						firstConvoName, destConvoName);
		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				final int maxRetries = 10;
				int ntry = 1;
				do {
					getContactListPage().tapOnName(destConvoName);
					Thread.sleep(3000);
					ntry++;
				} while (!getDialogPage().isDialogVisible()
						&& ntry <= maxRetries);
				assert getDialogPage().isDialogVisible() : "The conversation has not been opened after "
						+ maxRetries + " retries";
				getDialogPage().tapDialogPageBottom();
				getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
			}
		}, timeout);
	}

	/**
	 * Generates android performance report and saves it by path provided in the
	 * configuration file, option "perfReportPath". The previous report is going
	 * to be silently deleted if it already exists in this folder
	 * 
	 * @step. ^I generate performance report for (\\d+) users?$
	 * 
	 * @param usersCount
	 *            count of users in the test (self user + all contacts)
	 * 
	 * @throws Exception
	 */
	@Then("^I generate performance report for (\\d+) users?$")
	public void ThenIGeneratePerformanceReport(int usersCount) throws Exception {
		final List<String> logOutput = AndroidLoggingUtils
				.getLogLinesForCurrentTestCase(CommonAndroidSteps
						.getTestStartedTimestamp());
		final AndroidPerfReportModel dataModel = new AndroidPerfReportModel();
		dataModel.setContactsCount(usersCount - 1);
		dataModel.loadDataFromLogCat(String.join("\n", logOutput));
		AndroidPerformanceHelpers.storeWidgetDataAsJSON(
				AndroidCommonUtils.getGeckoboardWidgetIdFromConfig(getClass()),
				dataModel,
				AndroidCommonUtils.getPerfReportPathFromConfig(getClass()));
	}
}
