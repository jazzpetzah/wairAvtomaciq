package com.wearezeta.auto.common.driver;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.log.ZetaLogger;

final class SessionHelper {
	private static final Logger log = ZetaLogger.getLog(SessionHelper.class
			.getSimpleName());

	private boolean isSessionLost = false;
	private ZetaDriver wrappedDriver;

	public SessionHelper(ZetaDriver wrappedDriver) {
		this.wrappedDriver = wrappedDriver;
	}

	public static String stackTraceToString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append("\t at ");
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public List<WebElement> wrappedFindElements(
			Function<By, List<WebElement>> f, By by) {
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

	public static final int SCREENSHOTING_TIMEOUT_SECONDS = 7;

	private void waitForScreenshots() {
		if (this.wrappedDriver instanceof HasParallelScreenshotsFeature) {
			if (ZetaFormatter.getScreenshotMakers().isPresent()) {
				ZetaFormatter.getScreenshotMakers().get().shutdown();
				try {
					if (!ZetaFormatter
							.getScreenshotMakers()
							.get()
							.awaitTermination(SCREENSHOTING_TIMEOUT_SECONDS,
									TimeUnit.SECONDS)) {
						log.warn(String
								.format("Not all screenshots were taken for %s within %s seconds timeout",
										this.wrappedDriver.getClass()
												.getSimpleName(),
										SCREENSHOTING_TIMEOUT_SECONDS));
						((HasParallelScreenshotsFeature) this.wrappedDriver)
								.forceStopOfScreenshoting();
					}
				} catch (InterruptedException e) {
					// silently ignore
				}
			}
		}
	}

	public void wrappedClose(IVoidMethod f) {
		waitForScreenshots();
		try {
			f.call();
		} catch (org.openqa.selenium.remote.SessionNotFoundException ex) {
			log.error("Setting isSessionLost=true");
			log.error(ex.getMessage() + "\n" + stackTraceToString(ex));
			setSessionLost(true);
		}
	}

	public void wrappedQuit(IVoidMethod f) {
		waitForScreenshots();
		try {
			f.call();
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
