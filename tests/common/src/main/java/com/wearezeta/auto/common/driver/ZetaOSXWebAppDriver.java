package com.wearezeta.auto.common.driver;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.net.URL;
import org.apache.log4j.Logger;
import org.openqa.selenium.Beta;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;

public class ZetaOSXWebAppDriver extends ZetaWebAppDriver {

	private static final Logger LOG = ZetaLogger.getLog(ZetaWebAppDriver.class
			.getName());

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
				return osxDriver.manage().window().getSize();
			}

			@Override
			public Point getPosition() {
				return osxDriver.manage().window().getPosition();
			}

		}
	}
}
