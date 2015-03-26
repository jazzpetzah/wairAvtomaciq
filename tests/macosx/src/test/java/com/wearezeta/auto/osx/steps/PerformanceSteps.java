package com.wearezeta.auto.osx.steps;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.PerformanceCommon;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.PerformanceCommon.PerformanceLoop;
import com.wearezeta.auto.osx.pages.ContactListPage;
import com.wearezeta.auto.osx.pages.ConversationPage;
import com.wearezeta.auto.osx.pages.PagesCollection;
import com.wearezeta.auto.osx.pages.common.ChoosePicturePage;

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
		PagesCollection.contactListPage = new ContactListPage(
				PagesCollection.mainMenuPage.getDriver(),
				PagesCollection.mainMenuPage.getWait());

		perfCommon.runPerformanceLoop(new PerformanceLoop() {
			public void run() throws Exception {
				// Send messages cycle by UI
				for (int i = 0; i < PerformanceCommon.SEND_MESSAGE_NUM; i++) {
					ArrayList<WebElement> visibleContactsList = new ArrayList<WebElement>(
							PagesCollection.contactListPage.getContacts());
					visibleContactsList.remove(0);

					final int contactIdx = perfCommon.random
							.nextInt(visibleContactsList.size() - 1);
					String contact = visibleContactsList.get(contactIdx)
							.getText();
					PagesCollection.contactListPage.openConversation(contact,
							false);

					PagesCollection.conversationPage = new ConversationPage(
							PagesCollection.contactListPage.getDriver(),
							PagesCollection.contactListPage.getWait());
					int numberMessages = PagesCollection.conversationPage
							.getNumberOfMessageEntries(contact);
					int numberPictures = PagesCollection.conversationPage
							.getNumberOfImageEntries();
					if (numberMessages > 0 || numberPictures > 0) {
						try {
							PagesCollection.conversationPage
									.scrollDownToLastMessage();
						} catch (Exception exep) {
							System.out.println("no need to scroll");
						}
					}
					randomMessage = CommonUtils.generateGUID();
					PagesCollection.conversationPage
							.writeNewMessage(randomMessage);
					PagesCollection.conversationPage.sendNewMessage();
					try {
						PagesCollection.conversationPage
								.scrollDownToLastMessage();
						PagesCollection.conversationPage
								.scrollDownToLastMessage();
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Scrolling fail: ", ex);
					}
					try {
						PagesCollection.conversationPage
								.shortcutChooseImageDialog();
						PagesCollection.choosePicturePage = new ChoosePicturePage(
								PagesCollection.conversationPage.getDriver(),
								PagesCollection.conversationPage.getWait());

						Assert.assertTrue(PagesCollection.choosePicturePage
								.isVisible());

						PagesCollection.choosePicturePage
								.openImage(picturename);
					} catch (Exception ex) {
						perfCommon.getLogger().debug("Image posting failed: ",
								ex);
					}
					try {
						PagesCollection.conversationPage
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
		PagesCollection.contactListPage.waitUntilMainWindowAppears();
		PagesCollection.contactListPage.minimizeZClient();
		perfCommon.getLogger().debug("Client minimized");
	}

	public void restoreClient() throws Exception {
		PagesCollection.contactListPage.restoreZClient();
		perfCommon.getLogger().debug("Client restored");
	}

}
