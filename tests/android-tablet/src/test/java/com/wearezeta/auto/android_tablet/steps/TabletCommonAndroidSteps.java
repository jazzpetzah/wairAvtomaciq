package com.wearezeta.auto.android_tablet.steps;

import java.util.concurrent.Future;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.google.common.base.Throwables;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.common.reporter.LogcatListener;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.android_tablet.pages.TabletWelcomePage;
import com.wearezeta.auto.common.CommonCallingSteps;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.Platform;
import com.wearezeta.auto.common.ZetaFormatter;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.PlatformDrivers;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.When;

public class TabletCommonAndroidSteps {
	static {
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.http",
				"warn");
	}

	private final AndroidTabletPagesCollection pagesCollection = AndroidTabletPagesCollection
			.getInstance();

	private static final Logger log = ZetaLogger
			.getLog(TabletCommonAndroidSteps.class.getSimpleName());

	public static LogcatListener listener = new LogcatListener();

	private final CommonSteps commonSteps = CommonSteps.getInstance();
	@SuppressWarnings("unused")
	private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();
	public static final Platform CURRENT_PLATFORM = Platform.Android;

	public static final String PATH_ON_DEVICE = "/mnt/sdcard/DCIM/Camera/userpicture.jpg";
	public static final int DEFAULT_SWIPE_TIME = 1500;
	@SuppressWarnings("unused")
	private static final String DEFAULT_USER_AVATAR = "aqaPictureContact600_800.jpg";

	private static String getUrl() throws Exception {
		return CommonUtils
				.getAndroidAppiumUrlFromConfig(TabletCommonAndroidSteps.class);
	}

	private static String getPath() throws Exception {
		return CommonUtils
				.getAndroidApplicationPathFromConfig(TabletCommonAndroidSteps.class);
	}

	@SuppressWarnings("unchecked")
	public Future<ZetaAndroidDriver> resetAndroidDriver(String url,
			String path, boolean isUnicode, Class<?> cls) throws Exception {
		final DesiredCapabilities capabilities = new DesiredCapabilities();
		LoggingPreferences object = new LoggingPreferences();
		object.enable("logcat", Level.ALL);
		capabilities.setCapability(CapabilityType.LOGGING_PREFS, object);
		capabilities.setCapability("platformName", CURRENT_PLATFORM.getName());
		capabilities.setCapability("deviceName",
				CommonUtils.getAndroidDeviceNameFromConfig(cls));
		capabilities.setCapability("app", path);
		capabilities.setCapability("appPackage",
				CommonUtils.getAndroidPackageFromConfig(cls));
		capabilities.setCapability("appActivity",
				CommonUtils.getAndroidActivityFromConfig(cls));
		capabilities.setCapability("appWaitActivity",
				CommonUtils.getAndroidWaitActivitiesFromConfig(cls));
		capabilities.setCapability("applicationName", "selendroid");
		capabilities.setCapability("automationName", "selendroid");

		if (isUnicode) {
			capabilities.setCapability("unicodeKeyboard", true);
			capabilities.setCapability("resetKeyboard", true);
		}

		try {
			return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
					.resetDriver(url, capabilities, 1,
							this::onDriverInitFinished,
							this::onDriverInitStarted);
		} catch (SessionNotCreatedException e) {
			// Unlock the screen and retry
			AndroidCommonUtils.unlockScreen();
			Thread.sleep(5000);
			return (Future<ZetaAndroidDriver>) PlatformDrivers.getInstance()
					.resetDriver(url, capabilities, 1,
							this::onDriverInitFinished,
							this::onDriverInitStarted);
		}
	}

	private Boolean onDriverInitStarted() {
		try {
			AndroidCommonUtils.uploadPhotoToAndroid(PATH_ON_DEVICE);
			AndroidCommonUtils.disableHints();
			AndroidCommonUtils.disableHockeyUpdates();
			String backendJSON = AndroidCommonUtils
					.createBackendJSON(CommonUtils.getBackendType(this
							.getClass()));
			AndroidCommonUtils.deployBackendFile(backendJSON);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return true;
	}

	private static final int UPDATE_ALERT_VISIBILITY_TIMEOUT = 5; // seconds

	@SuppressWarnings("unused")
	private void closeUpdateAlertIfAppears(RemoteWebDriver drv, By locator) {
		try {
			if (DriverUtils.waitUntilLocatorIsDisplayed(drv, locator,
					UPDATE_ALERT_VISIBILITY_TIMEOUT)) {
				drv.findElement(locator).click();
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private static final long INTERFACE_INIT_TIMEOUT_MILLISECONDS = 15000;

	private void onDriverInitFinished(RemoteWebDriver drv) {
		final By locator = By
				.xpath(AndroidLocators.CommonLocators.xpathDismissUpdateButton);
		final long millisecondsStarted = System.currentTimeMillis();
		WebDriverException savedException = null;
		do {
			try {
				DriverUtils.waitUntilLocatorIsDisplayed(drv, locator, 1);
				break;
			} catch (WebDriverException e) {
				savedException = e;
				log.debug("Waiting for the views to initialize properly...");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					Throwables.propagate(e);
				}
			} catch (Exception e) {
				Throwables.propagate(e);
			}
		} while (System.currentTimeMillis() - millisecondsStarted <= INTERFACE_INIT_TIMEOUT_MILLISECONDS);
		if (System.currentTimeMillis() - millisecondsStarted > INTERFACE_INIT_TIMEOUT_MILLISECONDS) {
			log.error(String
					.format("UI views have not been initialized properly after %s seconds. Restarting Selendroid usually helps ;-)",
							INTERFACE_INIT_TIMEOUT_MILLISECONDS));
			throw savedException;
		}
		// Break the glass in case of fire!
		// Just uncomment the following line if Android developers break update
		// alert appearance one more time
		// closeUpdateAlertIfAppears(drv, locator);
	}

	private void initFirstPage(boolean isUnicode) throws Exception {
		final Future<ZetaAndroidDriver> lazyDriver = resetAndroidDriver(
				getUrl(), getPath(), isUnicode, this.getClass());
		pagesCollection.setFirstPage(new TabletWelcomePage(lazyDriver));
		ZetaFormatter.setLazyDriver(lazyDriver);
	}

	public void commonBefore() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		ZetaFormatter.setBuildNumber(AndroidCommonUtils
				.readClientVersionFromAdb());
	}

	@Before({ "~@unicode" })
	public void setUp() throws Exception {
		commonBefore();
		initFirstPage(false);
	}

	@Before({ "@unicode" })
	public void setUpUnicode() throws Exception {
		commonBefore();
		initFirstPage(true);
	}

	@After
	public void tearDown() throws Exception {
		try {
			// async calls/waiting instances cleanup
			CommonCallingSteps.getInstance().cleanupWaitingInstances();
			CommonCallingSteps.getInstance().cleanupCalls();
		} catch (Exception e) {
			// do not fail if smt fails here
			e.printStackTrace();
		}

		pagesCollection.clearAllPages();

		if (PlatformDrivers.getInstance().hasDriver(CURRENT_PLATFORM)) {
			PlatformDrivers.getInstance().quitDriver(CURRENT_PLATFORM);
		}

		commonSteps.getUserManager().resetUsers();
	}

	/**
	 * Rotate device to landscape
	 * 
	 * @step. ^I rotate UI to landscape$
	 * 
	 * @throws Exception
	 */
	@When("^I rotate UI to landscape$")
	public void WhenIRotateUILandscape() throws Exception {
		pagesCollection.getCommonPage().rotateLandscape();
	}

	/**
	 * Rotate device to portrait
	 * 
	 * @step. ^I rotate UI to portrait$
	 * 
	 * @throws Exception
	 */
	@When("^I rotate UI to portrait$")
	public void WhenIRotateUIPortrait() throws Exception {
		pagesCollection.getCommonPage().rotatePortrait();
	}
}
