package com.wearezeta.auto.android.steps;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.reporter.AndroidPerformanceReportGenerator;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {
	private static final String RXLOGGER_RESOURCE_FILE_PATH = "/sdcard/RxLogger/Resource0.csv";
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private static final int DEFAULT_SWIPE_TIME = 500;
	private static final long DEFAULT_WAIT_TIME = 1000;

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
		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send message to random visible chat
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					// --Get list of visible dialogs visible dialog
					PagesCollection.contactListPage
							.waitForConversationListLoad();
					ArrayList<WebElement> visibleContactsList;
					int counter = 0;
					do {
						Thread.sleep(DEFAULT_WAIT_TIME);
						visibleContactsList = new ArrayList<WebElement>(
								PagesCollection.contactListPage
										.GetVisibleContacts());
						counter++;
					} while ((visibleContactsList.isEmpty() || visibleContactsList == null)
							&& counter != 3);
					visibleContactsList.remove(0);
					// --
					int randomInt;
					String convName;
					do {
						randomInt = perfCommon.random
								.nextInt(visibleContactsList.size() - 1);
						convName = visibleContactsList.get(randomInt).getText();
					} while (convName.contains("tst"));
					PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
							.tapOnContactByPosition(visibleContactsList,
									randomInt);
					PagesCollection.dialogPage.isDialogVisible();
					PagesCollection.dialogPage.tapDialogPageBottom();
					PagesCollection.dialogPage.typeMessage(CommonUtils
							.generateGUID());
					Thread.sleep(DEFAULT_WAIT_TIME);
					if (perfCommon.random.nextBoolean()) {
						PagesCollection.dialogPage
								.swipeDown(DEFAULT_SWIPE_TIME);
						PagesCollection.contactListPage = PagesCollection.dialogPage
								.navigateBack(DEFAULT_SWIPE_TIME);
						PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
								.tapOnContactByPosition(visibleContactsList,
										randomInt);
						PagesCollection.dialogPage.isDialogVisible();
						PagesCollection.dialogPage.tapDialogPageBottom();
						Thread.sleep(DEFAULT_WAIT_TIME);
						PagesCollection.dialogPage.sendFrontCameraImage();
					}
					for (int y = 0; y < 2; y++) {
						PagesCollection.dialogPage
								.swipeDown(DEFAULT_SWIPE_TIME);
					}
					PagesCollection.contactListPage = PagesCollection.dialogPage
							.navigateBack(DEFAULT_SWIPE_TIME);
				}

				/*
				 * Broke swipe in dialogs
				 * PagesCollection.dialogPage.minimizeApplication(); isMinimized
				 * = true;
				 */
			}
		}, timeout);
	}

	/**
	 * Tests loading time of conversation with specified number of messages and
	 * images
	 * 
	 * @step. ^I test conversation loading time for conversation with (\\d+)
	 *        messages and (\\d+) images$
	 * 
	 * @param messages
	 *            number of messages in conversation
	 * @param images
	 *            number of images in conversation
	 * 
	 * @throws Exception
	 * 
	 */
	@When("^I test conversation loading time for conversation with (\\d+) messages and (\\d+) images$")
	public void ITestConversationLoadingTimeForConversation(int messages,
			int images) throws Exception {
		final String CONVERSATION_NAME_TEMPLATE = "perf%stxt%simgtst";
		String conv = String.format(CONVERSATION_NAME_TEMPLATE, messages,
				images);
		int count = 0;
		while (count < 10) {
			count++;
			boolean isPassed = true;
			try {
				PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
						.tapOnName(conv);
			} catch (Exception e) {
				isPassed = false;
				PagesCollection.contactListPage.contactListSwipeUp(1000);
			}
			if (isPassed)
				break;
		}
		PagesCollection.dialogPage.isDialogVisible();
		CommonAndroidSteps.listener.stopListeningLogcat();
		for (int y = 0; y < 2; y++) {
			PagesCollection.dialogPage.swipeDown(DEFAULT_SWIPE_TIME);
		}
		PagesCollection.contactListPage = PagesCollection.dialogPage
				.navigateBack(DEFAULT_SWIPE_TIME);
	}

	/**
	 * Generates android performance report
	 * 
	 * @step. ^I generate performance report$
	 * 
	 * @throws Exception
	 */
	@Then("^I generate performance report for (\\d+) users$")
	public void ThenIGeneratePerformanceReport(int usersCount) throws Exception {
		AndroidPerformanceReportGenerator.setUsersCount(usersCount);
		// CommonAndroidSteps.listener.stopListeningLogcat();
		AndroidCommonUtils.copyFileFromAndroid(
				AndroidPerformanceReportGenerator.RXLOG_FILEPATH,
				RXLOGGER_RESOURCE_FILE_PATH);
		Thread.sleep(5000);
		Assert.assertTrue(AndroidPerformanceReportGenerator
				.updateReportDataWithCurrentRun(CommonAndroidSteps.listener
						.getOutput()));
		Assert.assertTrue(AndroidPerformanceReportGenerator.generateRunReport());
	}
}
