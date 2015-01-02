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
import java.util.LinkedHashMap;
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

	public static String registerUserAndReturnMail() throws Exception {
		String nextSuffix = null;
		String regMail = null;
		Map<String, String> nextUser = generateUniqUserCredentials(
				getMboxName());
		for (Map.Entry<String, String> entry : nextUser.entrySet()) {
			nextSuffix = entry.getKey();
			regMail = entry.getValue();
		}

		IMAPSMailbox mbox = getMboxInstance();
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", regMail);
		MBoxChangesListener listener = mbox.startMboxListener(expectedHeaders);
		BackEndREST.registerNewUser(regMail, nextSuffix, getMboxPassword());
		activateRegisteredUser(listener);

		return regMail;
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

	public static Map<String, String> generateUniqUserCredentials(String mail) {
		String userId = generateUserId();
		String regMail = generateUniqEmail(mail, userId);

		Map<String, String> user = new LinkedHashMap<String, String>();
		user.put(userId, regMail);
		log.debug("Generated credentials for new user registration: " + regMail);
		return user;
	}

	public static IMAPSMailbox getMboxInstance()
			throws MessagingException, IOException, InterruptedException {
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

	private static String generateUserId() {
		return CommonUtils.generateGUID().replace("-", "");
	}

	private static String generateUniqEmail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}
}