package com.wearezeta.auto.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.email.EmailHeaders;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.user_management.ClientUser;
import com.wearezeta.auto.user_management.UserState;

public class BackendAPIWrappers {
	private static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;
	private static final int NUMBER_OF_REGISTRATION_RETRIES = 5;
	private static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	public static final int ACTIVATION_TIMEOUT = 120; // seconds

	private static final Logger log = ZetaLogger
			.getLog(BackendAPIWrappers.class.getSimpleName());

	// ! Mutates user instance
	public static ClientUser createUser(ClientUser user) throws Exception {
		IMAPSMailbox mbox = IMAPSMailbox.getInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", user.getEmail());
		MBoxChangesListener listener = mbox.startMboxListener(expectedHeaders);
		BackEndREST.registerNewUser(user.getEmail(), user.getName(),
				user.getPassword());
		activateRegisteredUser(listener);
		user.setUserState(UserState.Created);
		return user;
	}

	public static void activateRegisteredUser(MBoxChangesListener listener)
			throws Exception {
		EmailHeaders registrationInfo = IMAPSMailbox.getFilteredMessageHeaders(
				listener, ACTIVATION_TIMEOUT);
		BackEndREST.activateNewUser(registrationInfo.getXZetaKey(),
				registrationInfo.getXZetaCode());
		log.debug(String.format("User %s is activated",
				registrationInfo.getLastUserEmail()));
	}

	public static String generateUniqName() {
		return CommonUtils.generateGUID().replace("-", "");
	}

	public static String generateUniqEmail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}

	// ! Mutates the users list
	public static void generateUsers(List<ClientUser> usersToCreate)
			throws Exception {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);

		final AtomicInteger createdClientsCount = new AtomicInteger(0);
		for (final ClientUser userToCreate : usersToCreate) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					int count = 0;
					int waitTime = 1;
					while (count < NUMBER_OF_REGISTRATION_RETRIES) {
						try {
							createUser(userToCreate);
							createdClientsCount.incrementAndGet();
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
						count++;
						try {
							Thread.sleep(waitTime * 1000);
							waitTime *= 2;
						} catch (InterruptedException e) {
							return;
						}
					}
				}
			});
			executor.execute(worker);
		}
		executor.shutdown();
		if (!executor
				.awaitTermination(USERS_CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to prepare predefined users within %d seconds timeout",
							USERS_CREATION_TIMEOUT));
		}
		if (createdClientsCount.get() != usersToCreate.size()) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}
	}

	// ! Mutates all user instances
	public static void createContactLinks(ClientUser userFrom,
			List<ClientUser> usersTo) throws Exception {
		userFrom = BackEndREST.loginByUser(userFrom);
		userFrom.setId(BackEndREST.getUserInfo(userFrom).getId());
		for (ClientUser userTo : usersTo) {
			autoTestSendRequest(userFrom, userTo);
			autoTestAcceptAllRequest(userTo);
		}
	}

	// ! Mutates user instance
	public static void autoTestSendRequest(ClientUser userFrom, ClientUser userTo)
			throws Exception {
		userFrom = BackEndREST.loginByUser(userFrom);
		userFrom = BackEndREST.getUserInfo(userFrom);
		BackEndREST.sendConnectRequest(userFrom, userTo, userTo.getName(),
				"Hello!!!");
		userFrom.setUserState(UserState.RequestSend);
	}

	// ! Mutates user instance
	public static ClientUser autoTestAcceptAllRequest(ClientUser userTo)
			throws Exception {
		userTo = BackEndREST.loginByUser(userTo);
		BackEndREST.acceptAllConnections(userTo);
		userTo.setUserState(UserState.AllContactsConnected);
		return userTo;
	}

	// ! Mutates user instance
	public static void sendDialogMessage(ClientUser fromUser,
			ClientUser toUser, String message) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationWithSingleUser(fromUser, toUser);
		BackEndREST.sendConversationMessage(fromUser, id, message);
	}

	// ! Mutates user instance
	public static void sendDialogMessageByChatName(ClientUser fromUser,
			String toChat, String message) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationByName(fromUser, toChat);
		BackEndREST.sendConversationMessage(fromUser, id, message);
	}

	// ! Mutates user instance
	public static String sendPingToConversation(ClientUser fromUser,
			String toChat) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String id = BackEndREST.getConversationByName(fromUser, toChat);
		return BackEndREST.sendConversationPing(fromUser, id);
	}

	// ! Mutates user instance
	public static void sendHotPingToConversation(ClientUser fromUser,
			String toChat, String id) throws Exception {
		fromUser = BackEndREST.loginByUser(fromUser);
		String conv_id = BackEndREST.getConversationByName(fromUser, toChat);
		BackEndREST.sendConvertsationHotPing(fromUser, conv_id, id);
	}
	
//	public void sendRandomMessagesToUser(int messCount) {
//		Random random = new Random();
//		if (messCount > userChats.size()) {
//			messCount = userChats.size();
//		}
//		ExecutorService executor = Executors
//				.newFixedThreadPool(MAX_PARALLEL_CREATION_TASKS);
//		for (int i = 0; i < messCount; i++) {
//			ConvPair pair = userChats.get(random.nextInt(userChats.size() - 1));
//			final ClientUser user = pair.getContact();
//			final String contact = pair.getConvName();
//			Runnable worker = new Thread(new Runnable() {
//				public void run() {
//					try {
//						BackEndREST.sendDialogMessageByChatName(user, contact,
//								CommonUtils.generateGUID());
//					} catch (Throwable e) {
//						log.debug(e.getMessage());
//					}
//				}
//			});
//			executor.submit(worker);
//		}
//		executor.shutdown();
//		try {
//			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//		} catch (InterruptedException e) {
//			log.debug(e.getMessage());
//		}
//	}
//
//	public void sendDefaultImageToUser(int imagesCount) throws Throwable {
//		InputStream configFileStream = null;
//		Random random = new Random();
//		configFileStream = new FileInputStream(
//				CommonUtils.getImagePath(ClientUsersManager.class));
//		for (int i = 0; i < imagesCount; i++) {
//			ConvPair pair = userChats.get(random.nextInt(userChats.size() - 1));
//			final ClientUser user = pair.getContact();
//			final String contact = pair.getConvName();
//			BackEndREST.sendPictureToChatByName(user, contact, "default",
//					configFileStream);
//		}
//		if (configFileStream != null) {
//			configFileStream.close();
//		}
//	}
//
//	public void createGroupChatWithUnconnecteduser(String chatName,
//			String groupCreator) throws Exception {
//		ClientUsersManager usrMgr = ClientUsersManager.getInstance();
//		
//		ClientUser groupCreatorUser = usrMgr.findUserByNameAlias(groupCreator);
//		ClientUser unconnectedUser = usrMgr.findUserByNameAlias(ClientUsersManager.YOUR_USER_2_ALIAS);
//		ClientUser selfUser = usrMgr.findUserByNameAlias(ClientUsersManager.SELF_USER_ALIAS);
//
//		BackEndREST.sendConnectRequest(groupCreatorUser, unconnectedUser,
//				CONNECTION_CONSTANT + groupCreatorUser.getName(), chatName);
//		BackEndREST.acceptAllConnections(unconnectedUser);
//		List<ClientUser> users = new ArrayList<ClientUser>();
//		users.add(selfUser); // add self
//		users.add(unconnectedUser);
//
//		groupCreatorUser = BackEndREST.loginByUser(groupCreatorUser);
//		BackEndREST.createGroupConversation(groupCreatorUser, users, chatName);
//	}
}
