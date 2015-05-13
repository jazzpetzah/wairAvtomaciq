package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.Response;

import com.wearezeta.auto.common.log.ZetaLogger;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver {

	private static final Logger log = ZetaLogger.getLog(ZetaAndroidDriver.class
			.getSimpleName());

	private boolean isSessionLost = false;

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
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
		} catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (RuntimeException ex) {
			// log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}

		return result;
	}

	@Override
	public WebElement findElement(By by) {
		WebElement result = null;
		try {
			result = super.findElement(by);
		} catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (RuntimeException ex) {
			// log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}

		return result;
	}

	@Override
	public void swipe(int startx, int starty, int endx, int endy, int duration) {
		try {
			super.swipe(startx, starty, endx, endy, duration);
		} catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}

	@Override
	public void tap(int fingers, int x, int y, int duration) {
		try {
			super.tap(fingers, x, y, duration);
		} catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}

	@Override
	public void tap(int fingers, WebElement element, int duration) {
		try {
			super.tap(fingers, element, duration);
		} catch (RuntimeException ex) {
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			throw ex;
		}
	}

	@Override
	public void close() {
		super.close();
	}

	@Override
	public void quit() {
		try {
			super.quit();
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
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

	// This is dirty hack for NoSuchElementException in android tests
	// when sometimes Appium does not refresh UI elements tree properly
	@Override
	public Response execute(String driverCommand, Map<String, ?> parameters) {
		try {
			return super.execute(driverCommand, parameters);
		} catch (NoSuchElementException e) {
			log.debug(String
					.format("Driver command '%s' failed with NoSuchElementException. Trying to refresh UI tree...",
							driverCommand));
			try {
				execute(DriverCommand.GET_PAGE_SOURCE);
			} catch (WebDriverException e1) {
				e1.printStackTrace();
			}
			return super.execute(driverCommand, parameters);
		}
	}

}
