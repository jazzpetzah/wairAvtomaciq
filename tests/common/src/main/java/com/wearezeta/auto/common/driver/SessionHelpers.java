package com.wearezeta.auto.common.driver;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.log.ZetaLogger;

import io.appium.java_client.AppiumDriver;

final class SessionHelpers {
	private AppiumDriver wrappedDriver;

	public SessionHelpers(AppiumDriver wrappedDriver) {
		this.wrappedDriver = wrappedDriver;
	}

	private static final Logger log = ZetaLogger.getLog(SessionHelpers.class
			.getSimpleName());

	public final long INIT_TIMEOUT_MILLISECONDS = 1000 * 60 * 3;

	private boolean isSessionLost = false;

	public static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append("\t at ");
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public List<WebElement> findElements(By by) {
		List<WebElement> result = null;
		try {
			result = wrappedDriver.findElements(by);
		} catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}

		return result;
	}

	public WebElement findElement(By by) {
		WebElement result = null;
		try {
			result = wrappedDriver.findElement(by);
		} catch (org.openqa.selenium.remote.UnreachableBrowserException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}

		return result;
	}

	public void close() {
		try {
			wrappedDriver.close();
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
	}

	public void quit() {
		try {
			wrappedDriver.quit();
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
	}

	public boolean isSessionLost() {
		return isSessionLost;
	}

	private void setSessionLost(boolean isSesstionLost) {
		this.isSessionLost = isSesstionLost;
	}

}
