package com.wearezeta.auto.android;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

	private Random random = new Random();

	@When("^I (.*) start test cycle for (\\d+) minutes")
	public void WhenIStartTestCycleForNMinutes(String nameAlias, int time)
			throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;
		// Boolean isMinimized = false; Broke swipe in dialogs

		final String name = perfCommon.getUserManager()
				.findUserByNameOrNameAlias(nameAlias).getName();
		while (diffInMinutes < time) {

			// Get BackEnd messages
			perfCommon.sendRandomMessagesToUser(
					PerformanceCommon.BACK_END_MESSAGE_COUNT,
					PerformanceCommon.SIMULTANEOUS_MSGS_COUNT);
			perfCommon
					.sendDefaultImageToUser(PerformanceCommon.BACK_END_MESSAGE_COUNT / 5);
			// ----

			/*
			 * Broke swipe in dialogs if (isMinimized) {
			 * PagesCollection.contactListPage.restoreApplication(); }
			 */
			// --Get list of visible dialogs visible dialog
			ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
					PagesCollection.contactListPage.GetVisibleContacts());
			for (int i = 0; i < visibleContactsList.size(); i++) {
				if (visibleContactsList.get(i).getText().equals(name)) {
					visibleContactsList.remove(i);
					break;
				}
			}
			// --

			// Send message to random visible chat
			for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
				int randomInt = random.nextInt(visibleContactsList.size() - 1);
				PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
						.tapOnContactByPosition(visibleContactsList, randomInt);
				Thread.sleep(1000);
				PagesCollection.dialogPage.tapOnCursorFrame();
				PagesCollection.dialogPage.typeMessage(CommonUtils
						.generateGUID());
				Thread.sleep(1000);
				PagesCollection.dialogPage.swipeDown(500);
				if (random.nextBoolean()) {
					Thread.sleep(1000);
					PagesCollection.dialogPage.sendFrontCameraImage();
				}
				for (int y = 0; y < 2; y++) {
					PagesCollection.dialogPage.swipeDown(500);
				}
				PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
						.swipeRight(500);
			}
			// ----

			/*
			 * Broke swipe in dialogs
			 * PagesCollection.dialogPage.minimizeApplication(); isMinimized =
			 * true;
			 */
			Thread.sleep((random
					.nextInt(PerformanceCommon.MAX_WAIT_VALUE_IN_MIN) + PerformanceCommon.MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);
			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}
	}
}
