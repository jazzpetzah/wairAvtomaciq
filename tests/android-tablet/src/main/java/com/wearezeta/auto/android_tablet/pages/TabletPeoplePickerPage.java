package com.wearezeta.auto.android_tablet.pages;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class TabletPeoplePickerPage extends AndroidTabletPage {
	@FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement closePeoplePickerBtn;

	public static final String idPickerSearch = "puet_pickuser__searchbox";
	@FindBy(id = idPickerSearch)
	public WebElement pickerSearch;

	public static final String xpathTopPeopleHeader = "//*[@id='rv_top_users']";

	public static final Function<String, String> xpathTopPeopleAvatarByName = name -> String
			.format("%s//*[@id='cwtf__startui_top_user' and .//*[@value='%s']]",
					xpathTopPeopleHeader, name.toUpperCase());

	private static final Function<String, String> xpathFoundAvatarByName = name -> String
			.format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']"
					+ "/preceding-sibling::*[@id='cv_pickuser__searchuser_chathead']",
					name);

	public TabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	private PeoplePickerPage getAndroidPeoplePickerPage() throws Exception {
		return this.getAndroidPageInstance(PeoplePickerPage.class);
	}

	public boolean waitUntilVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn))
				&& pickerSearch.getLocation().getX() >= 0;
	}

	public boolean waitUntilInvisible() throws Exception {
		return DriverUtils.waitUntilLocatorDissapears(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn))
				|| pickerSearch.getLocation().getX() < 0;
	}

	public void tapCloseButton() throws Exception {
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.id(PeoplePickerPage.idPeoplePickerClearbtn));
		closePeoplePickerBtn.click();
	}

	public void typeTextInPeopleSearch(String searchCriteria) throws Exception {
		getAndroidPeoplePickerPage().typeTextInPeopleSearch(searchCriteria);
	}

	public void tapFoundItem(String item) throws Exception {
		final By locator = By
				.xpath(PeoplePickerPage.xpathPeoplePickerContactByName
						.apply(item));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The item '%s' is not visible in People Picker search list after the defualt timeout expired",
						item);
		getDriver().findElement(locator).click();
	}

	public boolean waitUntilTopPeopleIsVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
				By.xpath(xpathTopPeopleHeader));
	}

	public void tapTopPeopleAvatar(String name) throws Exception {
		final By locator = By.xpath(xpathTopPeopleAvatarByName.apply(name));
		getDriver().findElement(locator).click();
	}

	public Optional<BufferedImage> takeAvatarScreenshot(String name)
			throws Exception {
		final By locator = By.xpath(xpathFoundAvatarByName.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("User avatar for '%s' is not visible", name);
		final WebElement theAvatar = getDriver().findElement(locator);
		return this.getElementScreenshot(theAvatar);
	}

	public boolean waitUntilAvatarIsVisible(String name) {
		try {
			this.getAndroidPeoplePickerPage().waitUserPickerFindUser(name);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public String getFirstPYMKItemName() throws Exception {
		return getAndroidPeoplePickerPage().getPYMKItemName(1);
	}

	public void tapPlusButtonOnFirstPYMKItem() throws Exception {
		getAndroidPeoplePickerPage().clickPlusOnPYMKItem(1);
	}

	public boolean waitUntilPYMKItemInvisible(String name) throws Exception {
		return getAndroidPeoplePickerPage().waitUntilPYMKItemIsInvisible(name);
	}

	public void tapFirstPYMKItem() throws Exception {
		getAndroidPeoplePickerPage().tapPYMKItem(1);
	}

	public void shortSwipeRightFirstPYMKItem() throws Exception {
		getAndroidPeoplePickerPage().shortSwipeRigthOnPYMKItem(1);
	}

	public void tapHideButtonInFirstPYMKItem() throws Exception {
		getAndroidPeoplePickerPage().clickHideButtonOnPYMKItem(1);
	}

	public void longSwipeRightFirstPYMKItem() throws Exception {
		getAndroidPeoplePickerPage().longSwipeRigthOnPYMKItem(1);
	}

	public void doShortSwipeDown() throws Exception {
		getAndroidPeoplePickerPage().doShortSwipeDown();
	}

	public void doLongSwipeDown() throws Exception {
		getAndroidPeoplePickerPage().doLongSwipeDown();
	}

	public void tapOpenOrCreateConversationButton() throws Exception {
		getAndroidPeoplePickerPage().tapOpenConversationButton();
	}

	public boolean waitUntilOpenOrConversationButtonIsVisible()
			throws Exception {
		return getAndroidPeoplePickerPage()
				.waitUntilOpenOrCreateConversationButtonIsVisible();
	}

	public boolean waitUntilOpenConversationButtonIsInvisible()
			throws Exception {
		return getAndroidPeoplePickerPage()
				.waitUntilOpenOrCreateConversationButtonIsInvisible();
	}

	public void tapCallButton() throws Exception {
		getAndroidPeoplePickerPage().tapCallButton();
	}

}
