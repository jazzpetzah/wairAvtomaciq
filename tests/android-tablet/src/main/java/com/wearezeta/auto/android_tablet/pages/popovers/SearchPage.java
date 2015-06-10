package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class SearchPage extends AbstractPopoverPage {
	public static final String idSearchInput = "puet_pickuser__searchbox";

	public final static Function<String, String> xpathSearchResultsAvatarByName = name -> String
			.format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*",
					name);

	public static final String xpathAddToConversationButton = "//*[@id='ttv_pickuser_confirmbutton__title' and @value='ADD TO CONVERSATION']/parent::*";
	@FindBy(xpath = xpathAddToConversationButton)
	private WebElement addToConversationButton;

	public SearchPage(Future<ZetaAndroidDriver> lazyDriver,
			AbstractPopoverContainer container) throws Exception {
		super(lazyDriver, container);
	}

	private WebElement getSearchInput() throws Exception {
		return this.getDriver().findElement(this.getContainer().getLocator())
				.findElement(By.id(idSearchInput));
	}

	public void enterSearchText(String text) throws Exception {
		getSearchInput().sendKeys(text);
	}

	public void tapAvatarFromSearchResults(String name) throws Exception {
		final By locator = By.xpath(xpathSearchResultsAvatarByName.apply(name));
		assert DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), locator) : String
				.format("The avatar of '%s' has not been shown in search resulst after timeout",
						name);
		this.getDriver().findElement(locator).click();
	}

	public void tapAddToConversationButton() {
		addToConversationButton.click();
	}

}
