package com.wearezeta.auto.ios.steps;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.reporter.IDeviceSysLogListener;
import com.wearezeta.auto.ios.reporter.IOSPerformanceReportGenerator;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {
	private static final Logger log = ZetaLogger.getLog(PerformanceSteps.class
			.getSimpleName());

	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	
	private static final int PERF_MON_INIT_DELAY = 5000; // milliseconds
	private static final int PERF_MON_STOP_TIMEOUT = 60 * 1000; // milliseconds
	private static final String ACTIVITY_MONITOR_TEMPLATE_PATH = "/Applications/Xcode.app/Contents/Applications/Instruments.app/Contents/Resources/templates/Activity\\ Monitor.tracetemplate";
	private static final String WIRE_ACTIVITY_MONITOR_TEMPLATE_PATH = "/Project/WireActivityMonitor.tracetemplate";

	public static IDeviceSysLogListener listener = new IDeviceSysLogListener();

	private AsyncProcess perfMon = null;

	public AsyncProcess getPerfMon() {
		return perfMon;
	}

	public void setPerfMon(AsyncProcess perfMon) {
		this.perfMon = perfMon;
	}

	private final IOSPagesCollection pagesCollecton = IOSPagesCollection
			.getInstance();

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollecton.getPage(DialogPage.class);
	}

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollecton.getPage(ContactListPage.class);
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
					boolean isLoaded = getContactListPage()
							.waitForContactListToLoad();
					if (!isLoaded) {
						getDialogPage()
								.swipeRight(
										500,
										DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
										30);
					}
					isLoaded = getContactListPage().waitForContactListToLoad();
					Assert.assertTrue("Contact list didn't load", isLoaded);
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							getContactListPage().GetVisibleContacts());

					final int MAX_ENTRIES_ON_SCREEN = 8;
					int randomRange = (visibleContactsList.size() > MAX_ENTRIES_ON_SCREEN) ? MAX_ENTRIES_ON_SCREEN
							: (visibleContactsList.size() - 1);
					int randomChatIdx;
					String convName;
					do {
						randomChatIdx = perfCommon.random.nextInt(randomRange);
						convName = visibleContactsList.get(randomChatIdx)
								.getText();
					} while (convName.contains("tst"));
					getContactListPage().tapOnContactByIndex(
							visibleContactsList, randomChatIdx);

					// Emulating reading of previously received messages
					// This is broken in iOS simulator, known Apple bug
					// Using "external" swipe is unstable for a long perf test
					// PagesCollection.dialogPage.swipeDown(500);

					// Writing response
					getDialogPage().tapOnCursorInput();
					getDialogPage().sendMessageUsingScript(
							CommonUtils.generateGUID());

					// Swipe back to the convo list
					getDialogPage().swipeRight(500,
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
		String templatePath = (new File(WIRE_ACTIVITY_MONITOR_TEMPLATE_PATH))
				.exists() ? WIRE_ACTIVITY_MONITOR_TEMPLATE_PATH
				: ACTIVITY_MONITOR_TEMPLATE_PATH;
		final int INSTRUMENTS_TIMEOUT_MS = 7200000;
		final String[] cmd = {
				"/bin/bash",
				"-c",
				String.format("cd $HOME && instruments -v -t %s -w %s -l %s",
						templatePath, iPhoneUDID, INSTRUMENTS_TIMEOUT_MS) };
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
		this.getPerfMon().stop(2, new int[] { monitorPid },
				PERF_MON_STOP_TIMEOUT);
	}

	private void exportTraceToCSV() throws Exception {
/*
		String script = String.format(CommonUtils
				.readTextFileFromResources("/scripts/export_trace_to_csv.txt"));

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("AppleScriptEngine");
		log.debug("Script engine: " + engine);
		log.debug("Script to execute: " + script);
		try {
			engine.eval(script);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}
*/
		int result = -1;
		int count = 0;
		while (result != 0 && count < 3) {
			CommonUtils
					.executeOsXCommand(new String[] {
							"bash",
							"-c",
							"echo "
									+ CommonUtils
											.getJenkinsSuperUserPassword(CommonUtils.class)
									+ "| sudo -S osascript /Project/iOS_Performance_Reports/export_data.scpt" });
			count++;
		}
	}

	/**
	 * Generates iOS performance report
	 * 
	 * @step. ^I generate performance report$
	 * 
	 * @throws Exception
	 */
	@Then("^I generate performance report for (\\d+) users$")
	public void ThenIGeneratePerformanceReport(int usersCount) throws Exception {
		IOSPerformanceReportGenerator.setUsersCount(usersCount);
		listener.stopListeningLogcat();
		log.debug(listener.getOutput());
		Thread.sleep(5000);
		exportTraceToCSV();
		Assert.assertTrue(IOSPerformanceReportGenerator
				.updateReportDataWithCurrentRun(listener.getOutput()));
		Assert.assertTrue(IOSPerformanceReportGenerator.generateRunReport());
	}
	
	@Before("@performance")
	public void StartLogListener() {
		try {
			listener.startListeningLogcat();
		} catch (Exception e) {
		}
	}
	
	@After("@performance")
	public void CloseInstruments() {
		try {
			IStopPerfMon();
		} catch (Exception e) {
		}
	}
}
