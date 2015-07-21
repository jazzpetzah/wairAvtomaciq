package com.wearezeta.auto.android.steps;

import java.util.List;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.reporter.AndroidPerfReportModel;
import com.wearezeta.auto.android.common.reporter.AndroidPerformanceHelpers;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

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
	 * Starts standard actions loop (read messages/send messages) to measure
	 * application performance
	 * 
	 * @step. ^I start testing cycle for (\\d+) minutes$
	 * 
	 * @param timeout
	 *            number of minutes to run the loop
	 * @throws Exception
	 */
	@When("^I start test cycle for (\\d+) minutes$")
	public void WhenIStartTestCycleForNMinutes(int timeout) throws Exception {
		final List<ClientUser> allUsers = usrMgr.getCreatedUsers();
		// Send messages to the last contact
		final String destConvoName = allUsers.get(allUsers.size() - 1)
				.getName();
		// Send twice more messages that can fit into single SyncEngine window
		perfCommon.sendMultipleMessagesIntoConversation(destConvoName,
				MAX_MSGS_IN_CONVO_WINDOW * 2);
		getContactListPage().waitForConversationListLoad();
		final String firstConvoName = getContactListPage()
				.getFirstVisibleConversationName();
		// This contact, which received messages, should be the first contact in
		// the visible convo list now
		assert destConvoName.equals(firstConvoName) : String
				.format("The very first conversation name '%s' is not the same as expected one ('%s')",
						firstConvoName, destConvoName);
		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				getContactListPage().tapOnName(firstConvoName);
				assert getDialogPage().isDialogVisible();
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
		CommonAndroidSteps.getLogcatListener().stop(2,
				new int[] { CommonAndroidSteps.getLogcatListener().getPid() },
				5000);
		final AndroidPerfReportModel dataModel = new AndroidPerfReportModel();
		dataModel.loadDataFromLogCat(CommonAndroidSteps.getLogcatListener()
				.getStdout()
				+ "\n"
				+ CommonAndroidSteps.getLogcatListener().getStderr());
		AndroidPerformanceHelpers.storeWidgetDataAsJSON(
				AndroidCommonUtils.getGeckoboardWidgetIdFromConfig(getClass()),
				dataModel,
				AndroidCommonUtils.getPerfReportPathFromConfig(getClass()));
	}
}
