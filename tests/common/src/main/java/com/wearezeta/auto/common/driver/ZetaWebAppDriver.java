package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.log.ZetaLogger;

public class ZetaWebAppDriver extends RemoteWebDriver implements ZetaDriver {
	
	private static final Logger log = ZetaLogger.getLog(ZetaWebAppDriver.class.getSimpleName());

	private boolean isSessionLost = false;
	
	public ZetaWebAppDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		isSessionLost = false;
	}
	
	private String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	    	sb.append("\t at ");
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}

	@Override
	public List<WebElement> findElements(By by) {
		List<WebElement> result = null;
		try {
			result = super.findElements(by);
		}
		catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
		catch (org.openqa.selenium.remote.SessionNotFoundException ex ) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
		catch (RuntimeException ex) {
//			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
		
		return result;
	}

	@Override
	public WebElement findElement(By by) {
		WebElement result = null;
		try {
			result = super.findElement(by);
		}
		catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
		catch (org.openqa.selenium.remote.SessionNotFoundException ex ) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
		catch (RuntimeException ex) {
//			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public void close() {
		super.close();
	}

	@Override
	public void quit() {
		try {
			super.quit();
		}
		catch (org.openqa.selenium.remote.SessionNotFoundException ex ) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
	}

	public boolean isSessionLost() {
		return isSessionLost;
	}

	public void setSessionLost(boolean isSesstionLost) {
		this.isSessionLost = isSesstionLost;
	}
}
