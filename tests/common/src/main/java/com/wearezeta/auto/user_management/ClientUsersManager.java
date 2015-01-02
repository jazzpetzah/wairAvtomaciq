package com.wearezeta.auto.user_management;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;

public class ClientUsersManager {
	private void setClientUserAliases(ClientUser user, String[] nameAliases,
			String[] passwordAliases, String[] emailAliases) {
		if (nameAliases != null && nameAliases.length > 0) {
			user.clearNameAliases();
			for (String nameAlias : nameAliases) {
				user.addNameAlias(nameAlias);
			}
		}
		if (passwordAliases != null && passwordAliases.length > 0) {
			user.clearPasswordAliases();
			for (String passwordAlias : passwordAliases) {
				user.addPasswordAlias(passwordAlias);
			}
		}
		if (emailAliases != null && emailAliases.length > 0) {
			user.clearEmailAliases();
			for (String emailAlias : passwordAliases) {
				user.addEmailAlias(emailAlias);
			}
		}
	}

	private void resetClientsList(String[] aliases, List<ClientUser> dstList)
			throws IOException {
		dstList.clear();
		for (String nameAlias : aliases) {
			ClientUser pendingUser = new ClientUser();
			setClientUserAliases(pendingUser, new String[] { nameAlias },
					new String[] { YOUR_PASS_ALIAS },
					new String[] { YOUR_EMAIL_ALIAS });
			dstList.add(pendingUser);
		}
	}

	private void resetUsers() throws IOException {
		resetClientsList(new String[] { YOUR_USER_1_ALIAS, YOUR_USER_2_ALIAS,
				YOUR_USER_3_ALIAS, YOUR_USER_4_ALIAS, YOUR_USER_5_ALIAS,
				YOUR_USER_6_ALIAS, YOUR_USER_7_ALIAS, YOUR_USER_8_ALIAS,
				YOUR_USER_9_ALIAS, YOUR_USER_10_ALIAS, YOUR_USER_11_ALIAS },
				users);
	}

	public static final String YOUR_PASS_ALIAS = "aqaPassword";
	public static final String YOUR_EMAIL_ALIAS = "aqaUser";

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
	private List<ClientUser> users = new ArrayList<ClientUser>();

	public List<ClientUser> getCreatedUsers() {
		ArrayList<ClientUser> result = new ArrayList<ClientUser>();
		for (ClientUser usr : this.users) {
			if (usr.getUserState() != UserState.NotCreated) {
				result.add(usr);
			}
		}
		return result;
	}

	public void appendCustomUser(ClientUser newUser) {
		this.users.add(newUser);
	}

	public static final String CONTACT_1_ALIAS = "aqaContact1";
	public static final String CONTACT_2_ALIAS = "aqaContact2";
	public static final String CONTACT_3_ALIAS = "aqaContact3";
	public static final String CONTACT_PICTURE_NAME_ALIAS = "aqaPictureContact";
	public static final String CONTACT_PICTURE_EMAIL_ALIAS = "aqaPictureContactEmail";
	public static final String CONTACT_PICTURE_PASSWORD_ALIAS = "aqaPictureContactPassword";
	public static final String CONTACT_AVATAR_NAME_ALIAS = "aqaAvatar TestContact";
	public static final String CONTACT_AVATAR_EMAIL_ALIAS = "aqaAvatarTestContactEmail";
	public static final String CONTACT_AVATAR_PASSWORD_ALIAS = "aqaAvatarTestContactPassword";
	public static final String CONTACT_BLOCK_ALIAS = "aqaBlock";
	private List<ClientUser> contacts = new ArrayList<ClientUser>();

	private void resetContacts() throws IOException {
		resetClientsList(new String[] { CONTACT_1_ALIAS, CONTACT_2_ALIAS,
				CONTACT_3_ALIAS, CONTACT_PICTURE_NAME_ALIAS,
				CONTACT_AVATAR_NAME_ALIAS, CONTACT_BLOCK_ALIAS }, contacts);
		ClientUser pictureContact = findUserByNameAlias(CONTACT_PICTURE_NAME_ALIAS);
		setClientUserAliases(pictureContact, null,
				new String[] { CONTACT_PICTURE_PASSWORD_ALIAS },
				new String[] { CONTACT_PICTURE_EMAIL_ALIAS });
		ClientUser avatarContact = findUserByNameAlias(CONTACT_AVATAR_NAME_ALIAS);
		setClientUserAliases(avatarContact, null,
				new String[] { CONTACT_AVATAR_EMAIL_ALIAS },
				new String[] { CONTACT_AVATAR_PASSWORD_ALIAS });
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

	public static final String PERFORMANCE_USER_NAME = "perfUser";
	public static final String PERFORMANCE_USER_ALIAS = PERFORMANCE_USER_NAME;
	public static final String PERFORMANCE_PASS_ALIAS = "perfPass";
	private ClientUser perfUser = new ClientUser();

	private void resetPerfUser() throws IOException {
		perfUser.clearNameAliases();
		perfUser.addNameAlias(PERFORMANCE_USER_ALIAS);
		perfUser.clearPasswordAliases();
		perfUser.addPasswordAlias(PERFORMANCE_PASS_ALIAS);
		perfUser.clearEmailAliases();
		perfUser.addEmailAlias(YOUR_EMAIL_ALIAS);
	}

	private static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;
	private static final int NUMBER_OF_REGISTRATION_RETRIES = 5;
	private static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	private static ClientUsersManager instance = null;

	private ClientUsersManager() throws IOException {
		resetPerfUser();
		resetUsers();
		resetContacts();
	}

	public static ClientUsersManager getInstance() {
		if (instance == null) {
			try {
				instance = new ClientUsersManager();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return instance;
	}

	private List<ClientUser> getAllUsers() {
		List<ClientUser> allUsers = new ArrayList<ClientUser>();
		allUsers.addAll(users);
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

		final AtomicInteger createdClientsCount = new AtomicInteger(0);
		for (int clientIdx = 0; clientIdx < usersNumber + contactsNumber; clientIdx++) {
			List<ClientUser> srcList = null;
			int listIdx = 0;
			if (clientIdx < usersNumber) {
				srcList = this.users;
				listIdx = clientIdx;
			} else {
				srcList = this.contacts;
				listIdx = clientIdx - usersNumber;
			}
			final ClientUser userToCreate = srcList.get(listIdx);
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					int count = 0;
					int waitTime = 1;
					while (count < NUMBER_OF_REGISTRATION_RETRIES) {
						try {
							UserCreationHelper.createWireUser(userToCreate);
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
		if (createdClientsCount.get() != usersNumber + contactsNumber) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}
	}

	public void generatePerformanceUser() throws Exception {
		perfUser = UserCreationHelper.createWireUser(perfUser);
		perfUser = BackEndREST.loginByUser(perfUser);
		perfUser.setId(BackEndREST.getUserInfo(perfUser).getId());
	}

	public void createContactLinks(int linkedUsers) throws Exception {
		for (ClientUser yourUser : this.getCreatedUsers()) {
			yourUser = BackEndREST.loginByUser(yourUser);
			yourUser.setId(BackEndREST.getUserInfo(yourUser).getId());
		}
		for (int i = 0; i < linkedUsers; i++) {
			ClientUser dstUser = this.getCreatedUsers().get(i);
			for (ClientUser contact : this.getCreatedContacts()) {
				BackEndREST.autoTestSendRequest(contact, dstUser);
			}
			BackEndREST.autoTestAcceptAllRequest(dstUser);
		}
	}
}
