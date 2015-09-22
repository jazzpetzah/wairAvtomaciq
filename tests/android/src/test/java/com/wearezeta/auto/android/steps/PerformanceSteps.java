package com.wearezeta.auto.android.steps;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriverException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.AndroidLogListener;
import com.wearezeta.auto.android.common.AndroidLogListener.ListenerType;
import com.wearezeta.auto.android.common.reporter.AndroidBatteryPerfReportModel;
import com.wearezeta.auto.android.common.reporter.AndroidPerfReportModel;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.CommonCallingSteps2;
import com.wearezeta.auto.common.calling2.v1.model.Flow;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.performance.PerformanceHelpers;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	private final CommonCallingSteps2 commonCallingSteps = CommonCallingSteps2
			.getInstance();

	private static final Logger log = ZetaLogger.getLog(PerformanceSteps.class
			.getSimpleName());

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
	 *            count of messages to send. This should be greater or equal to
	 *            the maximum count of messages in convo window (which is
	 *            currently equal to 100)
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
				getContactListPage().verifyContactListIsFullyLoaded();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Thread.sleep(millisecondsDelay);
			ntry++;
		} while (!getContactListPage().isAnyConversationVisible()
				&& ntry <= maxTries);
		Assert.assertTrue(
				"No conversations are visible in the conversations list, but some are expected",
				getContactListPage().isAnyConversationVisible());
	}

	private void visitConversationWhenAvailable(final String destConvoName)
			throws Exception {
		final int maxRetries = 15;
		int ntry = 1;
		do {
			try {
				getContactListPage().tapOnName(destConvoName, 0);
			} catch (IllegalStateException | WebDriverException e) {
				if (ntry >= maxRetries) {
					throw e;
				} else {
					e.printStackTrace();
				}
			}
			Thread.sleep(3000);
			ntry++;
		} while (!getDialogPage().isDialogVisible() && ntry <= maxRetries);
		assert getDialogPage().isDialogVisible() : "The conversation has not been opened after "
				+ maxRetries + " retries";
		getDialogPage().tapDialogPageBottom();
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

		// Visit the conversation for the first time
		visitConversationWhenAvailable(destConvoName);

		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				visitConversationWhenAvailable(destConvoName);
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
		final AndroidPerfReportModel dataModel = new AndroidPerfReportModel();
		dataModel.setContactsCount(usersCount - 1);
		final String logOutput = AndroidLogListener.getInstance(
				ListenerType.PERF).getStdOut();
		dataModel.loadDataFromLogCat(logOutput);
		PerformanceHelpers.storeWidgetDataAsJSON(dataModel,
				AndroidCommonUtils.getPerfReportPathFromConfig(getClass()));
	}

	private AndroidBatteryPerfReportModel batteryPerfReport = null;

	/**
	 * Initialize battery perf report by recording current device metrics for
	 * the future comparison
	 * 
	 * @step. ^I initialize battery performance report$
	 * 
	 * @throws Exception
	 */
	@When("^I initialize battery performance report$")
	public void IInitializeBatteryPerfReport() throws Exception {
		batteryPerfReport = new AndroidBatteryPerfReportModel();
		batteryPerfReport.setPreviousCapacityValue(AndroidCommonUtils
				.getBatteryCapacity());
		batteryPerfReport.setPreviousRxBytes(AndroidCommonUtils.getRxBytes());
		batteryPerfReport.setPreviousTxBytes(AndroidCommonUtils.getTxBytes());
	}

	private final static long CALL_STATUS_CHECKING_INTERVAL = 30000; // milliseconds

	/**
	 * Check whether a call is still in progress
	 * 
	 * @step. ^I verify the call from (.*) is in progress for (\\d+) minutes?$
	 * 
	 * @param caller
	 *            caller name/alias
	 * @param durationMinutes
	 *            for how long we have to check that the call is in progress
	 * @throws Exception
	 */
	@And("^I verify the call from (.*) is in progress for (\\d+) minutes?$")
	public void IStartBatteryPerfTest(String caller, int durationMinutes)
			throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		while (System.currentTimeMillis() - millisecondsStarted <= durationMinutes * 1000 * 60) {
			Thread.sleep(CALL_STATUS_CHECKING_INTERVAL);
			final long secondsElapsed = (System.currentTimeMillis() - millisecondsStarted) / 1000;
			final long secondsRemaining = durationMinutes * 60 - secondsElapsed;
			final List<Flow> flows = commonCallingSteps.getFlows(caller);
			if (flows.size() == 0) {
				throw new IllegalStateException(
						String.format(
								"User '%s' has no active flows, "
										+ "which means that the call was unexpectedly terminated after %d seconds",
								caller, secondsElapsed));
			}
			for (Flow flow : flows) {
				final long rxBytes = flow.getBytesIn();
				Assert.assertTrue(
						"Received bytes count should be greater than 0",
						rxBytes > 0);
				final long txBytes = flow.getBytesOut();
				Assert.assertTrue("Sent bytes count should be greater than 0",
						txBytes > 0);
			}
			log.info(String
					.format("Successfully verified the ongoing call after %d seconds. %d seconds left till the end of the perf test...",
							secondsElapsed, secondsRemaining));
		}
	}

	/**
	 * Generate battery performance report based on collected data
	 * 
	 * @step. ^I generate battery performance report for (\\d+) minutes?$
	 * 
	 * @throws Exception
	 */
	@Then("^I generate battery performance report for (\\d+) minutes?$")
	public void IGerenearetBatteryPerfReport(int durationMinutes) throws Exception {
		if (this.batteryPerfReport == null) {
			throw new IllegalStateException(
					"You have to initialize the report first");
		}
		batteryPerfReport.setCurrentCapacityValue(AndroidCommonUtils
				.getBatteryCapacity());
		batteryPerfReport.setCurrentRxBytes(AndroidCommonUtils.getRxBytes());
		batteryPerfReport.setCurrentTxBytes(AndroidCommonUtils.getTxBytes());
		batteryPerfReport.setMinutesDuration(durationMinutes);
		PerformanceHelpers.storeWidgetDataAsJSON(batteryPerfReport,
				AndroidCommonUtils.getPerfReportPathFromConfig(getClass()));
	}

}
