package com.wearezeta.auto.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class CommonUtils {
	
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
	
	public static String getAppPathFromConfig(Class c) throws IOException {

        return getValueFromConfig(c, "appPath");
	}
	
	public static String getAndroidActivityFromConfig(Class c) throws IOException {

        return getValueFromConfig(c, "activity");
	}
	
	public static String getAndroidPackageFromConfig(Class c) throws IOException {

        return getValueFromConfig(c, "package");
	}

}
