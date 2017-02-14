package com.wearezeta.auto.web.pages;

import com.google.common.base.Function;
import com.wearezeta.auto.common.driver.DriverUtils;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContentSearchPage extends WebPage {

    private static final Logger LOG = ZetaLogger.getLog(ContentSearchPage.class.getSimpleName());

    private static final int RESULT_TIMEOUT = 5;

    @FindBy(css = WebAppLocators.ContentSearchPage.cssSearchResults)
    private List<WebElement> searchResults;

    public ContentSearchPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void clickResult(int index) {
        searchResults.get(index).click();
    }

    public int waitForPresentResults() throws Exception {
        WebDriverWait wait = new WebDriverWait(getDriver(), RESULT_TIMEOUT);
        return wait.withTimeout(RESULT_TIMEOUT, TimeUnit.SECONDS)
                .until((Function<? super WebDriver, List<WebElement>>) ExpectedConditions.visibilityOfAllElements(searchResults))
                .size();
    }

    public boolean waitForVisibilityOfResultContainingText(String text) throws Exception {
        return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), 
                By.xpath(WebAppLocators.ContentSearchPage.xpathSearchResultByText.apply(text)));
    }
    
    public boolean waitForInvisibilityOfResultContainingText(String text) throws Exception {
        return DriverUtils.waitUntilLocatorDissapears(getDriver(), 
                By.xpath(WebAppLocators.ContentSearchPage.xpathSearchResultByText.apply(text)));
    }

    public void clickResultWithText(String text) {
        List<WebElement> resultsWithText = searchResults.stream()
                .filter((result) -> result.getText().contains(text))
                .collect(Collectors.toList());
        if (resultsWithText.size() < 1) {
            throw new IllegalStateException("There were no results for the given text but we need exactly one match.");
        }
        if (resultsWithText.size() > 1) {
            throw new IllegalStateException("There were multiple results for the given text but we need exactly one match.");
        }
        resultsWithText.get(0).click();
    }

}
