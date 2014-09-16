package com.wearezeta.auto.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.sync.client.ZetaInstance;
import com.wearezeta.auto.sync.client.ZetaSender;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.ReportGenerator;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

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
				PagesCollection.loginPage.dismissUpdate();
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
	public void IRunSerialSyncEngineTest() {
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
				try {
					Thread.sleep(ExecutionContext.androidZeta().getMessagesSendingInterval()*1000);
				} catch (InterruptedException e) {
				}
				
			}
		}
	}
	
	@Given("I analyze registered data")
	public void IAnalyzeRegisteredData() {
		ReportGenerator.chatUsers = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			ReportGenerator.chatUsers.add(SyncEngineUtil.usersList.get(i)
					.getName());
		}

		ReportGenerator.loggedInUsers = new HashMap<String, String>();
		ReportGenerator.startupTimes = new HashMap<String, Long>();

		for (Map.Entry<String, ZetaInstance> client : ExecutionContext.clients
				.entrySet()) {
			if (client.getValue().isEnabled()) {
				ReportGenerator.loggedInUsers.put(client.getKey(), client.getValue().getUserInstance().getName());
				ReportGenerator.startupTimes.put(client.getKey(), client.getValue().getStartupTimeMs());
			} else {
				ReportGenerator.loggedInUsers.put(client.getKey(),
						"-==DISABLED==-");
			}
		}

		ReportGenerator.sentMessages = ExecutionContext.sentMessages;
		if (ExecutionContext.iosZeta().isEnabled()) {
			ReportGenerator.iosReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.iosZeta().listener().registeredMessages.entrySet()) {
				log.debug(message);
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {

					ReportGenerator.iosReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
		}
		
		if (ExecutionContext.androidZeta().isEnabled()) {
			ReportGenerator.androidReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.androidZeta().listener().registeredMessages.entrySet()) {
				log.debug(message);
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {
					ReportGenerator.androidReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
		}
		if (ExecutionContext.osxZeta().isEnabled()) {
			ReportGenerator.osxReceivedMessages = new LinkedHashMap<String, MessageEntry>();
			for (Map.Entry<String, MessageEntry> message : ExecutionContext
					.osxZeta().listener().registeredMessages.entrySet()) {
				log.debug(message);
				if (message.getValue().appearanceDate
						.after(ZetaSender.sendingStartDate)) {

					ReportGenerator.osxReceivedMessages.put(message.getKey(), message.getValue());
				}
			}
		}
		log.debug(ReportGenerator.generate());
	}

}
