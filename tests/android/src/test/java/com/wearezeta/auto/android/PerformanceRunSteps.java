package com.wearezeta.auto.android;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.DialogPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.CommonUtils;

import cucumber.api.java.en.When;

public class PerformanceRunSteps {
	private static final int SEND_MESSAGE_NUM = 4;
	private static final int BACK_END_MESSAGE_COUNT = 5;
	private static final int MIN_WAIT_VALUE_IN_MIN = 1;
	private static final int MAX_WAIT_VALUE_IN_MIN = 2;

	@When("^I (.*) start test cycle for (.*) minutes")
	public void WhenIStartTestCycleForNMinutes(String name, int time)
			throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;
//		Boolean isMinimized = false; Broke swipe in dialogs
		Random random = new Random();
		name = CommonUtils.retrieveRealUserContactPasswordValue(name);
		while (diffInMinutes < time) {

			// Get BackEnd messages
			CommonUtils.sendRandomMessagesToUser(BACK_END_MESSAGE_COUNT);
			CommonUtils.sendDefaultImageToUser((int) Math
					.floor(BACK_END_MESSAGE_COUNT / 5));
			// ----

/*			Broke swipe in dialogs
 * 			if (isMinimized) {
				PagesCollection.contactListPage.restoreApplication();
			}
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
			for (int i = 0; i < SEND_MESSAGE_NUM; i++) {
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

/*			Broke swipe in dialogs
 * 			PagesCollection.dialogPage.minimizeApplication();
			isMinimized = true;
*/
			Thread.sleep((random.nextInt(MAX_WAIT_VALUE_IN_MIN) + MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);
			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}
	}
}
