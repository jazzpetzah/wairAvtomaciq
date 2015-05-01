package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.log.ZetaLogger;

final class LazyDriverInitializer implements Callable<RemoteWebDriver> {
	private static final Logger log = ZetaLogger
			.getLog(LazyDriverInitializer.class.getSimpleName());

	private String url;
	private DesiredCapabilities capabilities;
	private Platform platform;

	public LazyDriverInitializer(Platform platform, String url,
			DesiredCapabilities capabilities) {
		this.url = url;
		this.capabilities = capabilities;
		this.platform = platform;
	}

	@Override
	public RemoteWebDriver call() throws Exception {
		log.debug(String.format(
				"Creating driver instance for platform '%s'...",
				this.platform.name()));
		RemoteWebDriver platformDriver = null;
		switch (this.platform) {
		case Mac:
			platformDriver = new ZetaOSXDriver(new URL(url), capabilities);
			break;
		case iOS:
			platformDriver = new ZetaIOSDriver(new URL(url), capabilities);
			break;
		case Android:
			platformDriver = new ZetaAndroidDriver(new URL(url), capabilities);
			break;
		case Web:
			platformDriver = new ZetaWebAppDriver(new URL(url), capabilities);
			break;
		default:
			throw new RuntimeException(String.format(
					"Platform '%s' is unknown", this.platform.name()));
		}
		log.debug(String.format(
				"Successfully created driver instance for platform '%s'",
				this.platform.name()));
		return platformDriver;
	}
}
