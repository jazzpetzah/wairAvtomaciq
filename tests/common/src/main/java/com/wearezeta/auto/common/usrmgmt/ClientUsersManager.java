package com.wearezeta.auto.common.usrmgmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import com.google.common.base.Throwables;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.backend.BackendRequestException;
import com.wearezeta.auto.common.log.ZetaLogger;

public class ClientUsersManager {
	private static final int NUMBER_OF_REGISTRATION_RETRIES = 3;

	public static final Function<Integer, String> NAME_ALIAS_TEMPLATE = idx -> String
			.format("user%dName", idx);
	public static final Function<Integer, String> PASSWORD_ALIAS_TEMPLATE = idx -> String
			.format("user%dPassword", idx);
	public static final Function<Integer, String> EMAIL_ALIAS_TEMPLATE = idx -> String
			.format("user%dEmail", idx);
	public static final Function<Integer, String> PHONE_NUMBER_ALIAS_TEMPLATE = idx -> String
			.format("user%dPhoneNumber", idx);
	private static final int MAX_USERS = 1001;

	private static final Logger log = ZetaLogger
			.getLog(ClientUsersManager.class.getSimpleName());

	public static void setClientUserAliases(ClientUser user,
			String[] nameAliases, String[] passwordAliases,
			String[] emailAliases, String[] phoneNumberAliases) {
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
			for (String emailAlias : emailAliases) {
				user.addEmailAlias(emailAlias);
			}
		}
		if (phoneNumberAliases != null && phoneNumberAliases.length > 0) {
			user.clearPhoneNumberAliases();
			for (String phoneNumberAlias : phoneNumberAliases) {
				user.addPhoneNumberAlias(phoneNumberAlias);
			}
		}
	}

	private void resetClientsList(List<ClientUser> dstList, int maxCount)
			throws Exception {
		this.selfUser = null;
		dstList.clear();
		for (int userIdx = 0; userIdx < maxCount; userIdx++) {
			ClientUser pendingUser = new ClientUser();
			final String[] nameAliases = new String[] { NAME_ALIAS_TEMPLATE
					.apply(userIdx + 1) };
			final String[] passwordAliases = new String[] { PASSWORD_ALIAS_TEMPLATE
					.apply(userIdx + 1) };
			final String[] emailAliases = new String[] { EMAIL_ALIAS_TEMPLATE
					.apply(userIdx + 1) };
			final String[] phoneNumberAliases = new String[] { PHONE_NUMBER_ALIAS_TEMPLATE
					.apply(userIdx + 1) };
			setClientUserAliases(pendingUser, nameAliases, passwordAliases,
					emailAliases, phoneNumberAliases);
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

	public void resetUsers() throws Exception {
		this.selfUser = null;
		this.resetClientsList(users, MAX_USERS);
	}

	private static ClientUsersManager instance = null;

	private ClientUsersManager() throws Exception {
		resetClientsList(this.users, MAX_USERS);
	}

	public synchronized static ClientUsersManager getInstance() {
		if (instance == null) {
			try {
				instance = new ClientUsersManager();
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		}
		return instance;
	}

	public static enum FindBy {
		NAME("Name"), PASSWORD("Password"), EMAIL("Email"), PHONE_NUMBER(
				"Phone Number"), NAME_ALIAS("Name Alias(es)"), PASSWORD_ALIAS(
				"Password Alias(es)"), EMAIL_ALIAS("Email Alias(es)"), PHONENUMBER_ALIAS(
				"Phone Number Alias(es)");

		private final String name;

		private FindBy(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public ClientUser findUserByPasswordAlias(String alias)
			throws NoSuchUserException {
		return findUserBy(alias, new FindBy[] { FindBy.PASSWORD_ALIAS });
	}

	public ClientUser findUserByPhoneNumberOrPhoneNumberAlias(String alias)
			throws NoSuchUserException {
		return findUserBy(alias, new FindBy[] { FindBy.PHONE_NUMBER,
				FindBy.PHONENUMBER_ALIAS });
	}

	public ClientUser findUserByNameOrNameAlias(String alias)
			throws NoSuchUserException {
		return findUserBy(alias,
				new FindBy[] { FindBy.NAME, FindBy.NAME_ALIAS });
	}

	public ClientUser findUserByEmailOrEmailAlias(String alias)
			throws NoSuchUserException {
		return findUserBy(alias, new FindBy[] { FindBy.EMAIL,
				FindBy.EMAIL_ALIAS });
	}

	public ClientUser findUserBy(String searchStr, FindBy[] findByCriterias)
			throws NoSuchUserException {
		for (FindBy findBy : findByCriterias) {
			try {
				return findUserBy(searchStr, findBy);
			} catch (NoSuchUserException e) {
				// Ignore silently
			}
		}
		throw new NoSuchUserException(String.format(
				"User '%s' could not be found by '%s'", searchStr,
				StringUtils.join(findByCriterias, ", ")));
	}

	public ClientUser findUserBy(String searchStr, FindBy findByCriteria)
			throws NoSuchUserException {
		searchStr = searchStr.trim();
		for (ClientUser user : users) {
			Set<String> aliases = new HashSet<String>();
			if (findByCriteria == FindBy.NAME_ALIAS) {
				aliases = user.getNameAliases();
			} else if (findByCriteria == FindBy.EMAIL_ALIAS) {
				aliases = user.getEmailAliases();
			} else if (findByCriteria == FindBy.PASSWORD_ALIAS) {
				aliases = user.getPasswordAliases();
			} else if (findByCriteria == FindBy.PHONENUMBER_ALIAS) {
				aliases = user.getPhoneNumberAliases();
			} else if (findByCriteria == FindBy.NAME) {
				if (user.getName().equalsIgnoreCase(searchStr)) {
					return user;
				}
			} else if (findByCriteria == FindBy.EMAIL) {
				if (user.getEmail().equalsIgnoreCase(searchStr)) {
					return user;
				}
			} else if (findByCriteria == FindBy.PASSWORD) {
				if (user.getPassword().equals(searchStr)) {
					return user;
				}
			} else if (findByCriteria == FindBy.PHONE_NUMBER) {
				if (user.getPhoneNumber().toString().equals(searchStr)) {
					return user;
				}
			} else {
				throw new RuntimeException(String.format(
						"Unknown FindBy criteria %s", findByCriteria));
			}
			for (String currentAlias : aliases) {
				if (currentAlias.equalsIgnoreCase(searchStr)) {
					return user;
				}
			}
		}
		throw new NoSuchUserException(String.format(
				"User '%s' could not be found by '%s'", searchStr,
				findByCriteria));
	}

	public String replaceAliasesOccurences(String srcStr, FindBy findByAliasType) {
		String result = srcStr;
		for (ClientUser dstUser : users) {
			Set<String> aliases = new HashSet<String>();
			String replacement = null;
			if (findByAliasType == FindBy.NAME_ALIAS) {
				aliases = dstUser.getNameAliases();
				replacement = dstUser.getName();
			} else if (findByAliasType == FindBy.EMAIL_ALIAS) {
				aliases = dstUser.getEmailAliases();
				replacement = dstUser.getEmail();
			} else if (findByAliasType == FindBy.PASSWORD_ALIAS) {
				aliases = dstUser.getPasswordAliases();
				replacement = dstUser.getPassword();
			} else if (findByAliasType == FindBy.PHONENUMBER_ALIAS) {
				aliases = dstUser.getPhoneNumberAliases();
				replacement = dstUser.getPhoneNumber().toString();
			} else {
				throw new RuntimeException(String.format(
						"Unsupported FindBy criteria '%s'", findByAliasType));
			}
			for (String alias : aliases) {
				result = result.replaceAll("(?i)\\b(" + alias + ")\\b",
						replacement);
			}
		}
		return result;
	}

	private final Random random = new Random();

	// ! Mutates the users list
	private void generateUsers(List<ClientUser> usersToCreate,
			final RegistrationStrategy strategy) throws Exception {
		ExecutorService executor = Executors
				.newFixedThreadPool(CommonUtils.MAX_PARALLEL_USER_CREATION_TASKS);

		final AtomicInteger createdClientsCount = new AtomicInteger(0);
		for (final ClientUser userToCreate : usersToCreate) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					int retryNumber = 1;
					int intervalSeconds = 1;
					do {
						long sleepInterval = 1000;
						try {
							BackendAPIWrappers.createUserViaBackdoor(
									userToCreate, retryNumber, strategy);
							createdClientsCount.incrementAndGet();
							return;
						} catch (BackendRequestException e) {
							e.printStackTrace();
							if (e.getReturnCode() == HttpStatus.SC_METHOD_FAILURE) {
								sleepInterval = (intervalSeconds + random
										.nextInt(BackendAPIWrappers.BACKEND_ACTIVATION_TIMEOUT)) * 2000;
								intervalSeconds *= 2;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						log.debug(String
								.format("Failed to create user '%s'. Retrying (%d of %d)...",
										userToCreate.getName(), retryNumber,
										NUMBER_OF_REGISTRATION_RETRIES));
						try {
							Thread.sleep(sleepInterval);
						} catch (InterruptedException ex) {
							return;
						}
						retryNumber++;
					} while (retryNumber <= NUMBER_OF_REGISTRATION_RETRIES);
				}
			});
			executor.execute(worker);
		}
		executor.shutdown();
		final int usersCreationTimeout = BackendAPIWrappers.BACKEND_ACTIVATION_TIMEOUT
				* usersToCreate.size() * NUMBER_OF_REGISTRATION_RETRIES * 3;
		if (!executor.awaitTermination(usersCreationTimeout, TimeUnit.SECONDS)) {
			throw new RuntimeException(
					String.format(
							"The backend has failed to prepare predefined users within %d seconds timeout",
							usersCreationTimeout));
		}
		if (createdClientsCount.get() != usersToCreate.size()) {
			throw new RuntimeException(
					"Failed to create new users or contacts on the backend");
		}
	}

	public static class TooManyUsersToCreateException extends Exception {
		private static final long serialVersionUID = -7730445785978830114L;

		TooManyUsersToCreateException(String msg) {
			super(msg);
		}
	}

	public void createUsersOnBackend(int count, RegistrationStrategy strategy)
			throws Exception {
		this.resetClientsList(this.users, MAX_USERS);
		if (count > MAX_USERS) {
			throw new TooManyUsersToCreateException(
					String.format(
							"Maximum allowed number of users to create is %d. Current number is %d",
							MAX_USERS, count));
		}
		generateUsers(this.users.subList(0, count), strategy);
	}

	private static String[] SELF_USER_NAME_ALISES = new String[] { "I", "Me",
			"Myself" };
	private static String[] SELF_USER_PASSWORD_ALISES = new String[] { "myPassword" };
	private static String[] SELF_USER_EMAIL_ALISES = new String[] { "myEmail" };
	private static String[] SELF_USER_PHONE_NUMBER_ALISES = new String[] { "myPhoneNumber" };

	private ClientUser selfUser = null;

	public void setSelfUser(ClientUser usr) {
		if (!this.users.contains(usr)) {
			throw new RuntimeException(String.format(
					"User %s should be one of precreated users!",
					usr.toString()));
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
			for (String phoneNumberAlias : SELF_USER_PHONE_NUMBER_ALISES) {
				if (this.selfUser.getPhoneNumberAliases().contains(
						phoneNumberAlias)) {
					this.selfUser.removePhoneNumberAlias(phoneNumberAlias);
				}
			}
		}
		this.selfUser = usr;
		for (String nameAlias : SELF_USER_NAME_ALISES) {
			this.selfUser.addNameAlias(nameAlias);
		}
		for (String passwordAlias : SELF_USER_PASSWORD_ALISES) {
			this.selfUser.addPasswordAlias(passwordAlias);
		}
		for (String emailAlias : SELF_USER_EMAIL_ALISES) {
			this.selfUser.addEmailAlias(emailAlias);
		}
		for (String phoneNumberAlias : SELF_USER_PHONE_NUMBER_ALISES) {
			this.selfUser.addPhoneNumberAlias(phoneNumberAlias);
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

	private static final int SHARED_USERS_MIN_CREATION_INTERVAL = 10;

	private void generateSharedUsers(List<ClientUser> sharedUsers, int count,
			RegistrationStrategy strategy) throws Exception {
		// It is highly possible, that some part (or, probably, all)
		// of these shared users already
		// exist on the backend, so we test which user doesn't exist
		// and create additional pending users

		// We check only each 10-th user to speed up our search
		// Also, this is the reason why we create extra users
		int lastExistingUserIndex = sharedUsers.size() - 1;
		while (lastExistingUserIndex >= 0) {
			try {
				BackendAPIWrappers.tryLoginByUser(sharedUsers
						.get(lastExistingUserIndex));
				for (int idx = 0; idx <= lastExistingUserIndex; idx++) {
					sharedUsers.get(idx).setUserState(UserState.Created);
				}
				if (lastExistingUserIndex == sharedUsers.size() - 1) {
					// All users already exist on the backend
					return;
				} else {
					// It is necessary to create several more users
					break;
				}
			} catch (BackendRequestException e) {
				if (e.getReturnCode() == HttpStatus.SC_FORBIDDEN) {
					lastExistingUserIndex -= SHARED_USERS_MIN_CREATION_INTERVAL;
				} else {
					throw e;
				}
			}
		}

		this.generateUsers(
				sharedUsers.subList(lastExistingUserIndex + 1,
						sharedUsers.size()), strategy);
	}

	public void appendSharedUsers(String commonNamePrefix, int count)
			throws Exception {
		if (count <= 0) {
			throw new RuntimeException(
					"Count of users should be positive integer number");
		}
		if (commonNamePrefix.length() == 0) {
			throw new RuntimeException(
					"Common name prefix value could not be empty");
		}

		List<ClientUser> sharedUsers = new ArrayList<ClientUser>();
		final int ceiledCount = count / SHARED_USERS_MIN_CREATION_INTERVAL
				* SHARED_USERS_MIN_CREATION_INTERVAL
				+ SHARED_USERS_MIN_CREATION_INTERVAL;
		resetClientsList(sharedUsers, ceiledCount);
		for (int sharedUserIdx = 0; sharedUserIdx < sharedUsers.size(); sharedUserIdx++) {
			ClientUser dstUser = sharedUsers.get(sharedUserIdx);
			final String name = commonNamePrefix
					+ Integer.toString(sharedUserIdx + 1);
			dstUser.setName(name);
			dstUser.addNameAlias(name);
			dstUser.setEmail(ClientUser.generateEmail(name));
			dstUser.addEmailAlias(name + "Email");
		}
		generateSharedUsers(sharedUsers, ceiledCount,
				RegistrationStrategy.ByEmail);

		// Appending shared users to the end of "normal" users list
		int appendPos = getCreatedUsers().size();
		for (int sharedUserIdx = 0; sharedUserIdx < count; sharedUserIdx++) {
			this.users.set(appendPos, sharedUsers.get(sharedUserIdx));
			appendPos++;
		}
	}

	public void appendSharedUsers(String commonNamePrefix,
			PhoneNumber startingPhoneNumber, int count) throws Exception {
		if (count <= 0) {
			throw new RuntimeException(
					"Count of users should be positive integer number");
		}
		if (commonNamePrefix.length() == 0) {
			throw new RuntimeException(
					"Common name prefix value could not be empty");
		}

		List<ClientUser> sharedUsers = new ArrayList<ClientUser>();
		final int ceiledCount = count / SHARED_USERS_MIN_CREATION_INTERVAL
				* SHARED_USERS_MIN_CREATION_INTERVAL
				+ SHARED_USERS_MIN_CREATION_INTERVAL;
		resetClientsList(sharedUsers, ceiledCount);
		for (int sharedUserIdx = 0; sharedUserIdx < sharedUsers.size(); sharedUserIdx++) {
			ClientUser dstUser = sharedUsers.get(sharedUserIdx);
			final String name = commonNamePrefix
					+ Integer.toString(sharedUserIdx + 1);
			dstUser.setName(name);
			dstUser.addNameAlias(name);
			dstUser.setEmail(ClientUser.generateEmail(name));
			dstUser.addEmailAlias(name + "Email");
			dstUser.setPhoneNumber(PhoneNumber.increasedBy(startingPhoneNumber,
					BigInteger.valueOf(sharedUserIdx)));
			dstUser.addPhoneNumberAlias(name + "PhoneNumber");
		}
		generateSharedUsers(sharedUsers, ceiledCount,
				RegistrationStrategy.ByPhoneNumber);

		// Appending shared users to the end of "normal" users list
		int appendPos = getCreatedUsers().size();
		for (int sharedUserIdx = 0; sharedUserIdx < count; sharedUserIdx++) {
			this.users.set(appendPos, sharedUsers.get(sharedUserIdx));
			appendPos++;
		}
	}

	/**
	 * Add custom user to the end of internal list of users, so then it is
	 * possible to use it in "standard" steps
	 * 
	 * Be careful when use this method. Make sure, that this user has been
	 * already created and has all the necessary aliases already set
	 * 
	 * @param user
	 *            prepared ClientUser instance
	 * 
	 * @return index in users list
	 */
	public int appendCustomUser(ClientUser user) {
		int appendPos = getCreatedUsers().size();
		this.users.set(appendPos, user);
		return appendPos;
	}
}
