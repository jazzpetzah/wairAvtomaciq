package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

class SearchPage extends AbstractPopoverPage {
    public final static Function<String, String> xpathSearchResultsAvatarByName = name -> String
            .format("//*[@id='ttv_pickuser__searchuser_name' and @value='%s']/parent::*",
                    name);

    @FindBy(id = PeoplePickerPage.idPickerBtnDone)
    private WebElement addToConversationButton;

    public SearchPage(Future<ZetaAndroidDriver> lazyDriver,
                      AbstractPopoverContainer container) throws Exception {
        super(lazyDriver, container);
    }

    private WebElement getSearchInput() throws Exception {
        return this.getDriver().findElement(this.getContainer().getLocator())
                .findElement(By.id(PeoplePickerPage.idPickerSearch));
    }

    public void enterSearchText(String text) throws Exception {
        getSearchInput().sendKeys(text);
    }

    public void tapAvatarFromSearchResults(String name) throws Exception {
        final By locator = By.xpath(xpathSearchResultsAvatarByName.apply(name));
        getElement(locator, String
                .format("The avatar of '%s' has not been shown in search resulst after timeout", name)).click();
    }

    public void tapAddToConversationButton() {
        addToConversationButton.click();
    }

}
