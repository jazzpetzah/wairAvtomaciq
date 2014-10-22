package com.wearezeta.auto.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.sync.client.InstanceState;
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
		OSXCommonUtils.removeAllZClientSettingsFromDefaults();
		
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

	private void startOsxClient(ExecutorService executor) throws IOException {
		final String osxPath = CommonUtils
				.getOsxApplicationPathFromConfig(this.getClass());
		final String osxAppiumUrl = CommonUtils
				.getOsxAppiumUrlFromConfig(this.getClass());
		executor.execute(new Runnable() {
			public void run() {
				com.wearezeta.auto.osx.steps.CommonSteps.senderPages = new com.wearezeta.auto.osx.pages.PagesCollection();
				com.wearezeta.auto.osx.pages.PagesCollection osxSenderPages = com.wearezeta.auto.osx.steps.CommonSteps.senderPages;

				long startDate = new Date().getTime();
				try {
					osxSenderPages
							.setMainMenuPage(new com.wearezeta.auto.osx.pages.MainMenuPage(
									osxAppiumUrl, osxPath));
					osxSenderPages
							.setLoginPage(new com.wearezeta.auto.osx.pages.LoginPage(
									osxAppiumUrl, osxPath));
				} catch (MalformedURLException e) {
				}
				long endDate = new Date().getTime();
				ExecutionContext.osxZeta().setStartupTimeMs(
						endDate - startDate);
				log.debug("OSX application startup time: "
						+ ExecutionContext.osxZeta().getStartupTimeMs()
						+ "ms");
				ZetaFormatter.setDriver(osxSenderPages.getLoginPage().getDriver());
				osxSenderPages.getLoginPage().sendProblemReportIfFound();
			}
		});
	}
	
	private void startAndroidClient(ExecutorService executor) throws IOException {
		try {
			AndroidCommonUtils.disableHints();
		} catch (Exception e) {
			log.fatal("Failed to create disable hints config.\n"
					+ e.getMessage());
			e.printStackTrace();
		}
		final String androidPath = CommonUtils
				.getAndroidApplicationPathFromConfig(this.getClass());
		final String androidAppiumUrl = CommonUtils
				.getAndroidAppiumUrlFromConfig(this.getClass());
		executor.execute(new Runnable() {
			public void run() {
				if (com.wearezeta.auto.android.pages.PagesCollection.loginPage == null) {
					long startDate = new Date().getTime();
					try {
						com.wearezeta.auto.android.pages.PagesCollection.loginPage = new com.wearezeta.auto.android.pages.LoginPage(
								androidAppiumUrl, androidPath);
						com.wearezeta.auto.android.pages.PagesCollection.loginPage.isVisible();
					} catch (Exception e) {
					}
					long endDate = new Date().getTime();
					try {
						startDate = readDateFromAppiumLog(AndroidCommonUtils.getAndroidAppiumLogPathFromConfig(CommonSteps.class));
					} catch (IOException e) {
						log.error("Failed to read Android application startup time from Appium log.\n"
								+ "Approximate value will be used. "
								+ e.getMessage());
					}
					ExecutionContext.androidZeta().setStartupTimeMs(
							endDate - startDate);
					log.debug("Android application startup time: "
							+ ExecutionContext.androidZeta()
									.getStartupTimeMs() + "ms");
				}
			}
		});
	}
	
	private void startIosClient(ExecutorService executor) throws IOException {
		final String iosPath = CommonUtils
				.getIosApplicationPathFromConfig(this.getClass());
		final String iosAppiumPath = CommonUtils
				.getIosAppiumUrlFromConfig(this.getClass());

		executor.execute(new Runnable() {
			public void run() {
				if (com.wearezeta.auto.ios.pages.PagesCollection.loginPage == null) {
					long startDate = new Date().getTime();
					try {
						if (CommonUtils.getIsSimulatorFromConfig(CommonSteps.class)) {
							com.wearezeta.auto.ios.pages.PagesCollection.loginPage = new com.wearezeta.auto.ios.pages.LoginPage(
									iosAppiumPath, iosPath, true);
						} else {
							com.wearezeta.auto.ios.pages.PagesCollection.loginPage = new com.wearezeta.auto.ios.pages.LoginPage(
									iosAppiumPath, iosPath, false);
						}
						com.wearezeta.auto.ios.pages.PagesCollection.loginPage.isLoginButtonVisible();
					} catch (MalformedURLException e) {
					} catch (IOException e) { }
					
					long endDate = new Date().getTime();
					try {
						startDate = readDateFromAppiumLog(IOSCommonUtils.getIosAppiumLogPathFromConfig(CommonSteps.class));
					} catch (IOException e) {
						log.error("Failed to read iOS application startup time from Appium log.\n"
								+ "Approximate value will be used. "
								+ e.getMessage());
					}
					ExecutionContext.iosZeta().setStartupTimeMs(
							endDate - startDate);
					log.debug("iOS application startup time: "
							+ ExecutionContext.iosZeta().getStartupTimeMs()
							+ "ms");
					try {
						com.wearezeta.auto.ios.pages.PagesCollection.loginPage.ignoreUpdate();
					} catch (NoSuchElementException e) {
						log.debug("No update notification.");
					}
				}
			}
		});
	}
	
	private static final String LOG_STRING_WITH_LAUNCH_TIME = "Application launch is started at ([0-9]*)";
	private long readDateFromAppiumLog(String logFile) throws IOException {
		long result = -1;
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File f = new File(logFile);
			fileReader = new FileReader(f);
			bufferedReader = new BufferedReader(fileReader);
			String s;
			Pattern pattern = Pattern.compile(LOG_STRING_WITH_LAUNCH_TIME);
			Matcher matcher;
			while ( ( s = bufferedReader.readLine() ) != null ) {
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					result = Long.parseLong(matcher.group(1));
					break;
				}
			}
		} catch (IOException e) {
			log.error("Failed to read appium.log for launch time.\n" + e.getMessage());
		} finally {
			if (bufferedReader != null) bufferedReader.close();
			if (fileReader != null) fileReader.close();
		}
		return result;
	}
	
	@Given("I start all platform clients")
	public void IStartPlatformClients() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(3);

		// OSX initialization
		if (ExecutionContext.isOsxEnabled()) {
			startOsxClient(executor);
		}

		// Android initialization
		if (ExecutionContext.isAndroidEnabled()) {
			startAndroidClient(executor);
		}

		// iOS initialization
		if (ExecutionContext.isIosEnabled()) {
			startIosClient(executor);
		}
		executor.shutdown();
		if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
			throw new Exception(
					"Clients startup was not finished in useful time.");
		}
	}

	@Given("I sign in to all platform clients and go to conversation (.*)")
	public void ISignInToAllPlatformClients(String conversation)
			throws InterruptedException, Exception {
		ExecutorService executor = Executors.newFixedThreadPool(3);

		final OSXClientSteps osxSteps = new OSXClientSteps();
		final AndroidClientSteps androidSteps = new AndroidClientSteps();
		final IOSClientSteps iosSteps = new IOSClientSteps();

		final String conversationName = conversation;

		executor.execute(new Runnable() {
			public void run() {
				try {
					osxSteps.OSXISignInUsingLoginAndPassword("1stUser",
							"usersPassword");
					osxSteps.OSXIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.fatal("OSX client crashed during login and opening conversation.");
					ExecutionContext.osxZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					androidSteps.AndroidISignInUsingLoginAndPassword("2ndUser",
							"usersPassword");
					androidSteps.AndroidIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.fatal("Android client crashed during login and opening conversation.");
					ExecutionContext.androidZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					iosSteps.IOSISignInUsingLoginAndPassword("3rdUser",
							"usersPassword");
					iosSteps.IOSIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.fatal("iOS client crashed during login and opening conversation.");
					ExecutionContext.iosZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
			}
		});
		executor.shutdown();
		if (!executor.awaitTermination(20, TimeUnit.MINUTES)) {
			throw new Exception(
					"Clients sign in was not finished in 20 minutes.");
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

	private LinkedHashMap<Date, String> iosPageSources = new LinkedHashMap<Date, String>();
	private LinkedHashMap<Date, String> osxPageSources = new LinkedHashMap<Date, String>();
	
	private void storeIosPageSource() {
		if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			try {
				ExecutionContext.iosZeta().listener().scrollToTheEndOfConversation();
			} catch (NoSuchElementException e) {
				log.error("Failed to get iOS page source. Client could be crashed.");
				if (ExecutionContext.iosZeta().listener().isSessionLost()) {
					log.error("Session lost on iOS client. No checks for next time.");
					ExecutionContext.iosZeta().setState(InstanceState.ERROR_CRASHED);
				}
			}
			String iosSource = ExecutionContext.iosZeta().listener().getChatSource();
			iosPageSources.put(new Date(), iosSource);
		}
	}
	
	private void storeOsxPageSource() {
		if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			osxPageSources.put(new Date(), ExecutionContext.osxZeta().listener().getChatSource());
		}
	}
	
	@Given("I run serial sync engine test")
	public void IRunSerialSyncEngineTest() throws InterruptedException, Exception {
		//send ios, receive osx and android
		if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.iosZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.iosZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.osxZeta().listener().waitForMessageOsx(message, true);
						}
					});
				}
				if (ExecutionContext.isAndroidEnabled() && ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.androidZeta().listener().waitForMessageAndroid(message, true);
						}
					});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				log.debug("Sent message #" + i + " from iOS client");
				if (ExecutionContext.iosZeta().getMessagesSendingInterval() > 0) {
					try {
						Thread.sleep(ExecutionContext.iosZeta().getMessagesSendingInterval()*1000);
					} catch (InterruptedException e) { }
				}
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		//fast sending of messages from iOS
		if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.iosZeta().getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext.iosZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message, false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i + " from iOS: "
						+ (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		//send osx, receive ios and android
		if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.osxZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
				executor.execute(new Runnable() {
					public void run() {	
						try {
							ExecutionContext.iosZeta().listener().waitForMessageIos(message, true);
						} catch (NoSuchElementException e) {
							log.error("Failed to receive message on iOS client. Client could be crashed.");
							if (ExecutionContext.iosZeta().listener().isSessionLost()) {
								log.error("Session lost on iOS client. No checks for next time.");
								ExecutionContext.iosZeta().setState(InstanceState.ERROR_CRASHED);
							}
						
						}
					}
				});
				}
				if (ExecutionContext.isAndroidEnabled() && ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.androidZeta().listener().waitForMessageAndroid(message, true);
						}
					});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				log.debug("Sent message #" + i + " from OSX client");
				if (ExecutionContext.osxZeta().getMessagesSendingInterval() > 0) {
					try {
						Thread.sleep(ExecutionContext.osxZeta().getMessagesSendingInterval()*1000);
					} catch (InterruptedException e) { }
				}
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		//fast sending of messages from OSX
		if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext.osxZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message, false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i + " from OSX: "
						+ (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		//send android, receive ios and osx
		if (ExecutionContext.isAndroidEnabled() && ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.androidZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.androidZeta().sender().sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
				executor.execute(new Runnable() {
					public void run() {
						ExecutionContext.osxZeta().listener().waitForMessageOsx(message, true);
					}
				});
				}
				if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
				executor.execute(new Runnable() {
					public void run() {
						try {
							ExecutionContext.iosZeta().listener().waitForMessageIos(message, true);
						} catch (NoSuchElementException e) {
							log.error("Failed to receive message on iOS client. Client could be crashed.");
							if (ExecutionContext.iosZeta().listener().isSessionLost()) {
								log.error("Session lost on iOS client. No checks for next time.");
								ExecutionContext.iosZeta().setState(InstanceState.ERROR_CRASHED);
							}
							
						}
					}
				});
				}
				executor.shutdown();
				if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
					throw new Exception("Work was not finished in useful time.");
				}
				log.debug("Sent message #" + i + " from Android client");
				if (ExecutionContext.androidZeta().getMessagesSendingInterval() > 0) {
					try {
						Thread.sleep(ExecutionContext.androidZeta().getMessagesSendingInterval()*1000);
					} catch (InterruptedException e) { }
				}
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		//fast sending of messages from Android
		if (ExecutionContext.isAndroidEnabled() && ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.androidZeta()
					.getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext.androidZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message, false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i + " from Android: "
						+ (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
	}
	
	@Given("I run fast sync engine test")
	public void IRunFastSyncEngineTest() throws Exception {
		// start sending message
		if (ExecutionContext.isIosEnabled() && ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			com.wearezeta.auto.ios.pages.DialogPage page = com.wearezeta.auto.ios.pages.PagesCollection.dialogPage;
			final String messages[] = new String[ExecutionContext.iosZeta().getMessagesToSend()];
			for (int j = 0; j < messages.length; j++) {
				messages[j] = CommonUtils.generateGUID();
			}
			long startDate = new Date().getTime();
			ExecutionContext.iosZeta().sender()
					.sendAllMessagesIos(messages, false);
			long endDate = new Date().getTime();
			log.debug("Time consumed for sending all text messages on ios: "
				+ (endDate - startDate) + "ms");
			
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		if (ExecutionContext.isOsxEnabled() && ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext.osxZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message, false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message on osx: "
						+ (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
		
		if (ExecutionContext.isAndroidEnabled() && ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.androidZeta()
					.getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext.androidZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message, false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message on android: "
						+ (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource();
		storeOsxPageSource();
	}
	
	@Given("I collect messages order data")
	public void ICollectMessagesOrderData() {
		ExecutionContext.iosZeta().listener().setPageSources(iosPageSources);
		ExecutionContext.osxZeta().listener().setPageSources(osxPageSources);

		ArrayList<MessageEntry> iosMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isIosEnabled()) {
			iosMessages = ExecutionContext.iosZeta().listener().receiveChatMessages(false);
		}
		
		ArrayList<MessageEntry> osxMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isOsxEnabled()) {
			osxMessages = ExecutionContext.osxZeta().listener().receiveChatMessages(false);
		}
		
		ArrayList<MessageEntry> androidMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isAndroidEnabled()) {
			androidMessages = ExecutionContext.androidZeta().listener().receiveChatMessages(false);
		}
		
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
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(iosMessages.get(i).messageContent))
					iosOrderCorrect = false;
			} catch (Exception e) {
				iosOrderCorrect = false;
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(androidMessages.get(i).messageContent))
					androidOrderCorrect = false;
			} catch (Exception e) {
				androidOrderCorrect = false;
			}
		}
		
		ExecutionContext.iosZeta().setMessagesListAfterTest(iosMessages);
		ExecutionContext.osxZeta().setMessagesListAfterTest(osxMessages);
		ExecutionContext.androidZeta().setMessagesListAfterTest(androidMessages);

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

	@Then("I collect builds and devices info")
	public void ICollectBuildsAndDevicesInfo() {
		if (ExecutionContext.isAndroidEnabled()) {
			BuildVersionInfo androidClientInfo = new BuildVersionInfo();
			try {
				androidClientInfo = AndroidCommonUtils.readClientVersion();
			} catch (InterruptedException ex) {
				log.error(ex.getMessage());
			} catch (IOException ioex) {
				log.error(ioex.getMessage());
			}
			ExecutionContext.androidZeta().setVersionInfo(androidClientInfo);
			
			ClientDeviceInfo androidDeviceInfo = null;
		}
		
		if (ExecutionContext.isIosEnabled()) {
			BuildVersionInfo iosClientInfo = IOSCommonUtils.readClientVersionFromPlist();
			ExecutionContext.iosZeta().setVersionInfo(iosClientInfo);
		}
		
		if (ExecutionContext.isOsxEnabled()) {
			BuildVersionInfo osxClientInfo = OSXCommonUtils.readClientVersionFromPlist();
			ExecutionContext.osxZeta().setVersionInfo(osxClientInfo);
		}
		
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
