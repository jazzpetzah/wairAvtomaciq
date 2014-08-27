package com.wearezeta.auto.common.log;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.wearezeta.auto.common.driver.ZetaDriver;

public class ZetaLogger {
	    public static synchronized Logger getLog(String className) {
	        URL configFile = ZetaDriver.class
	    			.getResource("/log4j.properties");
	    	PropertyConfigurator.configure(configFile);
	        return Logger.getLogger(className);
	}
}
