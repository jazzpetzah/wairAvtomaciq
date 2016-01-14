package com.wearezeta.auto.common.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class ZetaOSXDriver extends AppiumDriver<WebElement> implements ZetaDriver {

	private static final String AX_POSITION = "AXPosition";
	private static final String AX_SIZE = "AXSize";
	private static final String APP_NAME = "Wire";

	public ZetaOSXDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
	}

    @Override
    public List<WebElement> findElements(By by) {
        return super.findElements(by).stream().map((e) -> wrapElement(e)).collect(Collectors.toList());
    }

    @Override
    public WebElement findElement(By by) {
        return wrapElement(super.findElement(by));
    }

	private WireRemoteWebElement wrapElement(WebElement element) {
		return new WireRemoteWebElement(element);
	}

	@Override
	public boolean isSessionLost() {
		return false;
	}

	@Override
	public Options manage() {
		return new ZetaRemoteWebDriverOptions();
	}

	@Override
	public MobileElement scrollTo(String text) {
		throw new RuntimeException("Not implemented for OSX");
	}

	@Override
	public MobileElement scrollToExact(String text) {
		throw new RuntimeException("Not implemented for OSX");
	}

	protected class WireRemoteWebElement extends RemoteWebElement {

		private final WebElement originalElement;

		public WireRemoteWebElement(WebElement element) {
			this.originalElement = element;
		}

		@Override
		public void click() {
			originalElement.click();
		}

		@Override
		public void submit() {
			originalElement.submit();
		}

		@Override
		public void sendKeys(CharSequence... keysToSend) {
			originalElement.sendKeys(keysToSend);
		}

		@Override
		public void clear() {
			originalElement.clear();
		}

		@Override
		public String getTagName() {
			return originalElement.getTagName();
		}

		@Override
		public String getAttribute(String name) {
			return originalElement.getAttribute(name);
		}

		@Override
		public boolean isSelected() {
			return originalElement.isSelected();
		}

		@Override
		public boolean isEnabled() {
			return originalElement.isEnabled();
		}

		@Override
		public String getText() {
			return originalElement.getText();
		}

		@Override
		public String getCssValue(String propertyName) {
			return originalElement.getCssValue(propertyName);
		}

		@Override
		public List<WebElement> findElements(By by) {
			return originalElement.findElements(by);
		}

		@Override
		public WebElement findElement(By by) {
			return originalElement.findElement(by);
		}

		@Override
		public boolean equals(Object obj) {
			return originalElement.equals(obj);
		}

		@Override
		public int hashCode() {
			return originalElement.hashCode();
		}

		@Override
		public boolean isDisplayed() {
			return originalElement.isDisplayed();
		}

		@Override
		public Dimension getSize() {
			final NSPoint elementSize = NSPoint.fromString(this
					.getAttribute(AX_SIZE));
			return new Dimension(elementSize.x(), elementSize.y());
		}

		@Override
		public Point getLocation() {
			final NSPoint elementLocation = NSPoint.fromString(this
					.getAttribute(AX_POSITION));
			return new Point(elementLocation.x(), elementLocation.y());
		}

		@Beta
		@Override
		public <X> X getScreenshotAs(OutputType<X> outputType)
				throws WebDriverException {
			return originalElement.getScreenshotAs(outputType);
		}

		@Override
		public String toString() {
			return originalElement.toString();
		}

	}

	protected class ZetaRemoteWebDriverOptions extends RemoteWebDriverOptions {

		private static final String WINDOW_LOCATOR = "//AXApplication[@AXTitle='"
				+ APP_NAME + "']//AXWindow";

		@Beta
		@Override
		public WebDriver.Window window() {
			final String xpathWindow = WINDOW_LOCATOR;
			final WebElement window = findElement(By.xpath(xpathWindow));
			return new ZetaRemoteWindow(window);
		}

		@Beta
		protected class ZetaRemoteWindow extends RemoteWindow {

			private final WebElement window;

			public ZetaRemoteWindow(WebElement window) {
				this.window = window;
			}

			@Override
			public Dimension getSize() {
				NSPoint windowSize = NSPoint.fromString(window
						.getAttribute(AX_SIZE));
				return new Dimension(windowSize.x(), windowSize.y());
			}

			@Override
			public Point getPosition() {
				NSPoint elementLocation = NSPoint.fromString(window
						.getAttribute(AX_POSITION));
				return new Point(elementLocation.x(), elementLocation.y());
			}

		}
	}

}
