package com.wearezeta.auto.common.email;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.wearezeta.auto.common.CommonUtils;

public class MessagingUtils {
	public static final String DELIVERED_TO_HEADER = "Delivered-To";

	public static String msgToString(Message msg) throws IOException, MessagingException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		msg.writeTo(output);
		return output.toString();
	}

	public static String normalizeEmail(String originalEmail) {
		final int plusPos = originalEmail.indexOf("+");
		if (plusPos > 0) {
			return originalEmail.substring(0, plusPos) + originalEmail.substring(
					originalEmail.indexOf("@"), originalEmail.length()
			);
		}
		return originalEmail;
	}

	public static Message stringToMsg(String rawMsg) throws MessagingException {
		final Session session = Session.getInstance(System.getProperties(), null);
		return new MimeMessage(session, new ByteArrayInputStream(rawMsg.getBytes()));
	}
	
	public static String extractDeliveredToValue(String defaultValue, Map<String, String> headers) throws Exception {
		// Get emails for all recipients by default
		String deliveredTo = defaultValue;
		for (Map.Entry<String, String> pair : headers.entrySet()) {
			if (pair.getKey().equals(DELIVERED_TO_HEADER)) {
				deliveredTo = pair.getValue();
				break;
			}
		}
		return deliveredTo;
	}

	public static String extractDeliveredToValue(String defaultValue, Message msg) throws Exception {
		// Get emails for all recipients by default
		String deliveredTo = defaultValue;
		@SuppressWarnings("unchecked")
		final Enumeration<Header> hdrs = msg.getAllHeaders();
		while (hdrs.hasMoreElements()) {
			final Header hdr = hdrs.nextElement();
			if (hdr.getName().equals(DELIVERED_TO_HEADER)) {
				deliveredTo = hdr.getValue();
				break;
			}
		}
		return deliveredTo;
	}
	
	public static String getServerHost() throws Exception {
		return CommonUtils.getDefaultEmailServerFromConfig(MessagingUtils.class);
	}

	public static String getDefaultAccountName() throws Exception {
		return CommonUtils.getDefaultEmailFromConfig(MessagingUtils.class);
	}

	public static String getDefaultAccountPassword() throws Exception {
		return CommonUtils.getDefaultPasswordFromConfig(MessagingUtils.class);
	}

    public static String getSpecialAccountName() throws Exception {
        return CommonUtils.getSpecialEmailFromConfig(MessagingUtils.class);
    }

    public static String getSpecialAccountPassword() throws Exception {
        return CommonUtils.getSpecialPasswordFromConfig(MessagingUtils.class);
    }

    public static String generateEmail(String basemail, String suffix) {
        return basemail.split("@")[0].concat("+").concat(suffix)
                .concat("@").concat(basemail.split("@")[1]);
    }
}
