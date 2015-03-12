package com.wearezeta.auto.ios;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.common.process.AsyncProcess;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private static final int PERF_MON_INIT_DELAY = 5000; // milliseconds
	private static final int PERF_MON_STOP_TIMEOUT = 60 * 1000; // milliseconds
	private static final String ACTIVITY_MONITOR_TEMPLATE_PATH = "/Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate";

	private AsyncProcess perfMon = null;

	public AsyncProcess getPerfMon() {
		return perfMon;
	}

	public void setPerfMon(AsyncProcess perfMon) {
		this.perfMon = perfMon;
	}

	/**
	 * Starts standard actions loop (read messages/send messages) to measure
	 * application performance
	 * 
	 * @step. ^I start test cycle for (\\d+) minutes$
	 * 
	 * @param timeout
	 *            number of minutes to run the loop
	 * @throws Exception
	 */
	@When("^I start test cycle for (\\d+) minutes$")
	public void WhenIStartTestCycleForNMinutes(int timeout) throws Exception {
		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send messages in response to a random visible chat
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					// Get list of visible dialogs, remove self user name from
					// this list
					Assert.assertTrue("Contact list didn't load",
							PagesCollection.contactListPage
									.waitForContactListToLoad());
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							PagesCollection.contactListPage
									.GetVisibleContacts());

					int randomChatIdx = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
							.tapOnContactByIndex(visibleContactsList,
									randomChatIdx);

					// Emulating reading of previously received messages
					// This is broken in iOS simulator, known Apple bug
					// Using "external" swipe is unstable for a long perf test
					// PagesCollection.dialogPage.swipeDown(500);

					// Writing response
					PagesCollection.dialogPage.tapOnCursorInput();
					PagesCollection.dialogPage
							.sendMessageUsingScript(CommonUtils.generateGUID());

					// Swipe back to the convo list
					PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
							.swipeRight(
									500,
									DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
									30);
				}
			}
		}, timeout);
	}

	/**
	 * Start performance monitor tool for the real connected iPhone. This will
	 * throw the RuntimeException if there is no connected iPhone or instruments
	 * failed to start. All the collected perf logs will be saved in $HOME
	 * folder
	 * 
	 * @step. ^I start performance monitoring for the connected iPhone$
	 * 
	 * @throws Exception
	 */
	@When("^I start performance monitoring for the connected iPhone$")
	public void IStartPerfMon() throws Exception {
		final String iPhoneUDID = IOSCommonUtils.getConnectediPhoneUDID(true);
		final String[] cmd = {
				"/bin/bash",
				"-c",
				String.format("cd $HOME && instruments -v -t %s -w %s",
						ACTIVITY_MONITOR_TEMPLATE_PATH, iPhoneUDID) };
		final AsyncProcess ap = new AsyncProcess(cmd, true, true);
		ap.start();
		Thread.sleep(PERF_MON_INIT_DELAY);
		if (ap.isRunning()) {
			setPerfMon(ap);
		} else {
			throw new RuntimeException(
					"There are failures while starting perf monitor. Please check the log for more details.");
		}
	}

	/**
	 * Stop performance monitor tool for the real connected iPhone and flush
	 * performance logs. This will throw RuntimeException if 'I start
	 * performance monitoring for the connected iPhone' step has not been
	 * invoked before
	 * 
	 * @step. ^I finish performance monitoring for the connected iPhone$
	 * 
	 * @throws Exception
	 */
	@Then("^I finish performance monitoring for the connected iPhone$")
	public void IStopPerfMon() throws Exception {
		if (this.getPerfMon() == null) {
			throw new RuntimeException(
					"Please call the Start monitor step first");
		}
		if (!this.getPerfMon().isRunning()) {
			throw new RuntimeException(
					"Performance monitor has been unexpectedly killed before. Please check execution logs to get more details.");
		}
		// the real process id will be this parentPid + 1, because bash forks
		// a separate process from the command line that we provide
		final int monitorPid = this.getPerfMon().getPid() + 1;
		// Sending SIGINT to properly terminate perf monitor
		this.getPerfMon().stop("2", new int[] { monitorPid },
				PERF_MON_STOP_TIMEOUT);
	}
}
