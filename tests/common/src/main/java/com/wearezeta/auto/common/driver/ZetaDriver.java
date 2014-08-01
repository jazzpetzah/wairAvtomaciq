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
	
	public Logger log = ZetaLogger.getLog();

	public ZetaDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
	}
	
	private String stackTraceToString(Throwable e) {
	    StringBuilder sb = new StringBuilder();
	    for (StackTraceElement element : e.getStackTrace()) {
	        sb.append(element.toString());
	        sb.append("\n");
	    }
	    return sb.toString();
	}

	@Override
	public List<WebElement> findElements(By by) {
		log.debug("findElements " + by.toString());
		List<WebElement> result = null;
		try {
			result = super.findElements(by);
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage());
			log.error(stackTraceToString(ex));
			throw ex;
		}
		
		return result;
	}

	@Override
	public WebElement findElement(By by) {
		log.debug("findElement " + by.toString());
		WebElement result = null;
		try {
			result = super.findElement(by);
		}
		catch (Exception ex)
		{
			log.error(ex.getMessage());
			log.error(stackTraceToString(ex));
			throw ex;
		}
		
		return result;
	}
	
	@Override
	public void swipe(int startx, int starty, int endx, int endy, int duration) {
		log.debug("swipe startx=" + Integer.toString(startx) + " starty=" + Integer.toString(starty) +
				" endx=" + Integer.toString(endx) + " endy=" + Integer.toString(endy));
		try {
			super.swipe(startx, starty, endx, endy, duration);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
			log.error(stackTraceToString(ex));
			throw ex;
		}
	}
	
	@Override
	public void tap(int fingers, int x, int y, int duration) {
		log.debug("tap fingers=" + Integer.toString(fingers) + " x=" + Integer.toString(x) +
				" y=" + Integer.toString(y) + " duration=" + Integer.toString(duration));
		try {
			super.tap(fingers, x, y, duration);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
			log.error(stackTraceToString(ex));
			throw ex;
		}
	}
	
	@Override
	public void tap(int fingers, WebElement element, int duration) {
		
		log.debug("tap fingers=" + Integer.toString(fingers) + " element=" + element.toString() +
				" duration=" + Integer.toString(duration));
		try {
			super.tap(fingers, element, duration);
		}
		catch (Exception ex) {
			log.error(ex.getMessage());
			log.error(stackTraceToString(ex));
			throw ex;
		}
	}

	@Override
	public String getPageSource() {
		String result = super.getPageSource();
		log.debug(result);
		return result;
	}

	@Override
	public void close() {
		
		log.debug("received close()");
		super.close();
	}

	@Override
	public void quit() {
		log.debug("received quit()");
		super.quit();
	}

}
