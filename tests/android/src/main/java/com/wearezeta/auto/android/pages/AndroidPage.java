package com.wearezeta.auto.android.pages;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import android.view.KeyEvent;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.CommonSteps;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.log.ZetaLogger;

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

	public AndroidPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
	}

	final protected CommonSteps commonSteps = CommonSteps.getInstance();

	public void selectFirstGalleryPhoto() throws Exception {
		final Dimension screenDimension = AndroidCommonUtils.getScreenSize(this
				.getDriver());
		final int xDivider = 7;
		final int yDivider = 8;
		int y = screenDimension.height / 2;
		do {
			int x = screenDimension.width - screenDimension.width / xDivider;
			do {
				// Selendroid workaround
				// Cannot handle external apps properly :-(
				AndroidCommonUtils.genericScreenTap(x, y);
				try {
					if (DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
							By.xpath(DialogPage.xpathConfirmOKButton), 1)) {
						return;
					}
				} catch (WebDriverException e) {
					// ignore silently
				}
				x -= screenDimension.width / xDivider;
			} while (x >= screenDimension.width / xDivider);
			y -= screenDimension.height / yDivider;
		} while (y >= screenDimension.height / yDivider);
		throw new RuntimeException("Failed to tap the first gallery image!");
	}

	public void hideKeyboard() throws Exception {
		try {
			this.getDriver().hideKeyboard();
		} catch (WebDriverException e) {
			log.debug("The keyboard seems to be already hidden.");
		}
	}

	protected void pressEnter() throws Exception {
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ENTER);
	}

	protected void pressEsc() throws Exception {
		this.getDriver().sendKeyEvent(KeyEvent.KEYCODE_ESCAPE);
	}

	/**
	 * Navigates back by BACK button
	 * 
	 * @throws Exception
	 */
	public AndroidPage navigateBack() throws Exception {
		AndroidCommonUtils.tapBackButton();
		commonSteps.WaitForTime(0.5);
		// this.getDriver().navigate().back();
		return null;
	}

	public void rotateLandscape() throws Exception {
		// AndroidCommonUtils.rotateLanscape();
		this.getDriver().rotate(ScreenOrientation.LANDSCAPE);
	}

	public void rotatePortrait() throws Exception {
		// AndroidCommonUtils.rotatePortrait();
		this.getDriver().rotate(ScreenOrientation.PORTRAIT);
	}

	public ScreenOrientation getOrientation() throws Exception {
		return this.getDriver().getOrientation();
	}

	public AndroidPage returnBySwipe(SwipeDirection direction) throws Exception {
		return null;
	};

	@Override
	public AndroidPage swipeLeft(int durationMilliseconds) throws Exception {
		DriverUtils.swipeLeft(this.getDriver(), content, durationMilliseconds);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	@Override
	public AndroidPage swipeRight(int durationMilliseconds) throws Exception {
		DriverUtils.swipeRight(this.getDriver(), content, durationMilliseconds);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	@Override
	public AndroidPage swipeUp(int durationMilliseconds) throws Exception {
		DriverUtils.swipeUp(this.getDriver(), content, durationMilliseconds);
		return returnBySwipe(SwipeDirection.UP);
	}

	public void elementSwipeRight(WebElement el, int durationMilliseconds) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + 30,
					coords.y + elementSize.height / 2,
					coords.x + elementSize.width - 90,
					coords.y + elementSize.height / 2, durationMilliseconds);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void elementSwipeUp(WebElement el, int durationMilliseconds) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 100,
					coords.x + elementSize.width / 2, coords.y,
					durationMilliseconds);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void elementSwipeDown(WebElement el, int durationMilliseconds) {
		Point coords = el.getLocation();
		Dimension elementSize = el.getSize();
		try {
			this.getDriver().swipe(coords.x + elementSize.width / 2,
					coords.y + 100, coords.x + elementSize.width / 2,
					coords.y + elementSize.height - 300, durationMilliseconds);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void dialogsPagesSwipeUp(int durationMilliseconds) throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 300,
				coords.x + elementSize.width / 2, coords.y + 50,
				durationMilliseconds);
	}

	public void dialogsPagesSwipeDown(int durationMilliseconds)
			throws Exception {
		Point coords = content.getLocation();
		Dimension elementSize = content.getSize();
		this.getDriver().swipe(coords.x + elementSize.width / 2, coords.y + 50,
				coords.x + elementSize.width / 2,
				coords.y + elementSize.height - 300, durationMilliseconds);
	}

	@Override
	public AndroidPage swipeDown(int durationMilliseconds) throws Exception {
		DriverUtils.swipeDown(this.getDriver(), content, durationMilliseconds);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeRightCoordinates_old(int durationMilliseconds)
			throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(),
				durationMilliseconds);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeRightCoordinates_old(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		DriverUtils.swipeRightCoordinates(this.getDriver(),
				durationMilliseconds, horizontalPercent);
		return returnBySwipe(SwipeDirection.RIGHT);
	}

	public AndroidPage swipeLeftCoordinates_old(int durationMilliseconds)
			throws Exception {
		DriverUtils
				.swipeLeftCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeLeftCoordinates_old(int durationMilliseconds,
			int horizontalPercent) throws Exception {
		DriverUtils.swipeLeftCoordinates(this.getDriver(),
				durationMilliseconds, horizontalPercent);
		return returnBySwipe(SwipeDirection.LEFT);
	}

	public AndroidPage swipeUpCoordinates_old(int durationMilliseconds)
			throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeUpCoordinates_old(int durationMilliseconds,
			int verticalPercent) throws Exception {
		DriverUtils.swipeUpCoordinates(this.getDriver(), durationMilliseconds,
				verticalPercent);
		return returnBySwipe(SwipeDirection.UP);
	}

	public AndroidPage swipeByCoordinates_old(int durationMilliseconds,
			int widthStartPercent, int hightStartPercent, int widthEndPercent,
			int hightEndPercent) throws Exception {
		DriverUtils.swipeByCoordinates(this.getDriver(), durationMilliseconds,
				widthStartPercent, hightStartPercent, widthEndPercent,
				hightEndPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeDownCoordinates_old(int durationMilliseconds)
			throws Exception {
		DriverUtils
				.swipeDownCoordinates(this.getDriver(), durationMilliseconds);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public AndroidPage swipeDownCoordinates_old(int durationMilliseconds,
			int verticalPercent) throws Exception {
		DriverUtils.swipeDownCoordinates(this.getDriver(),
				durationMilliseconds, verticalPercent);
		return returnBySwipe(SwipeDirection.DOWN);
	}

	public void swipeByCoordinates(int durationMilliseconds,
			int widthStartPercent, int hightStartPercent, int widthEndPercent,
			int hightEndPercent) throws Exception {
		final Dimension screenDimension = getDriver().manage().window()
				.getSize();
		this.getDriver().swipe(screenDimension.width * widthStartPercent / 100,
				screenDimension.height * hightStartPercent / 100,
				screenDimension.width * widthEndPercent / 100,
				screenDimension.height * hightEndPercent / 100,
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
	 * Swipe from y = 90% of hight to y = 10% of hight. x = width/2
	 */
	public void swipeUpCoordinates(int durationMilliseconds) throws Exception {
		swipeUpCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}

	/**
	 * Swipe from y = 90% of hight to y = 10% of hight. x = widthPercent
	 */
	public void swipeUpCoordinates(int durationMilliseconds, int widthPercent)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_END, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_START);
	}

	/**
	 * Swipe from y = 10% of hight to y = 90% of hight. x = width/2
	 */
	public void swipeDownCoordinates(int durationMilliseconds) throws Exception {
		swipeDownCoordinates(durationMilliseconds, SWIPE_DEFAULT_PERCENTAGE);
	}
	/**
	 * Swipe from y = 10% of hight to y = 90% of hight. x = widthPercent
	 */
	public void swipeDownCoordinates(int durationMilliseconds, int widthPercent)
			throws Exception {
		swipeByCoordinates(durationMilliseconds, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_START, widthPercent,
				SWIPE_DEFAULT_PERCENTAGE_END);
	}

	public void tapButtonByClassNameAndIndex(WebElement element,
			String className, int index) {
		List<WebElement> buttonsList = element.findElements(By
				.className(className));
		buttonsList.get(index).click();
	}

	public void tapByCoordinates(int widthPercent, int hightPercent)
			throws Exception {
		int x = getDriver().manage().window().getSize().getWidth()
				* widthPercent / 100;
		int y = getDriver().manage().window().getSize().getHeight()
				* hightPercent / 100;
		AndroidCommonUtils.genericScreenTap(x, y);
	}

	public void tapOnCenterOfScreen() throws Exception {
		int x = getDriver().manage().window().getSize().getWidth() / 2;
		int y = getDriver().manage().window().getSize().getHeight() / 2;
		AndroidCommonUtils.genericScreenTap(x, y);
	}
}
