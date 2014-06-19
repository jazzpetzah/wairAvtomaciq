package com.wearezeta.auto.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;



public class CommonUtils {

	public static final String YOUR_USER_1 = "aqaUser";
	public static final String YOUR_USER_2 = "yourUser";
	public static final String YOUR_PASS = "aqaUser";
	public static final String CONTACT_1 = "aqaContact1";
	public static final String CONTACT_2 = "aqaContact2";
	public static List<ClientUser> yourUsers = new ArrayList<ClientUser>();
	public static List<ClientUser> contacts = new ArrayList<ClientUser>();

	private static String getValueFromConfig(Class c, String key) throws IOException {

		String val = "";
		InputStream configFileStream = null;

		try {
			URL configFile = c.getClass().getResource("/Configuration.cnf");
			configFileStream = configFile.openStream();
			Properties p = new Properties();
			p.load(configFileStream);


			val = (String)p.get(key);
		}
		finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}

		return val;
	}
	
	private static String getValueFromComonConfig(Class c, String key) throws IOException {

		String val = "";
		InputStream configFileStream = null;

		try {
			URL configFile = c.getClass().getResource("/ComonConfiguration.cnf");
			configFileStream = configFile.openStream();
			Properties p = new Properties();
			p.load(configFileStream);


			val = (String)p.get(key);
		}
		finally {
			if (configFileStream != null) {
				configFileStream.close();
			}
		}

		return val;
	}

	public static String getDefaultEmailFromConfig(Class c) throws IOException {

		return getValueFromComonConfig(c, "defaultEmail");
	}
	
	public static String getDefaultPasswordFromConfig(Class c) throws IOException {

		return getValueFromComonConfig(c, "defaultPassword");
	}
	
	public static String getDefaultBackEndUrlFromConfig(Class c) throws IOException {

		return getValueFromComonConfig(c, "defaultBackEndUrl");
	}
	
	public static String getUrlFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "Url");
	}

	public static Boolean getIsSimulatorFromConfig(Class c) throws IOException {

		return (getValueFromConfig(c, "isSimulator").equals("true"));
	}

	public static String getSwipeScriptPath(Class c) throws IOException {

		return getValueFromConfig(c, "swipeScriptPath");
	}

	public static String getAppPathFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "appPath");
	}

	public static String getAndroidActivityFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "activity");
	}

	public static String getGenerateUsersFlagFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "generateUsers");
	}

	public static String getAndroidPackageFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "package");
	}

	public static String generateGUID()
	{
		return UUID.randomUUID().toString();
	}

	public static String getContactName(String login)
	{
		String[] firstParts = null;
		String[] secondParts = null;
		firstParts = login.split("\\+");
		secondParts = firstParts[1].split("@");
		return secondParts[0];
	}

	public static void generateUsers(int contactNumber) throws IOException
	{
		for(int i  = 0; i < 2; i++){
			ClientUser user = new ClientUser();
			user.setEmail(CreateZetaUser.registerUserAndReturnMail());
			user.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
			if( user.getEmail() != null){
				user.setUserState(UsersState.Created);
				yourUsers.add(user);
			}
			else{
				throw new NullPointerException("User was not created");
			}
		}

		for(int i  = 0; i < contactNumber; i++){
			String contact = CreateZetaUser.registerUserAndReturnMail();
			if(contact != null){
				ClientUser user = new ClientUser();
				user.setEmail(contact);
				user.setPassword(getDefaultPasswordFromConfig(CommonUtils.class));
				user.setUserState(UsersState.Created);
				contacts.add(user);
			}
			else{
				throw new NullPointerException("Contact was not created");
			}
		}
	}

}
