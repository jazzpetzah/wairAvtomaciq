package com.wearezeta.auto.user_management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;
import org.json.JSONException;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class UserChatsHelper {
	private static final int MAX_PARALLEL_CREATION_TASKS = 5;
	private static final int CREATION_TIMEOUT = 60 * 5; // seconds
	
	private final static String CONNECTION_CONSTANT = "CONNECT TO ";

	private static final Logger log = ZetaLogger.getLog(UserChatsHelper.class
			.getSimpleName());

	private List<ClientUser> additionalUsers = new CopyOnWriteArrayList<ClientUser>();

	public List<ClientUser> getAdditionalUsers() {
		return new ArrayList<ClientUser>(this.additionalUsers);
	}

	private void resetAdditionalUsers() {
		this.additionalUsers.clear();
	}

	private List<ConvPair> userChats = new CopyOnWriteArrayList<ConvPair>();

	public List<ConvPair> getUserChats() {
		return new ArrayList<ConvPair>(this.userChats);
	}

	private void resetUserChats() {
		this.userChats.clear();
	}

	private static UserChatsHelper instance = null;

	public static UserChatsHelper getInstance() {
		if (instance == null) {
			instance = new UserChatsHelper();
		}
		return instance;
	}

	public UserChatsHelper() {
	}

	public void generateNUsers(int usersNum) throws InterruptedException,
			BackendRequestException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_CREATION_TASKS);
		final AtomicInteger numOfUsersCreatedWOErrors = new AtomicInteger(0);
		this.resetAdditionalUsers();
		for (int i = 0; i < usersNum; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						ClientUser user = new ClientUser();
						UserCreationHelper.createWireUser(user);
						additionalUsers.add(user);
						numOfUsersCreatedWOErrors.getAndIncrement();
					} catch (Exception e) {
						log.debug("error" + e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		if (!executor.awaitTermination(CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(String.format(
					"The backend has failed to generate "
							+ "additional users within %d seconds timeout",
					CREATION_TIMEOUT));
		}
		if (numOfUsersCreatedWOErrors.get() != usersNum) {
			throw new BackendRequestException(String.format(
					"Failed to create %d new users on the backend", usersNum));
		}
	}

	public void sendConnectionRequestInThreads(final ClientUser yourUser)
			throws IllegalArgumentException, UriBuilderException, IOException,
			JSONException, BackendRequestException, InterruptedException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_CREATION_TASKS);
		final AtomicInteger numOfConnsSentWOErrors = new AtomicInteger(0);
		this.resetUserChats();
		for (int i = 0; i < additionalUsers.size(); i++) {
			final ClientUser user = additionalUsers.get(i);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackEndREST.autoTestSendRequest(user, yourUser);
						ConvPair pair = new ConvPair();
						pair.setContact(user);
						pair.setConvName(yourUser.getName());
						userChats.add(pair);
						numOfConnsSentWOErrors.getAndIncrement();
					} catch (Exception e) {
						log.debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		if (!executor.awaitTermination(CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(String.format(
					"The backend has failed to send conection "
							+ "requests within %d seconds timeout",
					CREATION_TIMEOUT));
		}
		if (numOfConnsSentWOErrors.get() != additionalUsers.size()) {
			throw new BackendRequestException(String.format(
					"Failed to send connections to %d users on the backend",
					additionalUsers.size()));
		}
	}

	public void sendRandomMessagesToUser(int messCount) {
		Random random = new Random();
		if (messCount > userChats.size()) {
			messCount = userChats.size();
		}
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_CREATION_TASKS);
		for (int i = 0; i < messCount; i++) {
			ConvPair pair = userChats.get(random.nextInt(userChats.size() - 1));
			final ClientUser user = pair.getContact();
			final String contact = pair.getConvName();
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						BackEndREST.sendDialogMessageByChatName(user, contact,
								CommonUtils.generateGUID());
					} catch (Throwable e) {
						log.debug(e.getMessage());
					}
				}
			});
			executor.submit(worker);
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			log.debug(e.getMessage());
		}
	}

	public void sendDefaultImageToUser(int imagesCount) throws Throwable {
		InputStream configFileStream = null;
		Random random = new Random();
		configFileStream = new FileInputStream(
				CommonUtils.getImagePath(UsersManager.class));
		for (int i = 0; i < imagesCount; i++) {
			ConvPair pair = userChats.get(random.nextInt(userChats.size() - 1));
			final ClientUser user = pair.getContact();
			final String contact = pair.getConvName();
			BackEndREST.sendPictureToChatByName(user, contact, "default",
					configFileStream);
		}
		if (configFileStream != null) {
			configFileStream.close();
		}
		
	}

	public void createGroupChatWithUnconnecteduser(String chatName,
			String groupCreator) throws Exception {
		UsersManager usrMgr = UsersManager.getInstance();
		
		ClientUser groupCreatorUser = usrMgr.findUserByNameAlias(groupCreator);
		ClientUser unconnectedUser = usrMgr.findUserByNameAlias(UsersManager.YOUR_USER_2_ALIAS);
		ClientUser selfUser = usrMgr.findUserByNameAlias(UsersManager.SELF_USER_ALIAS);

		BackEndREST.sendConnectRequest(groupCreatorUser, unconnectedUser,
				CONNECTION_CONSTANT + groupCreatorUser.getName(), chatName);
		BackEndREST.acceptAllConnections(unconnectedUser);
		List<ClientUser> users = new ArrayList<ClientUser>();
		users.add(selfUser); // add self
		users.add(unconnectedUser);

		groupCreatorUser = BackEndREST.loginByUser(groupCreatorUser);
		BackEndREST.createGroupConversation(groupCreatorUser, users, chatName);
	}
}
