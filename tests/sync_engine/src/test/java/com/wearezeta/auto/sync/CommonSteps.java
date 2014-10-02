package com.wearezeta.auto.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.client.ZetaInstance;
import com.wearezeta.auto.sync.report.ReportData;
import com.wearezeta.auto.sync.report.ReportGenerator;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CommonSteps {
	static {
		System.setProperty("java.awt.headless", "false");
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private static final Logger log = ZetaLogger.getLog(CommonSteps.class
			.getSimpleName());

	@Before
	public void setup() throws Exception {
		boolean generateUsersFlag = Boolean.valueOf(SyncEngineUtil
				.getCommonGenerateUsersFromConfig(this.getClass()));

		if (generateUsersFlag) {
			SyncEngineUtil.generateUsers(3);
			Thread.sleep(SyncEngineUtil.BACKEND_SYNC_TIMEOUT);
			SyncEngineUtil.connectUsers();

			// create group chat
			ArrayList<ClientUser> chatParticipants = new ArrayList<ClientUser>();
			chatParticipants.add(SyncEngineUtil.usersList.get(1));
			chatParticipants.add(SyncEngineUtil.usersList.get(2));

			BackEndREST.createGroupConversation(
					SyncEngineUtil.usersList.get(0), chatParticipants,
					SyncEngineUtil.CHAT_NAME);
		} else {
			SyncEngineUtil.usePrecreatedUsers();
		}

		// init platform clients
		int i = 0;
		for (String platform : SyncEngineUtil.platforms) {
			ZetaInstance client = new ZetaInstance(platform);
			client.setUserInstance(SyncEngineUtil.usersList.get(i++));
			ExecutionContext.clients.put(platform, client);
		}

		// OSX initialization
		if (ExecutionContext.isOsxEnabled()) {
			String osxPath = CommonUtils.getOsxApplicationPathFromConfig(this
					.getClass());
			com.wearezeta.auto.osx.steps.CommonSteps.senderPages = new com.wearezeta.auto.osx.pages.PagesCollection();
			com.wearezeta.auto.osx.pages.PagesCollection osxSenderPages = com.wearezeta.auto.osx.steps.CommonSteps.senderPages;

			long startDate = new Date().getTime();
			osxSenderPages
					.setMainMenuPage(new com.wearezeta.auto.osx.pages.MainMenuPage(
							CommonUtils.getOsxAppiumUrlFromConfig(this
									.getClass()), osxPath));
			osxSenderPages
					.setLoginPage(new com.wearezeta.auto.osx.pages.LoginPage(
							CommonUtils.getOsxAppiumUrlFromConfig(this
									.getClass()), osxPath));
			long endDate = new Date().getTime();
			ExecutionContext.osxZeta().setStartupTimeMs(endDate-startDate);
			log.debug("OSX application startup time: "
					+ ExecutionContext.osxZeta().getStartupTimeMs() + "ms");
			ZetaFormatter.setDriver(osxSenderPages.getLoginPage().getDriver());
			osxSenderPages.getLoginPage().sendProblemReportIfFound();
		}

		// Android initialization
		if (ExecutionContext.isAndroidEnabled()) {
			try {
				AndroidCommonUtils.disableHints();
			} catch (Exception e) {
				log.fatal("Failed to create disable hints config.\n" + e.getMessage());
				e.printStackTrace();
			}
			String androidPath = CommonUtils
					.getAndroidApplicationPathFromConfig(this.getClass());
			if (com.wearezeta.auto.android.pages.PagesCollection.loginPage == null) {
				long startDate = new Date().getTime();
				com.wearezeta.auto.android.pages.PagesCollection.loginPage = new com.wearezeta.auto.android.pages.LoginPage(
						CommonUtils.getAndroidAppiumUrlFromConfig(this
								.getClass()), androidPath);
				long endDate = new Date().getTime();
				ExecutionContext.androidZeta().setStartupTimeMs(endDate-startDate);
				log.debug("Android application startup time: "
						+ ExecutionContext.androidZeta().getStartupTimeMs() + "ms");
			}
		}
		// iOS initialization
		if (ExecutionContext.isIosEnabled()) {
			String iosPath = CommonUtils.getIosApplicationPathFromConfig(this
					.getClass());
			if (com.wearezeta.auto.ios.pages.PagesCollection.loginPage == null) {
				long startDate = new Date().getTime();
				com.wearezeta.auto.ios.pages.PagesCollection.loginPage = new com.wearezeta.auto.ios.pages.LoginPage(
						CommonUtils.getIosAppiumUrlFromConfig(this.getClass()),
						iosPath);
				long endDate = new Date().getTime();
				ExecutionContext.iosZeta().setStartupTimeMs(endDate-startDate);
				log.debug("iOS application startup time: "
						+ ExecutionContext.iosZeta().getStartupTimeMs() + "ms");
			}
		}
	}

	@After
	public void teardown() throws IllegalArgumentException,
			IllegalAccessException, IOException {
		// osx teardown
		if (ExecutionContext.isOsxEnabled()) {
			com.wearezeta.auto.osx.steps.CommonSteps.senderPages
					.closeAllPages();
		}

		// android teardown
		if (ExecutionContext.isAndroidEnabled()) {
			com.wearezeta.auto.android.pages.PagesCollection.loginPage.Close();
			com.wearezeta.auto.android.pages.AndroidPage.clearPagesCollection();
		}

		// ios teardown
		if (ExecutionContext.isIosEnabled()) {
			com.wearezeta.auto.ios.pages.PagesCollection.loginPage.Close();
			com.wearezeta.auto.ios.pages.IOSPage.clearPagesCollection();
			com.wearezeta.auto.ios.tools.IOSKeyboard.dispose();
		}
	}

	@Given("I start sync engine test")
	public void IStartSyncEngineTest() throws Exception {
		log.debug("Starting sync engine");
		ExecutorService executor = Executors.newFixedThreadPool(10);

		int i = 0;
		for (String platform : SyncEngineUtil.platforms) {
			ZetaInstance client = ExecutionContext.clients.get(platform);
			if (client.isEnabled()) {
				log.debug("Init client for platform: " + platform);
				client.setUserInstance(SyncEngineUtil.usersList.get(i++));
				executor.execute(client.sender());
				executor.execute(client.listener());
			} else {
				log.debug("Client for platform " + platform
						+ " is skipped. SKIPPED");
			}
			ExecutionContext.clients.put(platform, client);
		}

		executor.shutdown();
		if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
			throw new Exception("Work was not finished in useful time.");
		}
	}
	
	@Given("I run serial sync engine test")
	public void IRunSerialSyncEngineTest() throws InterruptedException, Exception {
		//send ios, receive osx and android
		if (ExecutionContext.isIosEnabled()) {
			for (int i = 0; i < ExecutionContext.iosZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.iosZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.osxZeta().listener().waitForMessageOsx(message);
					}
				});
				}
				if (ExecutionContext.isAndroidEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.androidZeta().listener().waitForMessageAndroid(message);
					}
				});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				try {
					Thread.sleep(ExecutionContext.iosZeta().getMessagesSendingInterval()*1000);
				} catch (InterruptedException e) {
				}
			}
		}
		
		//send osx, receive ios and android
		if (ExecutionContext.isOsxEnabled()) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.osxZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isIosEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.iosZeta().listener().waitForMessageIos(message);
					}
				});
				}
				if (ExecutionContext.isAndroidEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.androidZeta().listener().waitForMessageAndroid(message);
					}
				});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				try {
					Thread.sleep(ExecutionContext.osxZeta().getMessagesSendingInterval()*1000);
				} catch (InterruptedException e) {
				}
				
			}
		}
		
		//send android, receive ios and osx
		if (ExecutionContext.isAndroidEnabled()) {
			for (int i = 0; i < ExecutionContext.androidZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.androidZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.osxZeta().listener().waitForMessageOsx(message);
					}
				});
				}
				if (ExecutionContext.isIosEnabled()) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.iosZeta().listener().waitForMessageIos(message);
					}
				});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				try {
					Thread.sleep(ExecutionContext.androidZeta().getMessagesSendingInterval()*1000);
				} catch (InterruptedException e) {
				}
				
			}
		}
	}
	
	@Given("I collect messages order data")
	public void ICollectMessagesOrderData() {
		ArrayList<MessageEntry> iosMessages = ExecutionContext.iosZeta().listener().receiveChatMessages();
		ArrayList<MessageEntry> osxMessages = ExecutionContext.osxZeta().listener().receiveChatMessages();
		ArrayList<MessageEntry> androidMessages = ExecutionContext.androidZeta().listener().receiveChatMessages();
		
		ArrayList<MessageEntry> sentMessages = new ArrayList<MessageEntry>(ExecutionContext.sentMessages.values());
		
		boolean iosOrderCorrect = true;
		boolean osxOrderCorrect = true;
		boolean androidOrderCorrect = true;
		
		for (int i = 0; i < sentMessages.size(); i++) {
			try {
				if (!sentMessages.get(i).messageContent.equals(osxMessages.get(i).messageContent))
					osxOrderCorrect = false;
			} catch (Exception e) {
				osxOrderCorrect = false;
				log.debug(sentMessages);
				log.debug(osxMessages);
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(iosMessages.get(i).messageContent))
					iosOrderCorrect = false;
			} catch (Exception e) {
				iosOrderCorrect = false;
				log.debug(sentMessages);
				log.debug(iosMessages);
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(androidMessages.get(i).messageContent))
					androidOrderCorrect = false;
			} catch (Exception e) {
				androidOrderCorrect = false;
				log.debug(sentMessages);
				log.debug(androidMessages);
			}
		}
		
		ExecutionContext.iosZeta().setOrderCorrect(iosOrderCorrect);
		ExecutionContext.osxZeta().setOrderCorrect(osxOrderCorrect);
		ExecutionContext.androidZeta().setOrderCorrect(androidOrderCorrect);
	}
	
	@Given("I build results report")
	public void IBuildResultsReport() throws IOException {
		ExecutionContext.report.fillReportInfo();
		log.debug(ReportData.toXml(ExecutionContext.report));
		ReportGenerator.generate(ExecutionContext.report);
	}

	@Then("I perform acceptance checks")
	public void IPerformAcceptanceChecks() {
		boolean isTestPassed = true;
		String comment = "Acceptance checks results:\n";
		
		if (!ExecutionContext.report.areClientsStable) {
			isTestPassed = false;
			comment += "\tClients stability check - failed\n";
		} else {
			comment += "\tClients stability check - passed\n";
		}
		
		if (!ExecutionContext.report.areMessagesReceived) {
			isTestPassed = false;
			comment += "\tMessages receiving check - failed\n";
		} else {
			comment += "\tMessages receiving check - passed\n";
		}
		
		if (!ExecutionContext.report.areMessagesReceiveTimeCorrect) {
			isTestPassed = false;
			comment += "\tMessages receive time check - failed\n";
		} else {
			comment += "\tMessages receive time check - passed\n";
		}
		
		if (!ExecutionContext.report.areMessagesOrderCorrect) {
			isTestPassed = false;
			comment += "\tMessages order check - failed\n";
		} else {
			comment += "\tMessages order check - passed\n";
		}
		
		Assert.assertTrue(comment, isTestPassed);
	}
}
