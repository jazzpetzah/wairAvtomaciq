package email_notifier;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import jersey.repackaged.com.google.common.base.Throwables;

import com.wearezeta.auto.common.email.MessagingUtils;

public class NotificationSender {
	private static NotificationSender instance = null;

	private NotificationSender() {
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		try {
			username = MessagingUtils.getAccountName();
			password = MessagingUtils.getAccountPassword();
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private Properties props = new Properties();
	private String username;
	private String password;

	public synchronized static NotificationSender getInstance() {
		if (instance == null) {
			instance = new NotificationSender();
		}
		return instance;
	}

	public void send(String recipients, String subject, String text)
			throws AddressException, MessagingException {
		send(Arrays.asList(recipients.split(",")), subject, text);
	}

	public void send(List<String> recipients, String subject, String text)
			throws AddressException, MessagingException {
		final Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		final Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(username));
		message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(String.join(",", recipients)));
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);
	}
}
