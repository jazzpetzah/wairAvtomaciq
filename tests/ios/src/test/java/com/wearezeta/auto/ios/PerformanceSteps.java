package com.wearezeta.auto.ios;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.common.driver.DriverUtils;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

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
	public void WhenIStartTestCycleForNMinutes(int timeout)
			throws Exception {
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

					int randomChatIdx = perfCommon.random.nextInt(visibleContactsList
							.size() - 1);
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
}
