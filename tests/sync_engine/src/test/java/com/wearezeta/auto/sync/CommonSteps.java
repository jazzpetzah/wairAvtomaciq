package com.wearezeta.auto.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.CommonAndroidSteps;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.LoginPage;
import com.wearezeta.auto.android.pages.PagesCollection;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.CommonIOSSteps;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
import com.wearezeta.auto.osx.steps.CommonOSXSteps;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.WireInstance;
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

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Before
	public void setup() throws Exception {
	}

	@After
	public void teardown() throws Exception {
		// osx teardown
		if (ExecutionContext.isOsxEnabled()) {
			com.wearezeta.auto.osx.pages.PagesCollection.loginPage.close();
			com.wearezeta.auto.osx.pages.OSXPage.clearPagesCollection();
		}

		// android teardown
		if (ExecutionContext.isAndroidEnabled()) {
			com.wearezeta.auto.android.pages.PagesCollection.loginPage.close();
			com.wearezeta.auto.android.pages.AndroidPage.clearPagesCollection();
		}

		// ios teardown
		if (ExecutionContext.isIosEnabled()) {
			com.wearezeta.auto.ios.pages.PagesCollection.loginPage.close();
			com.wearezeta.auto.ios.pages.IOSPage.clearPagesCollection();
			com.wearezeta.auto.ios.tools.IOSKeyboard.dispose();
		}
	}

	@Given("^(.*) has group chat (.*) with (.*)$")
	public void UserHasGroupChatWithContacts(String chatOwnerNameAlias,
			String chatName, String otherParticipantsNameAlises)
			throws Exception {
		com.wearezeta.auto.common.CommonSteps.getInstance()
				.UserHasGroupChatWithContacts(chatOwnerNameAlias, chatName,
						otherParticipantsNameAlises);
	}

	@Given("^(.*) is connected to (.*)$")
	public void UserIsConnectedTo(String userFromNameAlias,
			String usersToNameAliases) throws Exception {
		com.wearezeta.auto.common.CommonSteps.getInstance().UserIsConnectedTo(
				userFromNameAlias, usersToNameAliases);
	}

	@Given("^There \\w+ (\\d+) user[s]* where (.*) is me$")
	public void ThereAreNUsersWhereXIsMe(int count, String myNameAlias)
			throws Exception {
		com.wearezeta.auto.common.CommonSteps.getInstance()
				.ThereAreNUsersWhereXIsMe(count, myNameAlias);
	}

	private void startOsxClient(ExecutorService executor) throws Exception {
		executor.execute(new Runnable() {
			public void run() {
				CommonOSXSteps osxSteps = new CommonOSXSteps();
				try {
					osxSteps.setUp();
				} catch (Exception e) {
					log.debug("Failed to start OSX client. Error message: "
							+ e.getMessage());
					e.printStackTrace();
				}
				ExecutionContext.osxZeta().setStartupTimeMs(
						osxSteps.startupTime);
				log.debug("OSX application startup time: "
						+ ExecutionContext.osxZeta().getStartupTimeMs() + "ms");
			}
		});
	}

	private void startAndroidClient(ExecutorService executor) throws Exception {
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
					CommonAndroidSteps androidSteps = new CommonAndroidSteps();
					long startDate = new Date().getTime();
					long endDate = 0;
					try {
						androidSteps.commonBefore();
						final ZetaAndroidDriver driver = androidSteps
								.resetAndroidDriver(androidAppiumUrl,
										androidPath, false, this.getClass());
						final WebDriverWait wait = PlatformDrivers
								.createDefaultExplicitWait(driver);
						PagesCollection.loginPage = new LoginPage(driver, wait);
						endDate = new Date().getTime();
						ZetaFormatter.setDriver(PagesCollection.loginPage
								.getDriver());
					} catch (Exception e) {
						log.debug("Failed to start Android client. Error message: "
								+ e.getMessage());
						e.printStackTrace();
					}

					try {
						startDate = readDateFromAppiumLog(AndroidCommonUtils
								.getAndroidAppiumLogPathFromConfig(CommonSteps.class));
					} catch (Exception e) {
						log.error("Failed to read Android application startup time from Appium log.\n"
								+ "Approximate value will be used. "
								+ e.getMessage());
					}
					ExecutionContext.androidZeta().setStartupTimeMs(
							endDate - startDate);

					log.debug("Android application startup time: "
							+ ExecutionContext.androidZeta().getStartupTimeMs()
							+ "ms");
				}
			}
		});
	}

	private void startIosClient(ExecutorService executor) throws Exception {
		executor.execute(new Runnable() {
			public void run() {
				if (com.wearezeta.auto.ios.pages.PagesCollection.loginPage == null) {
					CommonIOSSteps iosSteps = new CommonIOSSteps();
					long startDate = new Date().getTime();
					try {
						iosSteps.setUpAcceptAlerts();
					} catch (Exception e) {
						log.debug("Failed to start iOS client. Error message: "
								+ e.getMessage());
						e.printStackTrace();
					}
					long endDate = new Date().getTime();
					try {
						startDate = readDateFromAppiumLog(IOSCommonUtils
								.getIosAppiumLogPathFromConfig(CommonSteps.class));
					} catch (Exception e) {
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
						com.wearezeta.auto.ios.pages.PagesCollection.loginPage
								.ignoreUpdate();
					} catch (Exception e) {
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
			while ((s = bufferedReader.readLine()) != null) {
				matcher = pattern.matcher(s);
				if (matcher.find()) {
					result = Long.parseLong(matcher.group(1));
					break;
				}
			}
		} catch (IOException e) {
			log.error("Failed to read appium.log for launch time.\n"
					+ e.getMessage());
		} finally {
			if (bufferedReader != null)
				bufferedReader.close();
			if (fileReader != null)
				fileReader.close();
		}
		return result;
	}

	@Given("I start all platform clients")
	public void IStartPlatformClients() throws Exception {
		// init platform clients
		int i = 0;

		ArrayList<ClientUser> usersList = new ArrayList<ClientUser>(
				usrMgr.getCreatedUsers());
		for (Platform platform : SyncEngineUtil.platforms) {
			WireInstance client = WireInstance.getInstance(platform);
			client.setUserInstance(usersList.get(i++));
			ExecutionContext.clients.put(platform, client);
		}

		ExecutorService executor = Executors.newFixedThreadPool(1);

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
					osxSteps.OSXISignInUsingLoginAndPassword(
							SyncEngineUtil.OSX_NAME_ALIAS,
							SyncEngineUtil.PASSWORD_ALIAS);
					osxSteps.OSXIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.fatal("OSX client crashed during login and opening conversation.\n"
							+ e.getMessage());
					ExecutionContext.osxZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
				log.debug("OSX client login finished.");
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					androidSteps.AndroidISignInUsingLoginAndPassword(
							SyncEngineUtil.ANDROID_NAME_ALIAS,
							SyncEngineUtil.PASSWORD_ALIAS);
					androidSteps.AndroidIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.fatal("Android client crashed during login and opening conversation.\n"
							+ e.getMessage());
					e.printStackTrace();
					ExecutionContext.androidZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
				log.debug("Android client login finished.");
			}
		});
		executor.execute(new Runnable() {
			public void run() {
				try {
					iosSteps.IOSISignInUsingLoginAndPassword(
							SyncEngineUtil.IOS_NAME_ALIAS,
							SyncEngineUtil.PASSWORD_ALIAS);
					iosSteps.IOSIOpenConversationWith(conversationName);
				} catch (Throwable e) {
					log.error("iOS client crashed during login and opening conversation.\n"
							+ e.getMessage());
					e.printStackTrace();
					ExecutionContext.iosZeta().setState(
							InstanceState.ERROR_CRASHED);
					// TODO: process crash
				}
				log.debug("iOS client login finished.");
			}
		});
		executor.shutdown();
		if (!executor.awaitTermination(20, TimeUnit.MINUTES)) {
			throw new Exception(
					"Clients sign in was not finished in 20 minutes.");
		}
	}

	private LinkedHashMap<Date, String> iosPageSources = new LinkedHashMap<Date, String>();
	private LinkedHashMap<Date, String> osxPageSources = new LinkedHashMap<Date, String>();

	private void storeIosPageSource(boolean last) {
		if (ExecutionContext.isIosEnabled()
				&& ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			if (!last) {
				try {
					ExecutionContext.iosZeta().listener()
							.scrollToTheEndOfConversation();
				} catch (NoSuchElementException e) {
					log.error("Failed to get iOS page source. Client could be crashed.\n"
							+ e.getMessage());
					e.printStackTrace();
					if (ExecutionContext.iosZeta().listener().isSessionLost()) {
						log.error("Session lost on iOS client. No checks for next time.");
						ExecutionContext.iosZeta().setState(
								InstanceState.ERROR_CRASHED);
					}
				}
			}
			String iosSource = ExecutionContext.iosZeta().listener()
					.getChatSource();
			iosPageSources.put(new Date(), iosSource);
		}
	}

	private void storeOsxPageSource() {
		if (ExecutionContext.isOsxEnabled()
				&& ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			String chatSource = ExecutionContext.osxZeta().listener()
					.getChatSource();
			osxPageSources.put(new Date(), chatSource);
		}
	}

	@Given("I run serial sync engine test")
	public void IRunSerialSyncEngineTest() throws InterruptedException,
			Exception {
		// send ios, receive osx and android
		if (ExecutionContext.isIosEnabled()
				&& ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.iosZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.iosZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled()
						&& ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.osxZeta().listener()
									.waitForMessage(message, true);
						}
					});
				}
				if (ExecutionContext.isAndroidEnabled()
						&& ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.androidZeta().listener()
									.waitForMessage(message, true);
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
						Thread.sleep(ExecutionContext.iosZeta()
								.getMessagesSendingInterval() * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		storeIosPageSource(false);
		storeOsxPageSource();

		// fast sending of messages from iOS
		if (ExecutionContext.isIosEnabled()
				&& ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.iosZeta().getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext
						.iosZeta()
						.sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message,
								false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i
						+ " from iOS: " + (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource(false);
		storeOsxPageSource();

		// send osx, receive ios and android
		if (ExecutionContext.isOsxEnabled()
				&& ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.osxZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isIosEnabled()
						&& ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							try {
								ExecutionContext.iosZeta().listener()
										.waitForMessage(message, true);
							} catch (NoSuchElementException e) {
								log.error("iOS: Wait for message sent from OS X finished with incorrect result.\n"
										+ e.getMessage());
								e.printStackTrace();
								if (ExecutionContext.iosZeta().listener()
										.isSessionLost()) {
									log.error("Session lost on iOS client. No checks for next time.");
									ExecutionContext.iosZeta().setState(
											InstanceState.ERROR_CRASHED);
								}

							}
						}
					});
				}
				if (ExecutionContext.isAndroidEnabled()
						&& ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.androidZeta().listener()
									.waitForMessage(message, true);
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
						Thread.sleep(ExecutionContext.osxZeta()
								.getMessagesSendingInterval() * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		storeIosPageSource(false);
		storeOsxPageSource();

		// send android, receive ios and osx
		if (ExecutionContext.isAndroidEnabled()
				&& ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.androidZeta()
					.getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				ExecutionContext.androidZeta().sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message);
				ExecutorService executor = Executors.newFixedThreadPool(2);
				if (ExecutionContext.isOsxEnabled()
						&& ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							ExecutionContext.osxZeta().listener()
									.waitForMessage(message, true);
						}
					});
				}
				if (ExecutionContext.isIosEnabled()
						&& ExecutionContext.iosZeta().getState() != InstanceState.ERROR_CRASHED) {
					executor.execute(new Runnable() {
						public void run() {
							try {
								ExecutionContext.iosZeta().listener()
										.waitForMessage(message, true);
							} catch (NoSuchElementException e) {
								log.error("iOS: Wait for message sent from Android finished with incorrect result.\n"
										+ e.getMessage());
								e.printStackTrace();
								if (ExecutionContext.iosZeta().listener()
										.isSessionLost()) {
									log.error("Session lost on iOS client. No checks for next time.");
									ExecutionContext.iosZeta().setState(
											InstanceState.ERROR_CRASHED);
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
						Thread.sleep(ExecutionContext.androidZeta()
								.getMessagesSendingInterval() * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
		storeIosPageSource(false);
		storeOsxPageSource();

		// fast sending of messages from OSX
		if (ExecutionContext.isOsxEnabled()
				&& ExecutionContext.osxZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.osxZeta().getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext
						.osxZeta()
						.sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message,
								false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i
						+ " from OSX: " + (endDate - startDate) + "ms");
				Thread.sleep(1000);
			}
		}
		storeIosPageSource(false);
		storeOsxPageSource();

		// fast sending of messages from Android
		if (ExecutionContext.isAndroidEnabled()
				&& ExecutionContext.androidZeta().getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < ExecutionContext.androidZeta()
					.getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				ExecutionContext
						.androidZeta()
						.sender()
						.sendTextMessage(SyncEngineUtil.CHAT_NAME, message,
								false);
				long endDate = new Date().getTime();
				log.debug("Time consumed for sending text message #" + i
						+ " from Android: " + (endDate - startDate) + "ms");
			}
		}
		storeIosPageSource(true);
		storeOsxPageSource();
	}

	@Given("I collect messages order data")
	public void ICollectMessagesOrderData() {
		if (ExecutionContext.iosZeta().isEnabled()) {
			ExecutionContext.iosZeta().listener()
					.setPageSources(iosPageSources);
		}

		if (ExecutionContext.osxZeta().isEnabled()) {
			ExecutionContext.osxZeta().listener()
					.setPageSources(osxPageSources);
		}

		ArrayList<MessageEntry> iosMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isIosEnabled()) {
			iosMessages = ExecutionContext.iosZeta().listener()
					.receiveAllChatMessages(false);
		}

		ArrayList<MessageEntry> osxMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isOsxEnabled()) {
			osxMessages = ExecutionContext.osxZeta().listener()
					.receiveAllChatMessages(false);
		}

		ArrayList<MessageEntry> androidMessages = new ArrayList<MessageEntry>();
		if (ExecutionContext.isAndroidEnabled()) {
			androidMessages = ExecutionContext.androidZeta().listener()
					.receiveAllChatMessages(false);
		}

		ArrayList<MessageEntry> sentMessages = new ArrayList<MessageEntry>(
				ExecutionContext.sentMessages.values());

		boolean iosOrderCorrect = true;
		boolean osxOrderCorrect = true;
		boolean androidOrderCorrect = true;

		for (int i = 0; i < sentMessages.size(); i++) {
			try {
				if (!sentMessages.get(i).messageContent.equals(osxMessages
						.get(i).messageContent))
					osxOrderCorrect = false;
			} catch (Exception e) {
				osxOrderCorrect = false;
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(iosMessages
						.get(i).messageContent))
					iosOrderCorrect = false;
			} catch (Exception e) {
				iosOrderCorrect = false;
			}
			try {
				if (!sentMessages.get(i).messageContent.equals(androidMessages
						.get(i).messageContent))
					androidOrderCorrect = false;
			} catch (Exception e) {
				androidOrderCorrect = false;
			}
		}

		ExecutionContext.iosZeta().setMessagesListAfterTest(iosMessages);
		ExecutionContext.osxZeta().setMessagesListAfterTest(osxMessages);
		ExecutionContext.androidZeta()
				.setMessagesListAfterTest(androidMessages);

		ExecutionContext.iosZeta().setOrderCorrect(iosOrderCorrect);
		ExecutionContext.osxZeta().setOrderCorrect(osxOrderCorrect);
		ExecutionContext.androidZeta().setOrderCorrect(androidOrderCorrect);
	}

	@Given("I build results report")
	public void IBuildResultsReport() throws Exception {
		ExecutionContext.report.fillReportInfo();
		log.debug(ReportData.toXml(ExecutionContext.report));
		ReportGenerator.generate(ExecutionContext.report);
	}

	@Then("I collect builds and devices info")
	public void ICollectBuildsAndDevicesInfo() {
		if (ExecutionContext.isAndroidEnabled()) {
			BuildVersionInfo androidClientInfo = new BuildVersionInfo();
			ClientDeviceInfo androidDeviceInfo = new ClientDeviceInfo();
			try {
				androidClientInfo = AndroidCommonUtils.readClientVersion();
				androidDeviceInfo = AndroidCommonUtils.readDeviceInfo();
				log.debug("Following info were taken from android device: "
						+ androidDeviceInfo);
			} catch (InterruptedException iex) {
				log.error(iex.getMessage());
			} catch (IOException ioex) {
				log.error(ioex.getMessage());
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
			ExecutionContext.androidZeta().setVersionInfo(androidClientInfo);
			ExecutionContext.androidZeta().setDeviceInfo(androidDeviceInfo);
		}

		if (ExecutionContext.isIosEnabled()) {
			BuildVersionInfo iosClientInfo = new BuildVersionInfo();
			ClientDeviceInfo iosDeviceInfo = new ClientDeviceInfo();
			try {
				iosClientInfo = IOSCommonUtils.readClientVersionFromPlist();
			} catch (Exception e) {
				log.error("Failed to get iOS client info from Info.plist.\n"
						+ e.getMessage());
			}
			ExecutionContext.iosZeta().setVersionInfo(iosClientInfo);
			try {
				iosDeviceInfo = IOSCommonUtils.readDeviceInfo();
			} catch (Exception e) {
				log.error("Failed to get iOS device info. Seems like client were crashed during test.\n"
						+ e.getMessage());
			}
			log.debug("Following info were taken from iOS device: "
					+ iosDeviceInfo);
			ExecutionContext.iosZeta().setDeviceInfo(iosDeviceInfo);
		}

		if (ExecutionContext.isOsxEnabled()) {
			BuildVersionInfo osxClientInfo = new BuildVersionInfo();
			ClientDeviceInfo osxDeviceInfo = new ClientDeviceInfo();
			try {
				osxClientInfo = OSXCommonUtils.readClientVersionFromPlist();
			} catch (Exception e) {
				log.error("Failed to read client info for OSX client.\n"
						+ e.getMessage());
			}
			ExecutionContext.osxZeta().setVersionInfo(osxClientInfo);
			try {
				osxDeviceInfo = OSXCommonUtils.readDeviceInfo();
				log.debug("Following info were taken from OSX device: "
						+ osxDeviceInfo);
			} catch (Exception e) {
				log.error("Error while getting device information for OS X client.\n"
						+ e.getMessage());
			}
			ExecutionContext.osxZeta().setDeviceInfo(osxDeviceInfo);
		}

	}

	@Then("I perform acceptance checks")
	public void IPerformAcceptanceChecks() {
		String commentFormat = "Acceptance checks results:\n"
				+ "\tClients stability check - %s\n"
				+ "\tMessages receiving check - %s\n"
				+ "\tMessages receive time check - %s\n"
				+ "\tMessages order check - %s\n";

		boolean isTestPassed = (ExecutionContext.report.areClientsStable
				&& ExecutionContext.report.areMessagesReceived
				&& ExecutionContext.report.areMessagesReceiveTimeCorrect && ExecutionContext.report.areMessagesOrderCorrect);

		String comment = String
				.format(commentFormat,
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areClientsStable),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesReceived),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesReceiveTimeCorrect),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesOrderCorrect));

		Assert.assertTrue(comment, isTestPassed);
	}
}
