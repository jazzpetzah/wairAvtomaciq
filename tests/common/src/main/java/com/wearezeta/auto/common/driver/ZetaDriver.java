package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.log.ZetaLogger;

import io.appium.java_client.AppiumDriver;

public class ZetaDriver extends AppiumDriver implements WebDriver {
	
	private static final Logger log = ZetaLogger.getLog(ZetaDriver.class.getSimpleName());

	private boolean isSessionLost = false;
	
	public ZetaDriver(URL remoteAddress, Capabilities desiredCapabilities) {
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
			setSessionLost(true);
		}
		catch (org.openqa.selenium.remote.SessionNotFoundException ex ) {
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
			setSessionLost(true);
		}
		catch (org.openqa.selenium.remote.SessionNotFoundException ex ) {
			setSessionLost(true);
		}
		catch (RuntimeException ex) {
//			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public void swipe(int startx, int starty, int endx, int endy, int duration) {
		try {
			super.swipe(startx, starty, endx, endy, duration);
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}
	
	@Override
	public void tap(int fingers, int x, int y, int duration) {
		try {
			super.tap(fingers, x, y, duration);
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}
	
	@Override
	public void tap(int fingers, WebElement element, int duration) {
		try {
			super.tap(fingers, element, duration);
		}
		catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}

	@Override
	public String getPageSource() {
		String result = super.getPageSource();
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
