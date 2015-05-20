package com.wearezeta.auto.common.driver;

import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.remote.RemoteTouchScreen;

import com.google.common.base.Throwables;

import io.appium.java_client.android.AndroidDriver;

public class ZetaAndroidDriver extends AndroidDriver implements ZetaDriver,
		HasTouchScreen {

	private SessionHelper sessionHelper;
	private RemoteTouchScreen touch;

	public ZetaAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		this.touch = new RemoteTouchScreen(getExecuteMethod());
		sessionHelper = new SessionHelper();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return this.sessionHelper.wrappedFindElements(super::findElements, by);
	}

	@Override
	public WebElement findElement(By by) {
		return this.sessionHelper.wrappedFindElement(super::findElement, by);
	}

	private Void closeDriver() {
		super.close();
		return null;
	}

	@Override
	public void close() {
		this.sessionHelper.wrappedClose(this::closeDriver);
	}

	private Void quitDriver() {
		super.quit();
		return null;
	}

	@Override
	public void quit() {
		this.sessionHelper.wrappedQuit(this::quitDriver);
	}

	@Override
	public boolean isSessionLost() {
		return this.sessionHelper.isSessionLost();
	}

	private int getCoord(double startC, double endC, double current,
			double duration) {
		return (int) Math.round(startC + (endC - startC) / duration * current);
	}

	private final static int SWIPE_STEP_DURATION_MILLISECONDS = 500;

	@Override
	public void swipe(int startx, int starty, int endx, int endy,
			int durationMilliseconds) {
		int duration = 1;
		if (durationMilliseconds > SWIPE_STEP_DURATION_MILLISECONDS) {
			duration = (durationMilliseconds % SWIPE_STEP_DURATION_MILLISECONDS == 0) ? (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS)
					: (durationMilliseconds / SWIPE_STEP_DURATION_MILLISECONDS + 1);
		}
		int current = 1;
		final TouchActions ta = new TouchActions(this);
		ta.down(startx, starty).perform();
		do {
			try {
				Thread.sleep(SWIPE_STEP_DURATION_MILLISECONDS);
			} catch (InterruptedException e) {
				Throwables.propagate(e);
			}
			ta.move(getCoord(startx, endx, current, duration),
					getCoord(starty, endy, current, duration)).perform();
			current++;
		} while (current <= duration);
		ta.up(endx, endy).perform();
	}

	@Override
	public TouchScreen getTouch() {
		return this.touch;
	}
}
