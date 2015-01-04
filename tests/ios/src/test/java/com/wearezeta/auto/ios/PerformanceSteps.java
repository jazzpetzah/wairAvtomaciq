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

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();
	private static final int MIN_SLEEP_VALUE = 30000;

	private Random random = new Random();

	@When("^I (.*) start test cycle for (\\d+) minutes")
	public void WhenIStartTestCycleForNMinutes(String nameAlias, int time)
			throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;

		final String name = perfCommon.getUserManager()
				.findUserByNameAlias(nameAlias).getName();
		while (diffInMinutes < time) {

			// Send x messages and x/5 images to your user
			perfCommon.sendRandomMessagesToUser(
					PerformanceCommon.BACK_END_MESSAGE_COUNT,
					PerformanceCommon.PARALLEL_MSGS);
			perfCommon
					.sendDefaultImageToUser(PerformanceCommon.BACK_END_MESSAGE_COUNT / 5);

			// Send message to random visible chat
			for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {

				// --Get list of visible dialogs, remove self user name from
				// this list
				Assert.assertTrue("Contact list didn't load",
						PagesCollection.contactListPage
								.waitForContactListToLoad());
				ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
						PagesCollection.contactListPage.GetVisibleContacts());
				for (int y = 0; y < visibleContactsList.size(); y++) {
					WebElement el = visibleContactsList.get(y);
					String user = el.getText();
					if (user.equals(name)) {
						visibleContactsList.remove(y);
						break;
					}
				}
				// --

				int randomInt = random.nextInt(visibleContactsList.size() - 1);
				PagesCollection.dialogPage = (DialogPage) PagesCollection.contactListPage
						.tapOnContactByIndex(visibleContactsList, randomInt);
				Thread.sleep(1000);
				// PagesCollection.dialogPage =
				// PagesCollection.dialogPage.sendImageFromAlbum();
				PagesCollection.dialogPage.tapOnCursorInput();
				PagesCollection.dialogPage.sendStringToInput(CommonUtils
						.generateGUID() + "\n");
				Thread.sleep(1000);
				PagesCollection.dialogPage.swipeDialogPageDown(500);
				Thread.sleep(2000);
				/*
				 * if (random.nextBoolean()) { Thread.sleep(1000);
				 * PagesCollection.dialogPage =
				 * PagesCollection.dialogPage.sendImageFromAlbum(); }
				 */
				for (int y = 0; y < 2; y++) {
					PagesCollection.dialogPage.swipeDialogPageDown(500);
					Thread.sleep(1000);
				}
				PagesCollection.contactListPage = (ContactListPage) PagesCollection.dialogPage
						.swipeRight(500);
				Thread.sleep(2000);
			}
			// ----

			// PagesCollection.contactListPage.minimizeZeta();
			Thread.sleep(MIN_SLEEP_VALUE
					+ (random.nextInt(PerformanceCommon.MAX_WAIT_VALUE_IN_MIN) + PerformanceCommon.MIN_WAIT_VALUE_IN_MIN)
					* 60 * 1000);
			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}
	}
}
