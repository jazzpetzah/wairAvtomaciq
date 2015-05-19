package com.wearezeta.auto.common.driver;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.log.ZetaLogger;

final class SessionHelper {
	private static final Logger log = ZetaLogger.getLog(SessionHelper.class
			.getSimpleName());

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

	public List<WebElement> wrappedFindElements(Function<By, List<WebElement>> f, By by) {
		List<WebElement> result = null;
		try {
			result = f.apply(by);
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

	public WebElement wrappedFindElement(Function<By, WebElement> f, By by) {
		WebElement result = null;
		try {
			result = f.apply(by);
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

	public void wrappedClose(Supplier<Void> f) {
		try {
			f.get();
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
	}

	public void wrappedQuit(Supplier<Void> f) {
		try {
			f.get();
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
