package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;
import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.osx.pages.ChoosePicturePage;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;

import cucumber.api.java.en.When;

public class PerformanceSteps {
	private final PerformanceCommon perfCommon = PerformanceCommon
			.getInstance();

	private String randomMessage;
	private static final String picturename = "testing.jpg";

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
	@When("^I start testing cycle for (\\d+) minutes$")
	public void WhenIStartTestingCycleForMinutes(int timeout) throws Exception {
		CommonOSXSteps.senderPages
				.setContactListPage(new ContactListPage(
						CommonUtils
								.getOsxAppiumUrlFromConfig(LoginPageSteps.class),
						CommonUtils
								.getOsxApplicationPathFromConfig(LoginPageSteps.class)));

		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send messages cycle by UI
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							CommonOSXSteps.senderPages.getContactListPage()
									.getContacts());
					visibleContactsList.remove(0);

					final int contactIdx = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					String contact = visibleContactsList.get(contactIdx)
							.getText();
					CommonOSXSteps.senderPages.getContactListPage()
							.openConversation(contact, false);

					CommonOSXSteps.senderPages.setConversationPage(new ConversationPage(
							CommonUtils
									.getOsxAppiumUrlFromConfig(ContactListPageSteps.class),
							CommonUtils
									.getOsxApplicationPathFromConfig(ContactListPageSteps.class)));
					int numberMessages = CommonOSXSteps.senderPages
							.getConversationPage().getNumberOfMessageEntries(
									contact);
					int numberPictures = CommonOSXSteps.senderPages
							.getConversationPage().getNumberOfImageEntries();
					if (numberMessages > 0 || numberPictures > 0) {
						try {
							CommonOSXSteps.senderPages.getConversationPage()
									.scrollDownToLastMessage();
						} catch (Exception exep) {
							System.out.println("no need to scroll");
						}
					}
					randomMessage = CommonUtils.generateGUID();
					CommonOSXSteps.senderPages.getConversationPage()
							.writeNewMessage(randomMessage);
					CommonOSXSteps.senderPages.getConversationPage()
							.sendNewMessage();
					try {
						CommonOSXSteps.senderPages.getConversationPage()
								.scrollDownToLastMessage();
						CommonOSXSteps.senderPages.getConversationPage()
								.scrollDownToLastMessage();
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Scrolling fail: ", ex);
					}
					try {
						CommonOSXSteps.senderPages.getConversationPage()
								.shortcutChooseImageDialog();
						CommonOSXSteps.senderPages
								.setChoosePicturePage(new ChoosePicturePage(
										CommonUtils
												.getOsxAppiumUrlFromConfig(ContactListPage.class),
										CommonUtils
												.getOsxApplicationPathFromConfig(ContactListPage.class)));

						ChoosePicturePage choosePicturePage = CommonOSXSteps.senderPages
								.getChoosePicturePage();
						Assert.assertTrue(choosePicturePage.isVisible());

						choosePicturePage.openImage(picturename);
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Image posting failed: ",
								ex);
					}
					try {
						CommonOSXSteps.senderPages.getConversationPage()
								.scrollDownToLastMessage();
					} catch (Exception exep) {
						perfCommon.getLogger().debug("Scrolling fail: ", exep);
					}
				}

				minimizeClient();
				restoreClient();
			}
		}, timeout);
	}

	public void minimizeClient() throws Exception {
		CommonOSXSteps.senderPages.getContactListPage()
				.waitUntilMainWindowAppears();
		CommonOSXSteps.senderPages.getContactListPage().minimizeZClient();
		perfCommon.getLogger().debug("Client minimized");
	}

	public void restoreClient() throws Exception {
		CommonOSXSteps.senderPages.getContactListPage().restoreZClient();
		perfCommon.getLogger().debug("Client restored");
	}

}
