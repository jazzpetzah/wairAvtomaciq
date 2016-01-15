package com.wearezeta.auto.common.driver;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import static com.wearezeta.auto.common.driver.ZetaDriver.MAX_COMMAND_DURATION;
import com.wearezeta.auto.common.log.ZetaLogger;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Beta;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Response;
import org.openqa.selenium.remote.SessionNotFoundException;
import org.openqa.selenium.remote.UnreachableBrowserException;

public class ZetaOSXWebAppDriver extends ZetaWebAppDriver {

	private static final Logger log = ZetaLogger.getLog(ZetaOSXWebAppDriver.class.getSimpleName());
	private ExecutorService pool;
	private volatile boolean isSessionLost = false;

	private ZetaOSXDriver osxDriver;

	public ZetaOSXWebAppDriver(URL remoteAddress,
		Capabilities desiredCapabilities, ZetaOSXDriver osxDriver) {
		super(remoteAddress, desiredCapabilities);
		this.osxDriver = osxDriver;
	}

	@Override
	public Options manage() {
		return new ZetaRemoteWebDriverOptions();
	}

	@Override
	public boolean isSessionLost() {
		return this.isSessionLost;
	}

	private void setSessionLost(boolean isSessionLost) {
		if (isSessionLost != this.isSessionLost) {
			log.warn(String.format("Changing isSessionLost to %s", isSessionLost));
			this.isSessionLost = isSessionLost;
		}
	}

	@Override
	protected Response execute(String command) {
		return this.execute(command, ImmutableMap.<String, Object>of());
	}

	@Override
	public Response execute(String driverCommand, Map<String, ?> parameters) {
		if (this.isSessionLost()) {
			log.warn(String.format("Driver session is dead. Skipping execution of '%s' command...", driverCommand));
			return null;
		}
		final Callable<Response> task = () -> super.execute(driverCommand, parameters);
		final Future<Response> future = getPool().submit(task);
		try {
			return future.get(MAX_COMMAND_DURATION, TimeUnit.SECONDS);
		} catch (Exception e) {
			if (e instanceof ExecutionException) {
				if ((e.getCause() instanceof UnreachableBrowserException)
					|| (e.getCause() instanceof SessionNotFoundException)) {
					setSessionLost(true);
				}
				Throwables.propagate(e.getCause());
			} else {
				setSessionLost(true);
				Throwables.propagate(e);
			}
		}
		// This should never happen
		return super.execute(driverCommand, parameters);
	}
	private synchronized ExecutorService getPool() {
		if (this.pool == null) {
			this.pool = Executors.newSingleThreadExecutor();
		}
		return this.pool;
	}

	public ZetaOSXDriver getOsxDriver() {
		return osxDriver;
	}

	protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {

		@Beta
		@Override
		public WebDriver.Window window() {
			return new ZetaRemoteWindow();
		}

		@Beta
		protected class ZetaRemoteWindow extends RemoteWebDriverOptions.RemoteWindow {

			@Override
			public Dimension getSize() {
				return osxDriver.manage().window().getSize();
			}

			@Override
			public Point getPosition() {
				return osxDriver.manage().window().getPosition();
			}

		}
	}
}
