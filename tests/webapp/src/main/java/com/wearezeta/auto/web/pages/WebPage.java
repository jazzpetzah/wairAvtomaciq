package com.wearezeta.auto.web.pages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.openqa.selenium.Alert;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebPage extends BasePage {

    @Override
    protected ZetaWebAppDriver getDriver() throws Exception {
        return (ZetaWebAppDriver) super.getDriver();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Future<ZetaWebAppDriver> getLazyDriver() {
        return (Future<ZetaWebAppDriver>) super.getLazyDriver();
    }

    private String url = null;

    public String getUrl() {
        return this.url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public WebPage(Future<ZetaWebAppDriver> lazyDriver) throws Exception {
        this(lazyDriver, null);
    }

    public WebPage(Future<ZetaWebAppDriver> lazyDriver, String url)
            throws Exception {
        super(lazyDriver);

        this.url = url;
    }

    public void navigateTo() throws Exception {
        if (this.url != null) {
            this.getDriver().navigate().to(this.url);
        } else {
            throw new RuntimeException(
                    String.format(
                            "'%s' page does not support direct navigation and can be loaded from other pages only",
                            this.getClass().getSimpleName()));
        }
    }

    public static void clearPagesCollection() throws IllegalArgumentException,
            IllegalAccessException {
        clearPagesCollection(WebappPagesCollection.class, WebPage.class);
    }

    public void acceptAlert() throws Exception {
        Alert popup = getDriver().switchTo().alert();
        popup.accept();
    }

    /**
     * Will add the query parameter "hl" to the current URL and load it to change the language of the page. Depending on
     * already added query parameter, we need to use & or ? to add it. This will probably only work on Login/Registering page.
     *
     * @param language Currently can be "de" or "en"
     * @throws Exception
     */
    public void switchLanguage(String language) throws Exception {
        String currentUrl = this.getDriver().getCurrentUrl();
        URL url = new URL(currentUrl);
        if (url.getQuery() == null) {
            this.getDriver().navigate().to(currentUrl + "?hl=" + language);
        } else {
            this.getDriver().navigate().to(currentUrl + "&hl=" + language);
        }
    }

    /**
     * Returns all inner text of html element on a page. Probably the whole text that is displayed.
     *
     * @return all text of a page
     * @throws Exception
     */
    public String getText() throws Exception {
        return getDriver().findElement(By.tagName("body")).getText();
    }

    /**
     * Returns a list of all placeholders of text boxes on the page. A placeholder is a text that is only shown if the user
     * has not written anything into the field yet.
     *
     * @return list of placeholders
     * @throws Exception
     */
    public List<String> getPlaceholders() throws Exception {
        List<WebElement> inputElements = getDriver().findElements(By.tagName("input"));
        List<String> placeholders = new ArrayList<>();
        for (WebElement element : inputElements) {
            if (element.isDisplayed()) {
                placeholders.add(element.getAttribute("placeholder"));
            }
        }
        return placeholders;
    }

    /**
     * Returns a list of all button captions on a page.
     *
     * @return list of captions
     * @throws Exception
     */
    public List<String> getButtonValues() throws Exception {
        List<WebElement> inputElements = getDriver().findElements(By.cssSelector("input[type='submit']"));
        List<String> values = new ArrayList<>();
        for (WebElement element : inputElements) {
            if (element.isDisplayed()) {
                values.add(element.getAttribute("value"));
            }
        }
        return values;
    }
}
