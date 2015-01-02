package com.wearezeta.auto.user_management;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
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

public class UsersManager {
	private static final Logger log = ZetaLogger.getLog(UsersManager.class
			.getSimpleName());

	private void resetClientsList(String[] aliases, List<ClientUser> dstList) throws IOException {
		dstList.clear();
		for (String userAlias : aliases) {
			ClientUser pendingUser = new ClientUser();
			pendingUser.setName(userAlias);
			pendingUser.addNameAlias(userAlias);
			pendingUser.setPassword(CommonUtils
					.getDefaultPasswordFromConfig(CommonUtils.class));
			pendingUser.addPasswordAlias(YOUR_PASS_ALIAS);
			dstList.add(pendingUser);
		}
	}

	private void resetUsers() throws IOException {
		resetClientsList(new String[] { YOUR_USER_1_ALIAS, YOUR_USER_2_ALIAS,
				YOUR_USER_3_ALIAS, YOUR_USER_4_ALIAS, YOUR_USER_5_ALIAS,
				YOUR_USER_6_ALIAS, YOUR_USER_7_ALIAS, YOUR_USER_8_ALIAS,
				YOUR_USER_9_ALIAS, YOUR_USER_10_ALIAS, YOUR_USER_11_ALIAS },
				yourUsers);
	}

	public static final String YOUR_PASS_ALIAS = "aqaPassword";
	public static final String YOUR_EMAIL_ALIAS = "aqaEmail";

	public static final String YOUR_USER_1_ALIAS = "aqaUser";
	public static final String SELF_USER_ALIAS = YOUR_USER_1_ALIAS;
	public static final String YOUR_USER_2_ALIAS = "yourUser";
	public static final String YOUR_USER_3_ALIAS = "yourContact";
	public static final String YOUR_UNCONNECTED_USER_ALIAS = YOUR_USER_3_ALIAS;
	public static final String YOUR_USER_4_ALIAS = "yourNotContact1";
	public static final String YOUR_USER_5_ALIAS = "yourNotContact2";
	public static final String YOUR_USER_6_ALIAS = "yourNotContact3";
	public static final String YOUR_USER_7_ALIAS = "yourNotContact4";
	public static final String YOUR_USER_8_ALIAS = "yourIgnore";
	public static final String YOUR_USER_9_ALIAS = "yourAccept";
	public static final String YOUR_USER_10_ALIAS = "yourGroupChat";
	public static final String YOUR_USER_11_ALIAS = "yourNotContact5";
	private List<ClientUser> yourUsers = new ArrayList<ClientUser>();

	public List<ClientUser> getCreatedUsers() {
		ArrayList<ClientUser> result = new ArrayList<ClientUser>();
		for (ClientUser usr : this.yourUsers) {
			if (usr.getUserState() != UserState.NotCreated) {
				result.add(usr);
			}
		}
		return result;
	}

	public void appendCustomUser(ClientUser newUser) {
		this.yourUsers.add(newUser);
	}

	public static final String CONTACT_1_ALIAS = "aqaContact1";
	public static final String CONTACT_2_ALIAS = "aqaContact2";
	public static final String CONTACT_3_ALIAS = "aqaContact3";

	public static final String CONTACT_4_ALIAS = "aqaPictureContact";
	public static final String CONTACT_PICTURE_NAME = CONTACT_4_ALIAS;
	public static final String CONTACT_PICTURE_EMAIL = "smoketester+aqaPictureContact@wearezeta.com";
	public static final String CONTACT_PICTURE_EMAIL_ALIAS = "aqaPictureContactEmail";
	public static final String CONTACT_PICTURE_PASSWORD = "picture123";
	public static final String CONTACT_PICTURE_PASSWORD_ALIAS = "aqaPictureContactPassword";

	public static final String CONTACT_5_ALIAS = "aqaAvatar TestContact";
	public static final String CONTACT_AVATAR_NAME = CONTACT_5_ALIAS;
	public static final String CONTACT_AVATAR_EMAIL = "smoketester+aqaAvatarTestContact@wearezeta.com";
	public static final String CONTACT_AVATAR_EMAIL_ALIAS = "aqaAvatarTestContactEmail";
	public static final String CONTACT_AVATAR_PASSWORD = "avatar123";
	public static final String CONTACT_AVATAR_PASSWORD_ALIAS = "aqaAvatarTestContactPassword";

	public static final String CONTACT_6_ALIAS = "aqaBlock";
	private List<ClientUser> contacts = new ArrayList<ClientUser>();

	private void resetContacts() throws IOException {
		resetClientsList(new String[] { CONTACT_1_ALIAS, CONTACT_2_ALIAS,
				CONTACT_3_ALIAS, CONTACT_4_ALIAS, CONTACT_5_ALIAS,
				CONTACT_6_ALIAS }, contacts);
		ClientUser pictureContact = findUserByNameAlias(CONTACT_3_ALIAS);
		pictureContact.setEmail(CONTACT_PICTURE_EMAIL);
		pictureContact.addEmailAlias(CONTACT_PICTURE_EMAIL_ALIAS);
		pictureContact.setPassword(CONTACT_PICTURE_PASSWORD);
		pictureContact.clearPasswordAliases();
		pictureContact.addPasswordAlias(CONTACT_PICTURE_PASSWORD_ALIAS);
		pictureContact.setUserState(UserState.Created);
		ClientUser avatarContact = findUserByNameAlias(CONTACT_4_ALIAS);
		avatarContact.setEmail(CONTACT_AVATAR_EMAIL);
		avatarContact.addEmailAlias(CONTACT_AVATAR_EMAIL_ALIAS);
		avatarContact.setPassword(CONTACT_AVATAR_PASSWORD);
		avatarContact.clearPasswordAliases();
		avatarContact.addPasswordAlias(CONTACT_AVATAR_PASSWORD_ALIAS);
		avatarContact.setUserState(UserState.Created);
	}

	public List<ClientUser> getCreatedContacts() {
		ArrayList<ClientUser> result = new ArrayList<ClientUser>();
		for (ClientUser usr : this.contacts) {
			if (usr.getUserState() != UserState.NotCreated) {
				result.add(usr);
			}
		}
		return result;
	}

	public void appendCustomContact(ClientUser newContact) {
		this.contacts.add(newContact);
	}

	public static final String PERFORMANCE_USER_ALIAS = "perfUser";
	public static final String PERFORMANCE_PASS_ALIAS = "perfPass";
	private ClientUser perfUser = new ClientUser();
	{
		perfUser.addNameAlias(PERFORMANCE_USER_ALIAS);
		perfUser.addPasswordAlias(PERFORMANCE_PASS_ALIAS);
	}

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

	private static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;
	private static final int NUMBER_OF_REGISTRATION_RETRIES = 5;
	private static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	private final static String CONNECTION_CONSTANT = "CONNECT TO ";

	private static UsersManager instance = null;

	private UsersManager() throws IOException {
		resetUsers();
		resetContacts();
	}

	public static UsersManager getInstance() {
		if (instance == null) {
			try {
				instance = new UsersManager();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private List<ClientUser> getAllUsers() {
		List<ClientUser> allUsers = new ArrayList<ClientUser>();
		allUsers.addAll(yourUsers);
		allUsers.addAll(contacts);
		allUsers.add(perfUser);
		return allUsers;
	}

	public static enum UserAliasType {
		NAME, PASSWORD, EMAIL;
	}

	public ClientUser findUserByNameAlias(String alias) {
		return findUserByAlias(alias, UserAliasType.NAME);
	}

	public ClientUser findUserByAlias(String alias, UserAliasType aliasType) {
		for (Object item : getAllUsers()) {
			ClientUser user = (ClientUser) item;
			Set<String> aliases = null;
			if (aliasType == UserAliasType.NAME) {
				aliases = user.getNameAliases();
			} else if (aliasType == UserAliasType.EMAIL) {
				aliases = user.getEmailAliases();
			} else if (aliasType == UserAliasType.PASSWORD) {
				aliases = user.getPasswordAliases();
			} else {
				assert (false);
			}
			for (String currentAlias : aliases) {
				if (currentAlias.equalsIgnoreCase(alias)) {
					return user;
				}
			}
		}
		throw new NoSuchElementException(String.format(
				"No user with username '%s' is in an available list", alias));
	}

	public String replaceAliasesOccurences(String srcStr,
			UserAliasType aliasType) {
		String result = srcStr;
		for (ClientUser dstUser : this.getAllUsers()) {
			try {
				Set<String> aliases = null;
				String replacement = null;
				if (aliasType == UserAliasType.NAME) {
					aliases = dstUser.getNameAliases();
					replacement = dstUser.getName();
				} else if (aliasType == UserAliasType.EMAIL) {
					aliases = dstUser.getEmailAliases();
					replacement = dstUser.getEmail();
				} else if (aliasType == UserAliasType.PASSWORD) {
					aliases = dstUser.getPasswordAliases();
					replacement = dstUser.getPassword();
				} else {
					assert (false);
				}
				for (String alias : aliases) {
					result = result.replace(alias, replacement);
				}
			} catch (NoSuchElementException e) {
				// Ignore silently
			}
		}
		return result;
	}

	public void generateUsers(int usersNumber, int contactsNumber)
			throws Exception {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		final List<ClientUser> createdClients = new CopyOnWriteArrayList<ClientUser>();
		for (int i = 0; i < usersNumber + contactsNumber; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					boolean doRetry = true;
					int count = 0;
					int waitTime = 1;
					while (doRetry && count < NUMBER_OF_REGISTRATION_RETRIES) {
						try {
							final String email = UserCreationHelper
									.registerUserAndReturnMail();
							ClientUser user = new ClientUser();
							user.setEmail(email);
							user.setUserState(UserState.Created);
							createdClients.add(user);
							doRetry = false;
						} catch (Exception e) {
							doRetry = true;
							e.printStackTrace();
						}
						if (doRetry) {
							count++;
							try {
								Thread.sleep(waitTime * 1000);
							} catch (InterruptedException e) {
								return;
							}
							waitTime *= 2;
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
		if (createdClients.size() != usersNumber + contactsNumber) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}

		int userIdx = 0;
		int contactIdx = 0;
		this.resetUsers();
		this.resetContacts();
		for (int clientIdx = 0; clientIdx < usersNumber + contactsNumber; clientIdx++) {
			ClientUser dstClient = null;
			if (clientIdx < usersNumber) {
				dstClient = this.yourUsers.get(userIdx);
				userIdx++;
			} else {
				dstClient = this.contacts.get(contactIdx);
				contactIdx++;
			}
			if (dstClient.getUserState() != UserState.NotCreated) {
				continue;
			}
			ClientUser srcClient = createdClients.get(clientIdx);
			dstClient.setEmail(srcClient.getEmail());
			dstClient.addEmailAlias(YOUR_EMAIL_ALIAS);
			dstClient.setUserState(srcClient.getUserState());
		}
	}

	public void generateNUsers(int usersNum) throws InterruptedException,
			BackendRequestException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		final AtomicInteger numOfUsersCreatedWOErrors = new AtomicInteger();
		numOfUsersCreatedWOErrors.set(0);
		this.resetAdditionalUsers();
		for (int i = 0; i < usersNum; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						String email = UserCreationHelper
								.registerUserAndReturnMail();
						ClientUser user = new ClientUser();
						user.setEmail(email);
						user.setPassword(CommonUtils
								.getDefaultPasswordFromConfig(CommonUtils.class));
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
		if (!executor
				.awaitTermination(USERS_CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to generate users within %d seconds timeout",
							USERS_CREATION_TIMEOUT));
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
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		final AtomicInteger numOfConnsSentWOErrors = new AtomicInteger();
		numOfConnsSentWOErrors.set(0);
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
		if (!executor
				.awaitTermination(USERS_CREATION_TIMEOUT, TimeUnit.SECONDS)) {
			throw new BackendRequestException(
					String.format(
							"The backend has failed to send conection requests within %d seconds timeout",
							USERS_CREATION_TIMEOUT));
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
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
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

	public void generatePerformanceUser() throws Exception {
		String email = UserCreationHelper.registerUserAndReturnMail();
		perfUser.setEmail(email);
		perfUser.setPassword(CommonUtils
				.getDefaultPasswordFromConfig(CommonUtils.class));
		perfUser = BackEndREST.loginByUser(perfUser);
		perfUser = BackEndREST.getUserInfo(perfUser);
	}

	public void createContactLinks(int linkedUsers) throws Exception {
		List<ClientUser> users = this.getCreatedUsers();
		for (ClientUser yourUser : users) {
			yourUser = BackEndREST.loginByUser(yourUser);
			yourUser = BackEndREST.getUserInfo(yourUser);
		}
		for (int i = 0; i < linkedUsers; i++) {
			ClientUser dstUser = users.get(i);
			for (ClientUser contact : this.getCreatedContacts()) {
				BackEndREST.autoTestSendRequest(contact, dstUser);
				contact.setUserState(UserState.RequestSend);
				Thread.sleep(500);
			}

			BackEndREST.autoTestAcceptAllRequest(dstUser);
			dstUser.setUserState(UserState.AllContactsConnected);
		}
	}

	public void createGroupChatWithUnconnecteduser(String chatName,
			String groupCreator) throws Exception {
		ClientUser groupCreatorUser = findUserByAlias(groupCreator,
				UserAliasType.NAME);
		ClientUser unconnectedUser = findUserByAlias(YOUR_USER_2_ALIAS,
				UserAliasType.NAME);
		ClientUser selfUser = findUserByAlias(YOUR_USER_1_ALIAS,
				UserAliasType.NAME);

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
