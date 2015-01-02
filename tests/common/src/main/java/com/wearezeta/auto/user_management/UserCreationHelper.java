package com.wearezeta.auto.user_management;

import javax.mail.*;
import org.apache.log4j.Logger;

import com.wearezeta.auto.common.BackEndREST;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.email.EmailHeaders;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCreationHelper {
	private static final Logger log = ZetaLogger
			.getLog(UserCreationHelper.class.getSimpleName());

	public static final String MAILS_FOLDER = "Inbox";
	public static final int ACTIVATION_TIMEOUT = 120; // seconds

	public static String getMboxName() throws IOException {
		return CommonUtils.getDefaultEmailFromConfig(UserCreationHelper.class);
	}

	private static String getMboxPassword() throws IOException {
		return CommonUtils
				.getDefaultPasswordFromConfig(UserCreationHelper.class);
	}

	// ! Mutates user instance
	public static ClientUser createWireUser(ClientUser user) throws Exception {
		IMAPSMailbox mbox = getMboxInstance();
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

	public static IMAPSMailbox getMboxInstance() throws MessagingException,
			IOException, InterruptedException {
		return new IMAPSMailbox(
				CommonUtils
						.getDefaultEmailServerFromConfig(UserCreationHelper.class),
				MAILS_FOLDER, getMboxName(), getMboxPassword());
	}

	public static List<EmailHeaders> getLastMailHeaders(String user,
			String password, int messageCount) throws MessagingException,
			IOException, InterruptedException {
		IMAPSMailbox mbox = getMboxInstance();
		return mbox.getLastMailHeaders(messageCount);
	}

	public static String generateUniqName() {
		return CommonUtils.generateGUID().replace("-", "");
	}

	public static String generateUniqEmail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}
}