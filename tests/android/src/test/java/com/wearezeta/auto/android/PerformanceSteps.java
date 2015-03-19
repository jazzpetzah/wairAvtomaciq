package com.wearezeta.auto.android;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

	/**
	 * Starts standard actions loop (read messages/send messages) to measure
	 * application performance
	 * 
	 * @step. ^I start testing cycle for (\\d+) minutes$
	 * 
	 * @param timeout
	 *            number of minutes to run the loop
	 * @throws Throwable
	 */
	@When("^I start test cycle for (\\d+) minutes$")
	public void WhenIStartTestCycleForNMinutes(int timeout) throws Throwable {
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
						Thread.sleep(1000);
						visibleContactsList = new ArrayList<WebElement>(
								PagesCollection.contactListPage
										.GetVisibleContacts());
						counter++;
					} while ((visibleContactsList.isEmpty()
							|| visibleContactsList == null) && counter != 3);
					visibleContactsList.remove(0);
					// --

					int randomInt = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
							.tapOnContactByPosition(visibleContactsList,
									randomInt);
					PagesCollection.dialogPage.isDialogVisible();
					PagesCollection.dialogPage
							.tapDialogPageBottomLinearLayout();
					PagesCollection.dialogPage.typeMessage(CommonUtils
							.generateGUID());
					Thread.sleep(1000);
					if (perfCommon.random.nextBoolean()) {
						PagesCollection.contactListPage = PagesCollection.dialogPage
								.navigateBack();
						PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
								.tapOnContactByPosition(visibleContactsList,
										randomInt);
						PagesCollection.dialogPage.isDialogVisible();
						PagesCollection.dialogPage
								.tapDialogPageBottomLinearLayout();
						PagesCollection.dialogPage.sendFrontCameraImage();
					}
					for (int y = 0; y < 2; y++) {
						PagesCollection.dialogPage.swipeDown(500);
					}
					PagesCollection.contactListPage = PagesCollection.dialogPage
							.navigateBack();
				}

				/*
				 * Broke swipe in dialogs
				 * PagesCollection.dialogPage.minimizeApplication(); isMinimized
				 * = true;
				 */
			}
		}, timeout);
	}
}
