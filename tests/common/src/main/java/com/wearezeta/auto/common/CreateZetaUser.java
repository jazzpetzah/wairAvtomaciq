package com.wearezeta.auto.common;

import javax.mail.*;
import javax.ws.rs.core.UriBuilderException;

import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CreateZetaUser {
	public static final String MAILS_FOLDER = "Inbox";
	public static final int ACTIVATION_TIMEOUT = 10; 

	public static String registerUserAndReturnMail() throws IOException,
			MessagingException, IllegalArgumentException, UriBuilderException,
			JSONException, BackendRequestException, InterruptedException {
		String nextSuffix = null;
		String regMail = null;
		String mail = CommonUtils
				.getDefaultEmailFromConfig(CreateZetaUser.class);
		String password = CommonUtils
				.getDefaultPasswordFromConfig(CreateZetaUser.class);

		Map<String, String> nextUser = generateNextUser(mail, password);

		for (Map.Entry<String, String> entry : nextUser.entrySet()) {
			nextSuffix = entry.getKey();
			regMail = entry.getValue();
		}

		BackEndREST.registerNewUser(regMail, nextSuffix, password);
		activateRegisteredUser(regMail, ACTIVATION_TIMEOUT, mail, password);

		return regMail;
	}

	public static Map<String, String> generateNextUser(String mail,
			String password) {
		String suffix = CommonUtils.generateGUID();
		suffix = suffix.replace("-", "");
		String regMail = null;
		Map<String, String> user = new LinkedHashMap<String, String>();

		regMail = setRegMail(mail, suffix);
		System.out.println(regMail);
		user.put(suffix, regMail);
		return user;
	}

	public static boolean activateRegisteredUser(String registeredUserMail,
			int timeout, String mail, String password)
			throws MessagingException, IOException, IllegalArgumentException,
			UriBuilderException, BackendRequestException, InterruptedException {
		final int ATTEMPS_NUMBER = 5;
		for (int i = 0; i < ATTEMPS_NUMBER; i++) {
			List<EmailHeaders> newMails = getLastMailHeaders(mail, password, 5);
			for (EmailHeaders newMailContent : newMails) {
				if (newMailContent.getLastUserEmail()
						.equals(registeredUserMail)) {
					BackEndREST.activateNewUser(newMailContent.getXZetaKey(),
							newMailContent.getXZetaCode());
					return true;
				}
			}
			sleep(timeout);
		}
		return false;
	}

	public static List<EmailHeaders> getLastMailHeaders(String user,
			String password, int messageCount) throws MessagingException,
			IOException, InterruptedException {
		IMAPSMailbox mbox = new IMAPSMailbox(
				CommonUtils
						.getDefaultEmailServerFromConfig(CreateZetaUser.class),
				MAILS_FOLDER, user, password);
		mbox.open();
		try {
			return mbox.getLastMailHeaders(messageCount);
		} finally {
			mbox.close();
		}
	}

	private static String setRegMail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}

	private static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}