package com.wearezeta.auto.common;

import javax.mail.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class CreateZetaUser {

	private static final String MAILS_FOLDER = "Inbox";
	private static final String MAIL_PROTOCOL= "imaps";

	public static String registerUserAndReturnMail() throws IOException {
		String nextSuffix = null;
		String regMail = null;
		String mail = CommonUtils.getDefaultEmailFromConfig(CreateZetaUser.class);
		String password = CommonUtils. getDefaultPasswordFromConfig(CreateZetaUser.class);

		Map<String,String> nextUser = generateNextUser(mail,password);

		for (Map.Entry<String, String> entry : nextUser.entrySet()) {
			nextSuffix = entry.getKey();
			regMail = entry.getValue();
		}
		
		BackEndREST.registerNewUser(regMail,nextSuffix,password);
		activateRegisteredUser(regMail,10, mail,password);
		
		return regMail;
	}

	public static Map<String,String> generateNextUser(String mail, String password)
	{
		String suffix = CommonUtils.generateGUID();
		suffix = suffix.replace("-", "");
		String regMail = null;
		Map<String,String> user = new LinkedHashMap<String, String>();
		
		regMail = setRegMail(mail, suffix);
		System.out.println(regMail);
		user.put(suffix, regMail);
		return user;
	}

	public static boolean activateRegisteredUser(String registeredUserMail,int timeout, String mail, String password ) throws IOException
	{
		boolean activationState = false;
		for(int i=0; i<5; i++)
		{
			List<EmailHeaders> newMails = getLastMailHeaders(mail,
					password, 5);
			for(EmailHeaders newMailContent: newMails){
				if(newMailContent.getLastUserEmail().equals(registeredUserMail))
				{
					BackEndREST.activateNewUser(newMailContent.getXZetaKey(),newMailContent.getXZetaCode());
					activationState = true;
					break;
				}
			}
			if(activationState){
				break;
			}
			sleep(timeout);
		}
		return activationState;
	}

	public static List<EmailHeaders> getLastMailHeaders(String user, String password, int messageCount) throws IOException {

		String protocol = MAIL_PROTOCOL;
		String host = CommonUtils.getDefaultEmailServerFromConfig(CreateZetaUser.class);
		String mbox = null;
		List<EmailHeaders> headersList = new ArrayList<EmailHeaders>();
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

			int lastMessage = folder.getMessageCount();

			if (lastMessage == 0) {
				System.out.println("ERROR: " + mbox + " folder is empty!");
				folder.close(false);
				store.close();
			}
			else{
				Message[] messages = folder.getMessages((lastMessage-messageCount),lastMessage);
				for(Message message : messages){
					EmailHeaders headers = new EmailHeaders();
					headers.setLastUserEmail(message.getHeader("Delivered-To")[0]);
					headers.setMailSubject(message.getHeader("Subject")[0]);
					headers.setXZetaPurpose(message.getHeader("X-Zeta-Purpose")[0]);
					headers.setXZetaKey(message.getHeader("X-Zeta-Key")[0]);
					headers.setXZetaCode(message.getHeader("X-Zeta-Code")[0]);
					headersList.add(headers);
				}
				folder.close(false);
				store.close();
			}
		} catch (Exception ex) {
			System.out.println("Exception! " + ex.getMessage());
			ex.printStackTrace();
		}
		return headersList;
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