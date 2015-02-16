package com.wearezeta.auto.ios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.ios.pages.ContactListPage;
import com.wearezeta.auto.ios.pages.DialogPage;
import com.wearezeta.auto.ios.pages.PagesCollection;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

	// seconds
	private static final int MIN_SLEEP_INTERVAL = 5;
	private static final int MAX_SLEEP_INTERVAL = 15;

	private Random random = new Random();

	@When("^I (.*) start test cycle for (\\d+) minutes")
	public void WhenIStartTestCycleForNMinutes(String nameAlias, int time)
			throws Exception {
		LocalDateTime startDateTime = LocalDateTime.now();
		long secondsElapsed = 0;
		do {
			// Send x messages and x/5 images to your user via backend
			perfCommon.sendRandomMessagesToUser(
					PerformanceCommon.BACK_END_MESSAGE_COUNT,
					PerformanceCommon.SIMULTANEOUS_MSGS_COUNT);
			perfCommon
					.sendDefaultImageToUser(PerformanceCommon.BACK_END_MESSAGE_COUNT / 5);

			// Send messages in response to a random visible chat
			for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
				// Get list of visible dialogs, remove self user name from this list
				Assert.assertTrue("Contact list didn't load",
						PagesCollection.contactListPage
								.waitForContactListToLoad());
				ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
						PagesCollection.contactListPage.GetVisibleContacts());
				visibleContactsList.remove(0);

				int randomChatIdx = random
						.nextInt(visibleContactsList.size() - 1);
				PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
						.tapOnContactByIndex(visibleContactsList, randomChatIdx);

				// Emulating reading of previously received messages
				// This is broken in iOS simulator, known Apple bug
				// Using "external" swipe is unstable for a long perf test
				// PagesCollection.dialogPage.swipeDown(500);

				// Writing response
				PagesCollection.dialogPage.tapOnCursorInput();
				PagesCollection.dialogPage.sendMessageUsingScript(CommonUtils
						.generateGUID());

				// Swipe back to the convo list
				PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
						.swipeRight(
								500,
								DriverUtils.SWIPE_X_DEFAULT_PERCENTAGE_HORIZONTAL,
								30);
			}

			Thread.sleep((MIN_SLEEP_INTERVAL + random
					.nextInt(MAX_SLEEP_INTERVAL - MIN_SLEEP_INTERVAL + 1)) * 1000);
			secondsElapsed = java.time.Duration.between(startDateTime,
					LocalDateTime.now()).toMillis() * 1000;
		} while (secondsElapsed < time * 60);
	}
}
