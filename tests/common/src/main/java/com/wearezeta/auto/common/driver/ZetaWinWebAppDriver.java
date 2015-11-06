package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;

import java.net.URL;

import org.apache.log4j.Logger;
import org.openqa.selenium.Beta;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

public class ZetaWinWebAppDriver extends ZetaWebAppDriver {

	@SuppressWarnings("unused")
	private static final Logger LOG = ZetaLogger.getLog(ZetaWebAppDriver.class
			.getName());

	private ZetaWinDriver winDriver;

	public ZetaWinWebAppDriver(URL remoteAddress,
			Capabilities desiredCapabilities, ZetaWinDriver winDriver) {
		super(remoteAddress, desiredCapabilities);
		this.winDriver = winDriver;
	}

	@Override
	public Options manage() {
		return new ZetaRemoteWebDriverOptions();
	}

	public ZetaWinDriver getWinDriver() {
		return winDriver;
	}

	protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {

		@Beta
		@Override
		public WebDriver.Window window() {
			return new ZetaRemoteWindow();
		}

		@Beta
		protected class ZetaRemoteWindow extends
				RemoteWebDriverOptions.RemoteWindow {

			@Override
			public Dimension getSize() {
				return winDriver.manage().window().getSize();
			}

			@Override
			public Point getPosition() {
				return winDriver.manage().window().getPosition();
			}

		}
	}
}
