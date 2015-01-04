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

import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;

public class ClientUsersManager {
	private static final int MAX_PARALLEL_USER_CREATION_TASKS = 5;
	private static final int NUMBER_OF_REGISTRATION_RETRIES = 5;
	private static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	private static final String NAME_ALIAS_TEMPLATE = "user%dName";
	private static final String PASSWORD_ALIAS_TEMPLATE = "user%dPassword";
	private static final String EMAIL_ALIAS_TEMPLATE = "user%dEmail";
	private static final int MAX_USERS = 1001;

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

	private void resetClientsList(List<ClientUser> dstList) throws IOException {
		this.selfUser = null;
		dstList.clear();
		for (int userIdx = 0; userIdx < MAX_USERS; userIdx++) {
			ClientUser pendingUser = new ClientUser();
			final String[] nameAliases = new String[] { String.format(
					NAME_ALIAS_TEMPLATE, userIdx + 1) };
			final String[] passwordAliases = new String[] { String.format(
					PASSWORD_ALIAS_TEMPLATE, userIdx + 1) };
			final String[] emailAliases = new String[] { String.format(
					EMAIL_ALIAS_TEMPLATE, userIdx + 1) };
			setClientUserAliases(pendingUser, nameAliases, passwordAliases,
					emailAliases);
			dstList.add(pendingUser);
		}
	}

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

	private static ClientUsersManager instance = null;

	private ClientUsersManager() throws IOException {
		resetClientsList(this.users);
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
		return this.users;
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
				throw new RuntimeException(String.format(
						"Unknown alias type %s", aliasType));
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

	// ! Mutates the users list
	private void generateUsers(List<ClientUser> usersToCreate) throws Exception {
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
							BackendAPIWrappers.createUser(userToCreate);
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

	public static class TooManyUsersToCreateException extends Exception {
		private static final long serialVersionUID = -7730445785978830114L;

		TooManyUsersToCreateException(String msg) {
			super(msg);
		}
	}

	public void createUsersOnBackend(int count)
			throws Exception {
		this.resetClientsList(this.users);
		if (count > MAX_USERS) {
			throw new TooManyUsersToCreateException(
					String.format(
							"Maximum allowed number of users to create is %d. Current number is %d",
							MAX_USERS, count));
		}
		generateUsers(this.users.subList(0, count));
	}

	private static String[] SELF_USER_NAME_ALISES = new String[] { "I", "me",
			"myself" };
	private static String[] SELF_USER_PASSWORD_ALISES = new String[] { "myPassword" };
	private static String[] SELF_USER_EMAIL_ALISES = new String[] { "myEmail" };

	private ClientUser selfUser = null;

	public void setSelfUser(ClientUser usr) {
		if (!this.users.contains(usr)) {
			throw new RuntimeException(String.format(
					"User %s should be one of precreated users!", usr));
		}

		if (this.selfUser != null) {
			for (String nameAlias : SELF_USER_NAME_ALISES) {
				if (this.selfUser.getNameAliases().contains(nameAlias)) {
					this.selfUser.removeNameAlias(nameAlias);
				}
			}
			for (String passwordAlias : SELF_USER_PASSWORD_ALISES) {
				if (this.selfUser.getPasswordAliases().contains(passwordAlias)) {
					this.selfUser.removePasswordAlias(passwordAlias);
				}
			}
			for (String emailAlias : SELF_USER_EMAIL_ALISES) {
				if (this.selfUser.getEmailAliases().contains(emailAlias)) {
					this.selfUser.removeEmailAlias(emailAlias);
				}
			}
		}
		this.selfUser = usr;
		if (!this.selfUser.getNameAliases().contains(SELF_USER_NAME_ALISES[0])) {
			for (String nameAlias : SELF_USER_NAME_ALISES) {
				this.selfUser.addNameAlias(nameAlias);
			}
		}
		if (!this.selfUser.getPasswordAliases().contains(
				SELF_USER_PASSWORD_ALISES[0])) {
			for (String passwordAlias : SELF_USER_PASSWORD_ALISES) {
				this.selfUser.addPasswordAlias(passwordAlias);
			}
		}
		if (!this.selfUser.getEmailAliases()
				.contains(SELF_USER_EMAIL_ALISES[0])) {
			for (String emailAlias : SELF_USER_EMAIL_ALISES) {
				this.selfUser.addEmailAlias(emailAlias);
			}
		}
	}

	public static class SelfUserIsNotDefinedException extends Exception {
		private static final long serialVersionUID = 5586439025162442603L;

		public SelfUserIsNotDefinedException(String msg) {
			super(msg);
		}
	}

	public ClientUser getSelfUserOrThrowError()
			throws SelfUserIsNotDefinedException {
		if (this.selfUser == null) {
			throw new SelfUserIsNotDefinedException(
					"Self user should be defined in some previous step!");
		}
		return this.selfUser;
	}

	public ClientUser getSelfUser() {
		return this.selfUser;
	}

	public boolean isSelfUserSet() {
		return (this.selfUser != null);
	}
}
