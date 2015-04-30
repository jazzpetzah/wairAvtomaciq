package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
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

	private String randomMessage;
	private static final String picturename = "testing.jpg";

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
	public void WhenIStartTestingCycleForMinutes(int timeout) throws Exception {
		PagesCollection.contactListPage = new ContactListPage(
				PagesCollection.mainMenuPage.getDriver(),
				PagesCollection.mainMenuPage.getWait());

		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send messages cycle by UI
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							PagesCollection.contactListPage.getContacts());
					visibleContactsList.remove(0);

					final int contactIdx = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					String contact = visibleContactsList.get(contactIdx)
							.getText();
					PagesCollection.contactListPage.openConversation(contact,
							false);

					PagesCollection.conversationPage = new ConversationPage(
							PagesCollection.contactListPage.getDriver(),
							PagesCollection.contactListPage.getWait());
					int numberMessages = PagesCollection.conversationPage
							.getNumberOfMessageEntries(contact);
					int numberPictures = PagesCollection.conversationPage
							.getNumberOfImageEntries();
					if (numberMessages > 0 || numberPictures > 0) {
						try {
							PagesCollection.conversationPage
									.scrollDownToLastMessage();
						} catch (Exception exep) {
							System.out.println("no need to scroll");
						}
					}
					randomMessage = CommonUtils.generateGUID();
					PagesCollection.conversationPage
							.writeNewMessage(randomMessage);
					PagesCollection.conversationPage.sendNewMessage();
					try {
						PagesCollection.conversationPage
								.scrollDownToLastMessage();
						PagesCollection.conversationPage
								.scrollDownToLastMessage();
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Scrolling fail: ", ex);
					}
					try {
						PagesCollection.choosePicturePage = PagesCollection.mainMenuPage
								.sendImage();
						Assert.assertTrue(PagesCollection.choosePicturePage
								.isVisible());

						PagesCollection.choosePicturePage
								.openImage(picturename);
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Image posting failed: ",
								ex);
					}
					try {
						PagesCollection.conversationPage
								.scrollDownToLastMessage();
					} catch (Exception exep) {
						perfCommon.getLogger().debug("Scrolling fail: ", exep);
					}
				}

				minimizeClient();
				restoreClient();
			}
		}, timeout);
	}

	/**
	 * Start performance monitor tool for the current OS X system. This will
	 * throw the RuntimeException if instruments failed to start. All the
	 * collected perf logs will be saved in $HOME folder
	 * 
	 * @step. ^I start performance monitoring for OS X$
	 * 
	 * @throws Exception
	 */
	@When("^I start performance monitoring for OS X$")
	public void IStartPerfMon() throws Exception {
		final String[] cmd = {
				"/bin/bash",
				"-c",
				String.format("cd $HOME && instruments -v -t %s",
						ACTIVITY_MONITOR_TEMPLATE_PATH) };
		final AsyncProcess ap = new AsyncProcess(cmd, true, true);
		ap.start();
		Thread.sleep(PERF_MON_INIT_DELAY);
		if (ap.isRunning()) {
			setPerfMon(ap);
		} else {
			throw new RuntimeException(
					"There are failures while starting perf monitor. "
							+ "Please check the log for more details.");
		}
	}

	/**
	 * Stop performance monitor tool for the current OS X system and flush
	 * performance logs. This will throw RuntimeException if 'I start
	 * performance monitoring for OS X' step has not been invoked before
	 * 
	 * @step. ^I finish performance monitoring for OS X$
	 * 
	 * @throws Exception
	 */
	@Then("^I finish performance monitoring for OS X$")
	public void IStopPerfMon() throws Exception {
		if (this.getPerfMon() == null) {
			throw new RuntimeException(
					"Please call the Start monitor step first");
		}
		if (!this.getPerfMon().isRunning()) {
			throw new RuntimeException(
					"Performance monitor has been unexpectedly killed before. "
							+ "Please check execution logs to get more details.");
		}
		// the real process id will be this parentPid + 1, because bash forks
		// a separate process from the command line that we provide
		final int monitorPid = this.getPerfMon().getPid() + 1;
		// Sending SIGINT to properly terminate perf monitor
		this.getPerfMon().stop("2", new int[] { monitorPid },
				PERF_MON_STOP_TIMEOUT);
	}

	public void minimizeClient() throws Exception {
		PagesCollection.contactListPage.waitUntilMainWindowAppears();
		PagesCollection.contactListPage.minimizeWindowUsingScript();
		perfCommon.getLogger().debug("Client minimized");
	}

	public void restoreClient() throws Exception {
		PagesCollection.mainMenuPage.restoreClient();
		perfCommon.getLogger().debug("Client restored");
	}

	public AsyncProcess getPerfMon() {
		return perfMon;
	}

	public void setPerfMon(AsyncProcess perfMon) {
		this.perfMon = perfMon;
	}

}
