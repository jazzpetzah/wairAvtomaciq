package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import cucumber.api.java.en.When;

public class PerformanceSteps {

	private static final int MIN_WAIT_VALUE_IN_MIN = 1;
	private static final int MAX_WAIT_VALUE_IN_MIN = 2;
	private static final int BACK_END_MESSAGE_COUNT = 5;
	private static final int SEND_MESSAGE_NUM = 4;
	private String randomMessage;
	private static final String picturename = "testing.jpg";
	private static final Logger log = ZetaLogger.getLog(PerformanceSteps.class.getSimpleName());

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
		
		//Main cycle
		while (diffInMinutes < time) {
			
			//Send messages and image by BackEnd
			try {
				CommonUtils.sendRandomMessagesToUser(BACK_END_MESSAGE_COUNT);
				CommonUtils.sendDefaultImageToUser((int) Math
						.floor(BACK_END_MESSAGE_COUNT / 5));
			}
			catch (Exception e) {
				
			}

			

			Thread.sleep(1000);

			//Send messages cycle by UI
			for (int j = 1; j <= SEND_MESSAGE_NUM; ++j) {
				ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
						CommonSteps.senderPages.getContactListPage()
								.getContacts());
				//remove self user from the list
				for (int i = 0; i < visibleContactsList.size(); i++) {
					if (visibleContactsList.get(i).getText().equals(user)) {
						visibleContactsList.remove(i);
						break;
					}
				}
				if (visibleContactsList.size()<= 0){
					continue;
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
				int numberMessages = CommonSteps.senderPages.getConversationPage().getNumberOfMessageEntries(contact);				
				int numberPictures = CommonSteps.senderPages.getConversationPage().getNumberOfImageEntries();
				if (numberMessages > 0 || numberPictures > 0){
					try{
					CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
					}
					catch(Exception exep){
						System.out.println("no need to scroll");
					}
				}
				randomMessage = CommonUtils.generateGUID();
				CommonSteps.senderPages.getConversationPage().writeNewMessage(
						randomMessage);
				Thread.sleep(2000);
				CommonSteps.senderPages.getConversationPage().sendNewMessage();
				Thread.sleep(2000);
				try{
				CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
				CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
				}
				catch(Exception ex){
					log.debug("Scrolling fail: ", ex);
				}
				Thread.sleep(2000);
				try{
				CommonSteps.senderPages.getConversationPage().shortcutChooseImageDialog();
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
				}
				catch(Exception ex){
					log.debug("Image posting failed: ", ex);
				}
				Thread.sleep(1000);
				try{
				CommonSteps.senderPages.getConversationPage().scrollDownToLastMessage();
				}
				catch(Exception exep){
					log.debug("Scrolling fail: ", exep);
				}
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
		log.debug("Client minimized");
	}

	@When("Set random sleep interval")
	public void SetRandomSleepInterval() throws InterruptedException {
		int sleepTimer = ((random.nextInt(MAX_WAIT_VALUE_IN_MIN) + MIN_WAIT_VALUE_IN_MIN) * 60 * 1000);
		log.debug("Sleep time: " + sleepTimer/1000 + " sec.");
		Thread.sleep(sleepTimer);
	}

	@When("Restore ZClient")
	public void RestoreZClient() throws Exception {
		CommonSteps.senderPages.getContactListPage().restoreZClient();
		log.debug("Client restored");
	}

}
