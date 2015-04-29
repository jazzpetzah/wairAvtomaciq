package com.wearezeta.auto.common.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.wearezeta.auto.common.CommonUtils;

public class MessagingUtils {
	public static final String DELIVERED_TO_HEADER = "Delivered-To";

	public static String msgToString(Message msg) throws IOException,
			MessagingException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		msg.writeTo(output);
		return output.toString();
	}

	public static Message stringToMsg(String rawMsg) throws MessagingException {
		final Session session = Session.getInstance(System.getProperties(),
				null);
		return new MimeMessage(session, new ByteArrayInputStream(
				rawMsg.getBytes()));
	}

	public static String getServerHost() throws Exception {
		return CommonUtils
				.getDefaultEmailServerFromConfig(MessagingUtils.class);
	}

	public static String getAccountName() throws Exception {
		return CommonUtils.getDefaultEmailFromConfig(MessagingUtils.class);
	}

	public static String getAccountPassword() throws Exception {
		return CommonUtils.getDefaultPasswordFromConfig(MessagingUtils.class);
	}
}
