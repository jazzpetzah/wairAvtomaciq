package com.wearezeta.auto.common.email;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

public class MessagingUtils {
	public static String msgToString(Message msg) throws IOException,
			MessagingException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		msg.writeTo(output);
		return output.toString();
	}

}
