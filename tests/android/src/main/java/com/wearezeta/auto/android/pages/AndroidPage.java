package com.wearezeta.auto.android.pages;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.Future;

public abstract class AndroidPage extends BasePage {

	protected static final String giphyPreviewButtonId = "cursor_button_giphy";

	protected static final String xpathConfirmBtn = "(//*[@id='confirm'])[last()]";

	protected static final String idEditText = "cursor_edit_text";

	protected static final String idCursorArea = "caret";

	protected static final String idGalleryBtn = "gtv__camera_control__pick_from_gallery";

	protected static final String idCloseImageBtn = "gtv__single_image_message__close";

	protected static final String idSearchHintClose = "zb__search_hint__close_button";

	protected static final String idConversationSendOption = "tv_conv_list_topic";

	public static final String xpathDismissUpdateButton = "//*[@value='Dismiss']";

	protected static final String classNameFrameLayout = "FrameLayout";

	private static final String idChatheadNotification = "mncv__notifications__chathead";

	protected static final Logger log = ZetaLogger.getLog(CommonUtils.class
			.getSimpleName());

	protected static final String idPager = "conversation_pager";
	@FindBy(id = idPager)
	private WebElement content;

	@Override
	protected ZetaAndroidDriver getDriver() throws Exception {
		return (ZetaAndroidDriver) super.getDriver();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Future<ZetaAndroidDriver> getLazyDriver() {
		return (Future<ZetaAndroidDriver>) super.getLazyDriver();
	}

	@Override
	protected long getDriverInitializationTimeout() {
		return 1000 * 60 * 6;
	}

	public AndroidPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	public void hideKeyboard() throws Exception {
		this.getDriver().hideKeyboard();
	}

	public void pressKeyboardSendButton() throws Exception {
		tapByCoordinates(94, 96);
	}

	/**
	 * Navigates back by BACK button
	 * 
	 * @throws Exception
	 */
	public void navigateBack() throws Exception {
		AndroidCommonUtils.tapBackButton();
		// Wait for animation
		Thread.sleep(1000);
	}

	public void rotateLandscape() throws Exception {
		// AndroidCommonUtils.rotateLanscape();
		this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
	}

	public void rotatePortrait() throws Exception {
		// AndroidCommonUtils.rotatePortrait();
		this.getDriver().rotate(ScreenOrientation.PORTRAIT);
	}

	public void dialogsPagesSwipeUp(int durationMilliseconds) throws Exception {
		swipeByCoordinates(durationMilliseconds, 50, 80, 50, 30);
	}

	public void dialogsPagesSwipeDown(int durationMilliseconds)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, 50, 30, 50, 80);
	}

	public void swipeByCoordinates(int durationMilliseconds,
			int widthStartPercent, int heightStartPercent, int widthEndPercent,
			int heightEndPercent) throws Exception {
		final Dimension screenDimension = getDriver().manage().window()
				.getSize();
		this.getDriver().swipe(screenDimension.width * widthStartPercent / 100,
				screenDimension.height * heightStartPercent / 100,
				screenDimension.width * widthEndPercent / 100,
				screenDimension.height * heightEndPercent / 100,
				durationMilliseconds);
	}

	public static final int SWIPE_DEFAULT_PERCENTAGE_START = 10;
	public static final int SWIPE_DEFAULT_PERCENTAGE_END = 90;
	public static final int SWIPE_DEFAULT_PERCENTAGE = 50;

	/**
	 * Swipe from x = 90% of width to x = 10% of width. y = height/2
	 */
	public void swipeRightCoordinates(int durationMilliseconds)
			throws Exception {
		swipeRightCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}

	/**
	 * Swipe from x = 10% of width to x = 90% of width. y = heightPercent
	 */
	public void swipeRightCoordinates(int durationMilliseconds,
			int heightPercent) throws Exception {
		swipeByCoordinates(durationMilliseconds,
				SWIPE_DEFAULT_PERCENTAGE_START, heightPercent,
				SWIPE_DEFAULT_PERCENTAGE_END, heightPercent);
	}

	/**
	 * Swipe from x = 90% of width to x = 10% of width. y = height/2
	 */
	public void swipeLeftCoordinates(int durationMilliseconds) throws Exception {
		swipeLeftCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}

	/**
	 * Swipe from x = 90% of width to x = 10% of width. y = heightPercent
	 */
	public void swipeLeftCoordinates(int durationMilliseconds, int heightPercent)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE_END,
				heightPercent, SWIPE_DEFAULT_PERCENTAGE_START, heightPercent);
	}

	/**
	 * Swipe from y = 90% of height to y = 10% of height. x = width/2
	 */
	public void swipeUpCoordinates(int durationMilliseconds) throws Exception {
		swipeUpCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}

	/**
	 * Swipe from y = 90% of height to y = 10% of height. x = widthPercent
	 */
	public void swipeUpCoordinates(int durationMilliseconds, int widthPercent)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_END, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_START);
	}

	/**
	 * Swipe from y = 10% of height to y = 90% of height. x = width/2
	 */
	public void swipeDownCoordinates(int durationMilliseconds) throws Exception {
		swipeDownCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}

	/**
	 * Swipe from y = 10% of height to y = 90% of height. x = widthPercent
	 */
	public void swipeDownCoordinates(int durationMilliseconds, int widthPercent)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_START, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_END);
	}

	public void tapByCoordinates(int widthPercent, int heightPercent)
			throws Exception {
		int x = getDriver().manage().window().getSize().getWidth()
				* widthPercent / 100;
		int y = getDriver().manage().window().getSize().getHeight()
				* heightPercent / 100;
		AndroidCommonUtils.genericScreenTap(x, y);
	}

	public void tapOnCenterOfScreen() throws Exception {
		tapByCoordinates(50, 50);
	}

	public void tapChatheadNotification() throws Exception {
		final By locator = By.id(idChatheadNotification);
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : "The chathead notification has not been displayed after the default timeout";
		final WebElement el = this.getDriver().findElement(locator);
		assert DriverUtils.waitUntilElementClickable(getDriver(), el) : "The chathead notification is not clickable";
		el.click();
	}

	public boolean waitUntilChatheadNotificationVisible() throws Exception {
		final By locator = By.id(idChatheadNotification);
		if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator)) {
			final WebElement el = this.getDriver().findElement(locator);
			return el.getLocation().getX() >= 0;
		}
		return false;
	}

	public boolean waitUntilChatheadNotificationInvisible() throws Exception {
		final By locator = By.id(idChatheadNotification);
		if (!DriverUtils.waitUntilLocatorDissapears(getDriver(), locator, 5)) {
			final WebElement el = this.getDriver().findElement(locator);
			return el.getLocation().getX() < 0;
		}
		return true;
	}
}
