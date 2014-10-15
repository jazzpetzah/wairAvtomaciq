package com.wearezeta.auto.osx.steps;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.LoginPage;
import com.wearezeta.auto.osx.pages.MainMenuPage;
import com.wearezeta.auto.osx.pages.PagesCollection;

import cucumber.api.java.en.When;

public class PerformanceSteps {

	private static final int MIN_WAIT_VALUE_IN_MIN = 1;
	private static final int MAX_WAIT_VALUE_IN_MIN = 2;
	private static final int BACK_END_MESSAGE_COUNT = 5;
	private static final int SEND_MESSAGE_NUM = 4;
	private String randomMessage;
	private static final String picturename = "test.jpg";

	Random random = new Random();

	@When("^I (.*) start testing cycle for (\\d+) minutes$")
	public void WhenIStartTestingCycleForMinutes(String user, int time)
			throws Throwable {
		LocalDateTime startDateTime = LocalDateTime.now();
		long diffInMinutes = 0;

		user = CommonUtils.retrieveRealUserContactPasswordValue(user);
		CommonSteps.senderPages
				.setContactListPage(new ContactListPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(LoginPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(LoginPageSteps.class)));

		while (diffInMinutes < time) {

			CommonUtils.sendRandomMessagesToUser(BACK_END_MESSAGE_COUNT);
			CommonUtils.sendDefaultImageToUser((int) Math
					.floor(BACK_END_MESSAGE_COUNT / 5));

			Thread.sleep(1000);

			for (int j = 1; j <= SEND_MESSAGE_NUM; ++j) {
				ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
						CommonSteps.senderPages.getContactListPage()
								.getContacts());
				for (int i = 0; i < visibleContactsList.size(); i++) {
					if (visibleContactsList.get(i).getText().equals(user)) {
						visibleContactsList.remove(i);
						break;
					}
				}
				
				int randomInt = random.nextInt(visibleContactsList.size() - 1);
				String contact = visibleContactsList.get(randomInt).getText();
				CommonSteps.senderPages.getContactListPage().openConversation(
						contact);
				Thread.sleep(100);
				
				CommonSteps.senderPages
						.setConversationPage(new ConversationPage(
								CommonUtils
										.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
								CommonUtils
										.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
				Thread.sleep(2000);
				randomMessage = CommonUtils.generateGUID();
				CommonSteps.senderPages.getConversationPage().writeNewMessage(
						randomMessage);
				Thread.sleep(2000);
				CommonSteps.senderPages.getConversationPage().sendNewMessage();
				Thread.sleep(1000);
				CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
				Thread.sleep(1000);

				CommonSteps.senderPages.getConversationPage()
						.openChooseImageDialog();
				CommonSteps.senderPages
						.setChoosePicturePage(new ChoosePicturePage(
								CommonUtils
										.getOsxAppiumUrlFromConfig(ContactListPage.class),
								CommonUtils
										.getOsxApplicationPathFromConfig(ContactListPage.class)));

				ChoosePicturePage choosePicturePage = CommonSteps.senderPages
						.getChoosePicturePage();
				Assert.assertTrue(choosePicturePage.isVisible());

				choosePicturePage.openImage(picturename);
				
				Thread.sleep(1000);
				CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
			}

			MinimizeZClient();
			SetRandomSleepInterval();
			RestoreZClient();
			Thread.sleep(2000);

			LocalDateTime currentDateTime = LocalDateTime.now();
			diffInMinutes = java.time.Duration.between(startDateTime,
					currentDateTime).toMinutes();
		}

	}

	@When("Minimize ZClient")
	public void MinimizeZClient() throws Exception {
		CommonSteps.senderPages.getContactListPage()
				.waitUntilMainWindowAppears();
		CommonSteps.senderPages.getContactListPage().minimizeZClient();
	}

	@When("Set random sleep interval")
	public void SetRandomSleepInterval() throws InterruptedException {
		int sleepTimer = ((random.nextInt(MAX_WAIT_VALUE_IN_MIN) + MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);

		System.out.print(sleepTimer);
		Thread.sleep(sleepTimer);
	}

	@When("Restore ZClient")
	public void RestoreZClient() throws Exception {
		CommonSteps.senderPages.getContactListPage().restoreZClient();
		
	}

}
