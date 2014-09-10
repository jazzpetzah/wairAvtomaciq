package com.wearezeta.auto.sync;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.UriBuilderException;

import org.json.JSONException;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.BackendRequestException;
import com.wearezeta.auto.common.ClientUser;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.CreateZetaUser;
import com.wearezeta.auto.common.UsersState;

public class SyncEngineUtil {

	public static final int MAX_PARALLEL_USER_CREATION_TASKS = 3;

	public static final int USERS_CREATION_TIMEOUT = 60 * 5; // seconds

	private static final Random rand = new Random();
	public static final int BACKEND_SYNC_TIMEOUT = 5000 + rand.nextInt(4000); // milliseconds

	public static final ArrayList<ClientUser> usersList = new ArrayList<ClientUser>();

	public static final String YOUR_USER_1 = "1stUser";
	public static final String YOUR_USER_2 = "2ndUser";
	public static final String YOUR_USER_3 = "3rdUser";
	public static final String USERS_PASSWORD = "usersPassword";

	public static final String CHAT_NAME = "SyncEngineTest";
	
	public static final String[] platforms = new String[] {
		CommonUtils.PLATFORM_NAME_OSX,
		CommonUtils.PLATFORM_NAME_ANDROID,
		CommonUtils.PLATFORM_NAME_IOS
	};
	
	public static void generateUsers(int numberOfUsers)
			throws BackendRequestException, InterruptedException {
		ExecutorService executor = Executors
				.newFixedThreadPool(MAX_PARALLEL_USER_CREATION_TASKS);
		for (int i = 0; i < numberOfUsers; i++) {
			Runnable worker = new Thread(new Runnable() {
				public void run() {
					try {
						final String email = CreateZetaUser
								.registerUserAndReturnMail();
						if (email == null)
							return;
						ClientUser user = new ClientUser();
						user.setEmail(email);
						user.setPassword(CommonUtils
								.getDefaultPasswordFromConfig(CommonUtils.class));
						user.setUserState(UsersState.Created);
						usersList.add(user);
					} catch (Exception e) {
						e.printStackTrace();
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
		if (usersList.size() != numberOfUsers) {
			throw new BackendRequestException(
					"Failed to create new users or contacts on the backend");
		}
	}

	public static void connectUsers() throws IllegalArgumentException,
			UriBuilderException, IOException, JSONException,
			BackendRequestException, InterruptedException {
		for (ClientUser yourUser : usersList) {
			yourUser = BackEndREST.loginByUser(yourUser);
			yourUser = BackEndREST.getUserInfo(yourUser);
		}
		for (int i = 0; i < usersList.size() - 1; i++) {
			for (int j = i + 1; j < usersList.size(); j++) {
				BackEndREST.autoTestSendRequest(usersList.get(j),
						usersList.get(i));
				usersList.get(j).setUserState(UsersState.RequestSend);

			}
			BackEndREST.autoTestAcceptAllRequest(usersList.get(i));
			ClientUser user = usersList.get(i);
			user.setUserState(UsersState.AllContactsConnected);
			usersList.set(i, user);
		}
	}

	public static void usePrecreatedUsers() {
		final String DEFAULT_PASSWORD = "aqa123456";
		final String EMAIL_TEMPLATE = "smoketester+%s@wearezeta.com";

		String[] userIds = new String[] { "c0c62e78531b443dbb94efe0e8a3ddf6",
				"d7016a6d68a74d219feb981a117d1e14",
				"9bd7f216cb064ce983890ba9f39ed91d" };
		for (String userId : userIds) {
			ClientUser user = new ClientUser(String.format(EMAIL_TEMPLATE,
					userId), DEFAULT_PASSWORD, userId,
					UsersState.AllContactsConnected);
			usersList.add(user);
		}
	}

	public static boolean getCommonGenerateUsersFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "common.generate.users"));
	}

	public static boolean getOSXClientEnabledFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "osx.client.enabled"));
	}

	public static boolean getAndroidClientEnabledFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "android.client.enabled"));
	}

	public static boolean getIosClientEnabledFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "ios.client.enabled"));
	}
	
	public static boolean getOSXBackendSenderFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "osx.backend.sender"));
	}

	public static boolean getAndroidBackendSenderFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "android.backend.sender"));
	}

	public static boolean getIosBackendSenderFromConfig(Class<?> c)
			throws IOException {
		return Boolean.parseBoolean(CommonUtils.getValueFromConfig(c, "ios.backend.sender"));
	}
	
	public static int getAcceptanceMaxSendingIntervalFromConfig(Class<?> c)
			throws IOException {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c, "acceptance.max.send.interval.sec"));
	}
	
	public static int getClientMessagesCount(Class<?> c)
			throws IOException {
		return Integer.parseInt(CommonUtils.getValueFromConfig(c, "acceptance.messages.count"));
	}

	public static String retrieveRealUserContactPasswordValue(String value) {
		Map<String, String> replacementMap = new LinkedHashMap<String, String>();
		if (usersList.size() > 0) {
			replacementMap.put(YOUR_USER_1, usersList.get(0).getName());
			replacementMap.put(YOUR_USER_2, usersList.get(1).getName());
			replacementMap.put(YOUR_USER_3, usersList.get(2).getName());
			replacementMap.put(USERS_PASSWORD, usersList.get(0).getPassword());
		}

		String result = value;
		for (Entry<String, String> replacementEntry : replacementMap.entrySet()) {
			if (result.contains(replacementEntry.getKey())) {
				result = result.replace(replacementEntry.getKey(),
						replacementEntry.getValue());
			}
		}
		return result;
	}
}
