package com.wearezeta.auto.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.BuildVersionInfo;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;
import com.wearezeta.auto.common.misc.MessageEntry;
import com.wearezeta.auto.ios.tools.IOSCommonUtils;
import com.wearezeta.auto.osx.common.OSXCommonUtils;
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
		SyncEngineUtil.defineHeadlessEnvironment();
		SyncEngineUtil.disableSeleniumLogs();
	}

	private static final Logger log = ZetaLogger.getLog(CommonSteps.class
			.getSimpleName());

	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

	@Before
	public void setup() throws Exception {
		// no preparations at this moment
	}

	@After
	public void teardown() throws Exception {
		for (WireInstance client : ExecutionContext.clients.values()) {
			client.closeAndClear();
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

		ExecutorService executor = Executors
				.newFixedThreadPool(SEConstants.Execution.STARTUP_THREAD_POOL_SIZE);

		for (WireInstance client : ExecutionContext.clients.values()) {
			executor.execute(client.startClientProcedure());
		}

		executor.shutdown();

		if (!executor.awaitTermination(
				SEConstants.Execution.STARTUP_TIMEOUT_MIN, TimeUnit.MINUTES)) {
			throw new Exception(
					String.format(
							"Clients startup was not finished in expected time (%s minutes).",
							SEConstants.Execution.STARTUP_TIMEOUT_MIN));
		}
	}

	@Given("I sign in to all platform clients and go to conversation (.*)")
	public void ISignInToAllPlatformClients(String conversation)
			throws InterruptedException, Exception {

		ExecutorService executor = Executors
				.newFixedThreadPool(SEConstants.Execution.SIGN_IN_THREAD_POOL_SIZE);

		for (WireInstance client : ExecutionContext.clients.values()) {
			executor.execute(client.signInAndOpenConversation());
		}

		executor.shutdown();

		if (!executor.awaitTermination(
				SEConstants.Execution.SIGN_IN_TIMEOUT_MIN, TimeUnit.MINUTES)) {
			throw new Exception(
					String.format(
							"Clients sign in was not finished in expected time (%s minutes).",
							SEConstants.Execution.SIGN_IN_TIMEOUT_MIN));
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

	private void sendAndListen(WireInstance sender, WireInstance... listeners)
			throws Exception {
		if (sender.isEnabled()
				&& sender.getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 0; i < sender.getMessagesToSend(); i++) {
				final String message = CommonUtils.generateGUID();
				sender.sender().sendTextMessage(
						SEConstants.Common.TEST_CONVERSATION, message);
				log.debug(String.format("%s: message '%s' sent.",
						sender.platform(), message));
				ExecutorService executor = Executors
						.newFixedThreadPool(listeners.length);
				for (final WireInstance listener : listeners) {
					if (listener.isEnabled()
							&& listener.getState() != InstanceState.ERROR_CRASHED) {
						executor.execute(new Runnable() {
							public void run() {
								try {
									listener.listener().waitForMessage(message,
											true);
									log.debug(String
											.format("%s: message received successfully.",
													listener.platform()));
								} catch (NoSuchElementException e) {
									log.debug(String.format(
											"%s: message was not found.\n%s",
											listener.platform(), e.getMessage()));
									if (listener.listener().isSessionLost()) {
										log.error("%s: session lost. Client testing stopped.");
										listener.setState(InstanceState.ERROR_CRASHED);
									}
								}
							}
						});
					}
				}
				executor.shutdown();
				if (!executor.awaitTermination(
						SEConstants.Execution.AWAITING_MESSAGES_TIMEOUT_MIN,
						TimeUnit.MINUTES)) {
					throw new Exception(
							String.format(
									"Message awaiting was not finished in expected time (%s minutes).",
									SEConstants.Execution.AWAITING_MESSAGES_TIMEOUT_MIN));
				}
				if (sender.getMessagesSendingInterval() > 0) {
					try {
						Thread.sleep(sender.getMessagesSendingInterval() * 1000);
					} catch (InterruptedException e) {
					}
				}
			}
		}
	}

	private void sendWithoutIntervalAndNoListen(WireInstance sender) {
		if (sender.isEnabled()
				&& sender.getState() != InstanceState.ERROR_CRASHED) {
			for (int i = 1; i <= sender.getMessagesToSend(); i++) {
				long startDate = new Date().getTime();
				final String message = CommonUtils.generateGUID();
				sender.sender().sendTextMessage(
						SEConstants.Common.TEST_CONVERSATION, message, false);
				long endDate = new Date().getTime();
				log.debug(String
						.format("%s: Time consumed for sending text message #%s - %sms",
								sender.platform(), i, (endDate - startDate)));
			}
		}
	}

	@Given("I run serial sync engine test")
	public void IRunSerialSyncEngineTest() throws InterruptedException,
			Exception {
		// send from ios, receive on osx and android
		sendAndListen(ExecutionContext.iosZeta(), ExecutionContext.osxZeta(),
				ExecutionContext.androidZeta());

		storeIosPageSource(false);
		storeOsxPageSource();

		// iOS: send messages without interval and without listening them
		sendWithoutIntervalAndNoListen(ExecutionContext.iosZeta());

		storeIosPageSource(false);
		storeOsxPageSource();

		// send from osx, receive on ios and android
		sendAndListen(ExecutionContext.osxZeta(), ExecutionContext.iosZeta(),
				ExecutionContext.androidZeta());

		storeIosPageSource(false);
		storeOsxPageSource();

		// send from android, receive on ios and osx
		sendAndListen(ExecutionContext.androidZeta(),
				ExecutionContext.iosZeta(), ExecutionContext.osxZeta());

		storeIosPageSource(false);
		storeOsxPageSource();

		// OSX: send messages without interval and without listening them
		// It is possible that short interval required for OSX
		sendWithoutIntervalAndNoListen(ExecutionContext.osxZeta());

		storeIosPageSource(false);
		storeOsxPageSource();

		// Android: send messages without interval and without listening them
		sendWithoutIntervalAndNoListen(ExecutionContext.androidZeta());

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
		boolean passed = (ExecutionContext.report.areClientsStable
				&& ExecutionContext.report.areMessagesReceived
				&& ExecutionContext.report.areMessagesReceiveTimeCorrect && ExecutionContext.report.areMessagesOrderCorrect);

		String failComment = String
				.format(SEConstants.Common.FAILED_CHECKS_LOG_OUTPUT,
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areClientsStable),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesReceived),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesReceiveTimeCorrect),
						SyncEngineUtil
								.prepareCheckStatus(ExecutionContext.report.areMessagesOrderCorrect));

		Assert.assertTrue(failComment, passed);
	}
}
