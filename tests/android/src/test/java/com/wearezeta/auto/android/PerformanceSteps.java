package com.wearezeta.auto.android;

import java.util.ArrayList;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.ContactListPage;
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
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							PagesCollection.contactListPage
									.GetVisibleContacts());
					visibleContactsList.remove(0);
					// --

					int randomInt = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
							.tapOnContactByPosition(visibleContactsList,
									randomInt);
					Thread.sleep(1000);
					PagesCollection.dialogPage.tapOnCursorFrame();
					PagesCollection.dialogPage.typeMessage(CommonUtils
							.generateGUID());
					Thread.sleep(1000);
					PagesCollection.dialogPage.swipeDown(500);
					if (perfCommon.random.nextBoolean()) {
						Thread.sleep(1000);
						PagesCollection.dialogPage.sendFrontCameraImage();
					}
					for (int y = 0; y < 2; y++) {
						PagesCollection.dialogPage.swipeDown(500);
					}
					PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
							.swipeRight(500);
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
