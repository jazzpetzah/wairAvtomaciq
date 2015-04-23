package com.wearezeta.auto.web.pages.popovers;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;
import static com.wearezeta.auto.web.locators.WebAppLocators.Common.TITLE_ATTRIBUTE_LOCATOR;
import com.wearezeta.auto.web.pages.WebPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPopoverContainer extends WebPage {

	private final static int VISIBILITY_TIMEOUT = 3; // seconds
	private static final String TOOLTIP_BACK = "Back";

	@FindBy(how = How.XPATH, using = PopoverLocators.Shared.xpathBackButton)
	private WebElement backButton;

	public AbstractPopoverContainer(ZetaWebAppDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	protected abstract String getXpathLocator();

	public void waitUntilVisibleOrThrowException() throws Exception {
		assert DriverUtils.isElementDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()), VISIBILITY_TIMEOUT) : "Popover "
				+ this.getXpathLocator()
				+ " has not been shown within "
				+ VISIBILITY_TIMEOUT + " seconds";
	}

	public boolean isVisible() throws Exception {
		return DriverUtils.isElementDisplayed(getDriver(),
				By.xpath(this.getXpathLocator()));
	}

	public boolean isBackButtonToolTipCorrect() {
		return TOOLTIP_BACK.equals(backButton
				.getAttribute(TITLE_ATTRIBUTE_LOCATOR));
	}
}
