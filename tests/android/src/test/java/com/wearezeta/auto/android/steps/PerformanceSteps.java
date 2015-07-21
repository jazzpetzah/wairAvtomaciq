package com.wearezeta.auto.android.steps;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.reporter.AndroidPerfReportModel;
import com.wearezeta.auto.android.common.reporter.AndroidPerformanceHelpers;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.performance.PerformanceCommon;
import com.wearezeta.auto.common.performance.PerformanceCommon.PerformanceLoop;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class PerformanceSteps {

	private static final Logger log = ZetaLogger.getLog(PerformanceSteps.class
			.getSimpleName());

	private final AndroidPagesCollection pagesCollection = AndroidPagesCollection
			.getInstance();
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private static final int DEFAULT_SWIPE_TIME = 500;
	private static final long DEFAULT_WAIT_TIME = 1000;

	private ContactListPage getContactListPage() throws Exception {
		return (ContactListPage) pagesCollection.getPage(ContactListPage.class);
	}

	private DialogPage getDialogPage() throws Exception {
		return (DialogPage) pagesCollection.getPage(DialogPage.class);
	}

	public ArrayList<WebElement> resetVisibleContactList() throws Exception {
		ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>();
		int counter = 0;
		do {
			Thread.sleep(DEFAULT_WAIT_TIME);
			visibleContactsList = new ArrayList<WebElement>(
					getContactListPage().GetVisibleContacts());
			counter++;
		} while ((visibleContactsList.isEmpty() || visibleContactsList == null)
				&& counter != 3);
		visibleContactsList.remove(0);
		return visibleContactsList;
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
		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send message to random visible chat
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					// --Get list of visible dialogs visible dialog
					getContactListPage().waitForConversationListLoad();
					ArrayList<WebElement> visibleContactsList = resetVisibleContactList();
					// --
					int randomInt;
					String convName;
					do {
						randomInt = perfCommon.random
								.nextInt(visibleContactsList.size() - 1);
						convName = visibleContactsList.get(randomInt).getText();
					} while (convName.contains("tst"));
					try {
						getContactListPage().tapOnContactByPosition(
								visibleContactsList, randomInt);
					} catch (Exception e) {
						visibleContactsList = resetVisibleContactList();
						getContactListPage().tapOnContactByPosition(
								visibleContactsList, randomInt);
					}
					getDialogPage().isDialogVisible();
					getDialogPage().tapDialogPageBottom();
					getDialogPage().typeMessage(CommonUtils.generateGUID());
					Thread.sleep(DEFAULT_WAIT_TIME);
					if (perfCommon.random.nextBoolean()) {
						getDialogPage().swipeDown(DEFAULT_SWIPE_TIME);
						getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
						try {
							getContactListPage().tapOnContactByPosition(
									visibleContactsList, randomInt);
						} catch (Exception e) {
							visibleContactsList = resetVisibleContactList();
							getContactListPage().tapOnContactByPosition(
									visibleContactsList, randomInt);
						}
						getDialogPage().isDialogVisible();
						getDialogPage().tapDialogPageBottom();

						Thread.sleep(DEFAULT_WAIT_TIME);
						boolean successful = false;
						int count = 0;
						do {
							try {
								getDialogPage().sendFrontCameraImage();
								successful = true;
							} catch (Throwable e) {
								log.debug("Camera image was not send before. Workaround...");
								for (int y = 0; y < 2; y++) {
									getDialogPage().swipeDown(
											DEFAULT_SWIPE_TIME);
								}
								getDialogPage()
										.navigateBack(DEFAULT_SWIPE_TIME);
								visibleContactsList = resetVisibleContactList();
								getContactListPage().tapOnContactByPosition(
										visibleContactsList, randomInt);
							}
							count++;
						} while (!successful && count < 2);
					}
					for (int y = 0; y < 2; y++) {
						getDialogPage().swipeDown(DEFAULT_SWIPE_TIME);
					}
					getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
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
				getContactListPage().tapOnName(conv);
			} catch (Exception e) {
				isPassed = false;
				getContactListPage().contactListSwipeUp(1000);
			}
			if (isPassed)
				break;
		}
		getDialogPage().isDialogVisible();
		for (int y = 0; y < 2; y++) {
			getDialogPage().swipeDown(DEFAULT_SWIPE_TIME);
		}
		getDialogPage().navigateBack(DEFAULT_SWIPE_TIME);
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
