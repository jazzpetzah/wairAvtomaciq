package com.wearezeta.auto.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;



public class CommonUtils {
	
	public static String yourUserName = null;
	public static UsersState yourUserState = UsersState.NotCreated;
	public static TreeMap<String,UsersState> contacts = new TreeMap<String, UsersState>();
	
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
	
	public static void generateUsers(int contactNumber)
	{
		yourUserName = CreateZetaUser.registerUserAndReturnMail(CreateZetaUser.defaultEmail, CreateZetaUser.defaultPassword, CreateZetaUser.defaultBackEndUrl);
		if(yourUserName != null){
			yourUserState = UsersState.Created;
		}
		else{
			 throw new NullPointerException("User was not created");
		}
		for(int i  = 0; i < contactNumber; i++){
			String contact = CreateZetaUser.registerUserAndReturnMail(CreateZetaUser.defaultEmail, CreateZetaUser.defaultPassword, CreateZetaUser.defaultBackEndUrl);
			if(contact != null){
				contacts.put(contact,UsersState.Created);
			}
			else{
				throw new NullPointerException("Contact was not created");
			}
		}
	}

}
