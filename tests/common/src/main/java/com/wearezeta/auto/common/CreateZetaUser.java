package com.wearezeta.auto.common;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import java.io.IOException;
import java.util.Properties;

import com.wearezeta.auto.common.misc.RegExUtil;
import com.wearezeta.auto.common.misc.ShellCommandExecutor;

public class CreateZetaUser {
	
	public static String defaultEmail = "smoketester@wearezeta.com";
	public static String defaultPassword = "aqa123456";
	public static String defaultBackEndUrl = "https://dev-nginz-https.zinfra.io";
	
	private static boolean error = false;
	private static final String CREATE_USER_JSON = "curl -i -XPOST \"%s/register\" "
			+"-H'Content-type: application/json' "
			+"-d'{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}'";
	private static final int MAILS_TO_CHECK = 7;
	private static final String MAILS_FOLDER = "Inbox";
	private static final String MAIL_SERVER = "imap.gmail.com";
	private static final String MAIL_PROTOCOL= "imaps";
	
	/**
	 *  Registers new zeta user and returns its mail.
	 *  
	 * @param mail
	 *            = smoketester@wearezeta.com
	 * @param password
	 *            = aqa123456
	 * @param backendUrl
	 *            = "https://armada-dev.z-infra.com" (old backend) <br>
	 *            https://dev-nginz-https.zinfra.io (new backend)
	 */
	private static String mail;
	private static String password;
	private static String backend_url;
	public static void main(String[] args) {
	  
	  mail = "smoketester@wearezeta.com"; 				// smoketester@wearezeta.com
	  password = "aqa123456"; 							// aqa123456
	  backend_url = "https://dev-nginz-https.zinfra.io"; 	//new backend:https://dev-nginz-https.zinfra.io
	  													//old backend: https://armada-dev.z-infra.com
	  String test=registerUserAndReturnMail(mail, password, backend_url);
	  System.out.println("Registerd mail: "+test); 
	  }
	
	public static String registerUserAndReturnMail(String mail, String password, String backendUrl) {
		String lastSuffix = null;
		String nextSuffixId = "1";
		String lastSuffixName = "aqa";
		String nextSuffix = lastSuffixName.concat(nextSuffixId);
		String mailContent = getMailContent(MAIL_PROTOCOL, MAIL_SERVER, mail,
				password, 20, false, "address.*\\S+@\\S+.*account");
		if (mailContent != null) {
			lastSuffix = RegExUtil
					.getStringByRegEx(mailContent, "\\+.*@", true);
			if (lastSuffix != null) {
				String lastSuffixId = RegExUtil.getStringByRegEx(lastSuffix,
						"\\d+", true);
				lastSuffixName = RegExUtil.getStringByRegEx(lastSuffix, "\\w+",
						true);
				lastSuffixName = RegExUtil.getStringByRegEx(lastSuffix, "\\D+",
						true);
				lastSuffixName = lastSuffixName.replace("+", "");
				nextSuffixId = Integer
						.toString(Integer.valueOf(lastSuffixId) + 1);
				nextSuffix = lastSuffixName.concat(nextSuffixId);
			} 
		}

		try {
			int i = 0;
			error = true;
			while (i < 20 && error) {
				i++;
				String regMail = setRegMail(mail, nextSuffix);
				System.out.println("Next regMail: " + regMail);
				String request = setRegRequest(regMail, password, backendUrl,
						nextSuffix);
				//System.out.println("request: " + request);
				error = ShellCommandExecutor.runCommand("",
						request, "Created");
				if (error)
					nextSuffixId = Integer.toString(Integer
							.valueOf(nextSuffixId) + 1);
				nextSuffix = lastSuffixName.concat(nextSuffixId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (!error) {
			String regURL = getMailContent(MAIL_PROTOCOL, MAIL_SERVER, mail,
					password, 20, true, "http.*activate.*");
			System.out.println("Generated activization request:\n\t"
					+ setActRequest(regURL));
			sleep(10);
			try {
				error = ShellCommandExecutor.runCommand("",
						setActRequest(regURL), "activated");
				sleep(3);
				error = ShellCommandExecutor.runCommand("",
						setActRequest(regURL), "expired");
				System.out.println("\nSuccess!\nUser " + setRegMail(mail, nextSuffix)
						+ " activated.");
				String regMail = setRegMail(mail, nextSuffix);
				return regMail;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			}
		}
		String regMail = null;
		if (!error) regMail = setRegMail(mail, nextSuffix);
		return regMail;
	}
	
	private static String setRegMail(String basemail, String suffix) {
		String genmail = basemail.split("@")[0].concat("+").concat(suffix)
				.concat("@").concat(basemail.split("@")[1]);
		return genmail;
	}

	private static String setRegRequest(String mail, String password,
			String backendUrl, String suffix) {
		String request = String.format(CREATE_USER_JSON, backendUrl, suffix, mail, password);  
		return request;
	}

	private static String setActRequest(String url) {
		String request = null;
		request = "curl \"" + url + "\"";
		return request;
	}

	private static String getMailContent(String protocol, String host,
			String user, String password, int timeout, boolean waitForNewMail,
			String regEx) {
		String mbox = null;
		String mailContent = null;
		if (waitForNewMail)
			sleep(5);

		try {
			Properties props = System.getProperties();
			Session session = Session.getInstance(props, null);
			Store store = null;
			if (protocol != null)
				store = session.getStore(protocol);
			else
				store = session.getStore();

			if (host != null || user != null || password != null)
				store.connect(host, -1, user, password);
			else
				store.connect();

			Folder folder = store.getDefaultFolder();
			if (mbox == null)
				mbox = MAILS_FOLDER;
			folder = folder.getFolder(mbox);
			try {
				folder.open(Folder.READ_WRITE);
			} catch (MessagingException ex) {
				folder.open(Folder.READ_ONLY);
			}
			int totalMessages = folder.getMessageCount();

			if (totalMessages == 0) {
				System.out.println("ERROR: " + mbox + " folder is empty!");
				folder.close(false);
				store.close();
			}

			if (waitForNewMail) {
				while (timeout > 0 && mailContent == null) {
					// System.out.println(timeout);
					timeout = timeout - 10;
					sleep(5);
					Message[] message = folder.getMessages();
					Flags flags = message[totalMessages - 1].getFlags();
					if (flags.getSystemFlags().length == 0)
						mailContent = msgPart(message[totalMessages - 1]);
				}
				if (mailContent == null)
					System.out.println("ERROR: No new messages received");
				else
					mailContent = RegExUtil.getStringByRegEx(mailContent,
							regEx, true);
			} else {
				int limit;
				if (totalMessages < MAILS_TO_CHECK)
					limit = 0;
				else
					limit = totalMessages - MAILS_TO_CHECK;
				Message[] message = folder.getMessages();
				for (int i = totalMessages - 1; i > limit; i--) {
					if (mailContent == null) {
						mailContent = msgPart(message[i]);
						mailContent = RegExUtil.getStringByRegEx(mailContent,
								regEx, true);
					} else
						break;
				}
				if (mailContent == null)
					System.out.println("ERROR: No matched message found");
			}
		} catch (Exception ex) {
			System.out.println("Exception! " + ex.getMessage());
			ex.printStackTrace();
		}
		return mailContent;
	}

	private static String msgPart(Part p) throws Exception {
		String mailContent = "";

		if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++)
				mailContent = mailContent.concat("\n").concat(
						msgPart(mp.getBodyPart(i)));
		} else if (p.isMimeType("text/plain")) {
			// System.out.println((String) p.getContent()); //print mail content
			mailContent = (String) p.getContent();
		}
		return mailContent;
	}

	private static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}