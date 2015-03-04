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

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private static final int PERF_MON_NOT_STARTED = -1;
	private static final int PERF_MON_INIT_DELAY = 2000; // milliseconds

	private int perfMonPid = PERF_MON_NOT_STARTED;

	public int getPerfMonPid() {
		return perfMonPid;
	}

	public void setPerfMonPid(int perfMonPid) {
		this.perfMonPid = perfMonPid;
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
					visibleContactsList.remove(0);

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
		final String strPid = CommonUtils
				.executeOsXCommandWithOutput(new String[] {
						"/bin/bash",
						"-c",
						"cd $HOME && instruments -v"
								+ " -t /Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate"
								+ " -w " + iPhoneUDID + " 1>&2 & echo $!" });
		if (strPid.length() > 0) {
			this.setPerfMonPid(Integer.parseInt(strPid));
			// This should be enough to initialize instruments and start
			// monitoring
			Thread.sleep(PERF_MON_INIT_DELAY);
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
		if (this.getPerfMonPid() == PERF_MON_NOT_STARTED) {
			throw new RuntimeException(
					"Please call the Start minitor step first");
		}

		CommonUtils
				.executeOsXCommandWithOutput(new String[] {
						"/bin/bash",
						"-c",
						String.format(
								"kill -SIGINT %d 1>&2"
										+ " && while [ -n \"`ps axu | grep \\\\b%d\\\\b | grep -v grep`\" ]; do sleep 1; done",
								this.getPerfMonPid(), this.getPerfMonPid()) });
	}
}
