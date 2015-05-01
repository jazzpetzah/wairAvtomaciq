package com.wearezeta.auto.sync;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.sync.client.InstanceState;
import com.wearezeta.auto.sync.client.WireInstance;
import com.wearezeta.auto.sync.report.ReportGenerator;
import com.wearezeta.auto.sync.report.data.ReportData;
import com.wearezeta.auto.sync.ExecutionContext;
import com.wearezeta.auto.sync.SyncEngineUtil;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class CommonSteps {

	static {
		CommonUtils.defineNoHeadlessEnvironment();
		CommonUtils.disableSeleniumLogs();
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
								} catch (Exception e) {
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

		ExecutionContext.iosZeta().listener().storePageSource(false);
		ExecutionContext.osxZeta().listener().storePageSource(false);

		// iOS: send messages without interval and without listening them
		sendWithoutIntervalAndNoListen(ExecutionContext.iosZeta());

		ExecutionContext.iosZeta().listener().storePageSource(false);
		ExecutionContext.osxZeta().listener().storePageSource(false);

		// send from osx, receive on ios and android
		sendAndListen(ExecutionContext.osxZeta(), ExecutionContext.iosZeta(),
				ExecutionContext.androidZeta());

		ExecutionContext.iosZeta().listener().storePageSource(false);
		ExecutionContext.osxZeta().listener().storePageSource(false);

		// send from android, receive on ios and osx
		sendAndListen(ExecutionContext.androidZeta(),
				ExecutionContext.iosZeta(), ExecutionContext.osxZeta());

		ExecutionContext.iosZeta().listener().storePageSource(false);
		ExecutionContext.osxZeta().listener().storePageSource(false);

		// OSX: send messages without interval and without listening them
		// It is possible that short interval required for OSX
		sendWithoutIntervalAndNoListen(ExecutionContext.osxZeta());

		ExecutionContext.iosZeta().listener().storePageSource(false);
		ExecutionContext.osxZeta().listener().storePageSource(false);

		// Android: send messages without interval and without listening them
		sendWithoutIntervalAndNoListen(ExecutionContext.androidZeta());

		ExecutionContext.iosZeta().listener().storePageSource(true);
		ExecutionContext.osxZeta().listener().storePageSource(true);

		for (WireInstance client : ExecutionContext.clients.values()) {
			client.listener().receiveAllChatMessages(false);
		}
	}

	@Given("I collect messages order data")
	public void ICollectMessagesOrderData() {
		for (WireInstance client : ExecutionContext.clients.values()) {
			client.reporter().copyWholeMessagesList();
			client.reporter().checkMessagesOrderCorrectness();
		}
	}

	@Given("I build results report")
	public void IBuildResultsReport() throws Exception {
		ExecutionContext.report.fillReportInfo();
		log.debug(ReportData.toXml(ExecutionContext.report));
		ReportGenerator.generate(ExecutionContext.report);
	}

	@Then("I collect builds and devices info")
	public void ICollectBuildsAndDevicesInfo() {
		for (WireInstance client : ExecutionContext.clients.values()) {
			client.reporter().readClientVersionAndDeviceInfo();
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
