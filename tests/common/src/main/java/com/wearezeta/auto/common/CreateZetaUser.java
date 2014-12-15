package com.wearezeta.auto.common;

import javax.mail.*;
import javax.ws.rs.core.UriBuilderException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.email.EmailHeaders;
import com.wearezeta.auto.common.email.IMAPSMailbox;
import com.wearezeta.auto.common.email.MBoxChangesListener;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class CreateZetaUser {

	private static final Logger log = ZetaLogger.getLog(CreateZetaUser.class
			.getSimpleName());

	public static final String MAILS_FOLDER = "Inbox";
	public static final int ACTIVATION_TIMEOUT = 120; // seconds

	public static final ArrayList<String> failedToActivate = new ArrayList<String>();

	public static String getMboxName() throws IOException {
		return CommonUtils.getDefaultEmailFromConfig(CreateZetaUser.class);
	}

	public static String getMboxPassword() throws IOException {
		return CommonUtils.getDefaultPasswordFromConfig(CreateZetaUser.class);
	}

	public static String registerUserAndReturnMail() throws Exception {
		String nextSuffix = null;
		String regMail = null;
		Map<String, String> nextUser = generateNextUser(getMboxName(),
				getMboxPassword());
		for (Map.Entry<String, String> entry : nextUser.entrySet()) {
			nextSuffix = entry.getKey();
			regMail = entry.getValue();
		}

		IMAPSMailbox mbox = getMboxInstance(getMboxName(), getMboxPassword());
		Map<String, String> expectedHeaders = new HashMap<String, String>();
		expectedHeaders.put("Delivered-To", regMail);
		MBoxChangesListener listener = mbox.startMboxListener(expectedHeaders);
		BackEndREST.registerNewUser(regMail, nextSuffix, getMboxPassword());
		activateRegisteredUser(listener);

		return regMail;
	}

	public static void activateRegisteredUser(MBoxChangesListener listener)
			throws TimeoutException, InterruptedException, MessagingException,
			IllegalArgumentException, UriBuilderException,
			BackendRequestException, IOException {
		EmailHeaders registrationInfo = IMAPSMailbox.getFilteredMessageHeaders(
				listener, ACTIVATION_TIMEOUT);
		BackEndREST.activateNewUser(registrationInfo.getXZetaKey(),
				registrationInfo.getXZetaCode());
		log.debug(String.format("User %s is activated",
				registrationInfo.getLastUserEmail()));
	}

	public static Map<String, String> generateNextUser(String mail,
			String password) {
		String suffix = CommonUtils.generateGUID();
		suffix = suffix.replace("-", "");
		String regMail = null;
		Map<String, String> user = new LinkedHashMap<String, String>();

		regMail = setRegMail(mail, suffix);
		user.put(suffix, regMail);

		log.debug("Generated credentials for new user registration: " + regMail
				+ ":" + password);

		return user;
	}

	public static IMAPSMailbox getMboxInstance(String user, String password)
			throws MessagingException, IOException, InterruptedException {
		return new IMAPSMailbox(
				CommonUtils
						.getDefaultEmailServerFromConfig(CreateZetaUser.class),
				MAILS_FOLDER, user, password);
	}

	public static List<EmailHeaders> getLastMailHeaders(String user,
			String password, int messageCount) throws MessagingException,
			IOException, InterruptedException {
		IMAPSMailbox mbox = getMboxInstance(user, password);
		return mbox.getLastMailHeaders(messageCount);
	}

	private static String setRegMail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}
}