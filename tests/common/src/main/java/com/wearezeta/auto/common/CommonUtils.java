package com.wearezeta.auto.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;


public class CommonUtils {
	
	public static final String FIRST_OS_NAME = "Windows";
	public static final String YOUR_USER_1 = "aqaUser";
	public static final String YOUR_USER_2 = "yourUser";
	public static final String YOUR_USER_3 = "yourContact";
	public static final String YOUR_PASS = "aqaPassword";
	public static final String CONTACT_1 = "aqaContact1";
	public static final String CONTACT_2 = "aqaContact2";
	public static List<ClientUser> yourUsers = new ArrayList<ClientUser>();
	public static List<ClientUser> contacts = new ArrayList<ClientUser>();

	public static String getOsName(){
		return System.getProperty("os.name");
	}
	
	public static void uploadPhotoToAndroid() throws Exception{
		if(getOsName().contains(FIRST_OS_NAME)){
		    Runtime.getRuntime().exec("cmd /C adb push " + getWindowsImagePath(CommonUtils.class) + "/mnt/sdcard/DCIM/Camera/userpicture.jpg");
		    Runtime.getRuntime().exec("cmd /C adb -d shell \"am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///sdcard \"Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }");
		}
		else{
			executeOsXCommand(new String[]{"/bin/bash", "-c", "adb push", getOsXImagePath(CommonUtils.class),"/mnt/sdcard/DCIM/Camera/userpicture.jpg"});
			executeOsXCommand(new String[]{"/bin/bash", "-c", "adb shell am broadcast -a android.intent.action.MEDIA_MOUNTED -d file:///sdcard Broadcasting: Intent { act=android.intent.action.MEDIA_MOUNTED dat=file:///sdcard }"});
		}
	}
	public static void killAndroidClient() throws Exception
	{
		if(getOsName().contains(FIRST_OS_NAME)){
			Runtime.getRuntime().exec("cmd /C adb shell am force-stop com.waz.zclient");
		}
		else{
			executeOsXCommand(new String[]{"/bin/bash", "-c", "adb shell am force-stop com.waz.zclient"});
		}
	}
	
	public static void executeOsXCommand(String [] cmd) throws Exception{
		Process process = Runtime.getRuntime().exec(cmd);
		System.out.println("Process Code "+ process.waitFor()); 
	}
	
	public static String retrieveRealUserContactPasswordValue(String value) {
		if (yourUsers.size() > 0) {
			if (value.contains(YOUR_USER_1)) {
				value = value.replace(YOUR_USER_1, yourUsers.get(0).getName());
			}
			if (value.contains(YOUR_USER_2)) {
				value = value.replace(YOUR_USER_2, yourUsers.get(1).getName());
			}
			if (value.contains(YOUR_USER_3)) {
				value = value.replace(YOUR_USER_3, yourUsers.get(2).getName());
			}
			if (value.contains(YOUR_PASS)) {
				value = value.replace(YOUR_PASS, yourUsers.get(0).getPassword());
			}
		} if (contacts.size() > 0) {
			if (value.contains(CONTACT_1)) {
				value = value.replace(CONTACT_1, contacts.get(0).getName());
			}
			if (value.contains(CONTACT_2)) {
				value = value.replace(CONTACT_2, contacts.get(1).getName());
			}
		}
		return value;
	}
	
	private static String getWindowsImagePath(Class c)throws IOException {

        return getValueFromConfig(c, "defaultWindowsImagePath");
	}
	
	public static String getResultImagePath(Class c)throws IOException {

        return getValueFromConfig(c, "resultImage");
	}
	
	public static String getOsXImagePath(Class c)throws IOException {

        return getValueFromConfig(c, "defaultOsXImagePath");
	}
	
	public static String getPictureResultsPathFromConfig(Class c)throws IOException {

        return getValueFromConfig(c, "pictureResultsPath");
	}
	
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
	
	public static String getDriverTimeoutFromConfig(Class c) throws IOException {

		return getValueFromConfig(c, "driverTimeoutSeconds");
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
	
	public static String getPhotoScriptPath(Class c)throws IOException {

        return getValueFromConfig(c, "photoScriptPath");
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
		for(int i  = 0; i < 3; i++){
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
	
	 public static void iOSSimulatorCameraRoll() throws IOException, InterruptedException{
		 
		 String scriptPath = CommonUtils.getPhotoScriptPath(CommonUtils.class);
		 
		 String [] cmd = new String []{"/bin/bash", scriptPath, "7.1"};
		 
		 Process process = Runtime.getRuntime().exec(cmd);
		 System.out.println("Process Code "+ process.waitFor());
	}
	 
	public static void usePrecreatedUsers() {
		ClientUser contact2 = new ClientUser("smoketester+aqa33@wearezeta.com", "aqa123456", "aqa33", UsersState.AllContactsConnected);
		ClientUser contact1 = new ClientUser("smoketester+aqa32@wearezeta.com", "aqa123456", "aqa32", UsersState.AllContactsConnected);
		ClientUser yourUser2 = new ClientUser("smoketester+aqa34@wearezeta.com", "aqa123456", "aqa34", UsersState.AllContactsConnected);
		ClientUser yourUser1 = new ClientUser("smoketester+aqa31@wearezeta.com", "aqa123456", "aqa31", UsersState.AllContactsConnected);
		yourUsers = new ArrayList<ClientUser>();
		contacts = new ArrayList<ClientUser>();
		yourUsers.add(yourUser1);
		yourUsers.add(yourUser2);
		contacts.add(contact1);
		contacts.add(contact2);
	}

}
