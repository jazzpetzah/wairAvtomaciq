package com.wearezeta.auto.web.pages;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.log.ZetaLogger;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;

import com.wearezeta.auto.common.BasePage;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WebPage extends BasePage {

    private static final Logger log = ZetaLogger.getLog(WebPage.class.getSimpleName());
    
    private static final int WIRE_LOADED_MAX_RETRY = 20;
    private static final int WIRE_LOADED_WAIT_MS = 1000;

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

    public String getCurrentUrl() throws Exception {
        return getDriver().getCurrentUrl();
    }

    public void setUrl(String url) {
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
     * Will add the query parameter "hl" to the current URL and load it to change the language of the page. Depending on already
     * added query parameter, we need to use & or ? to add it. This will probably only work on Login/Registering page.
     *
     * @param language Currently can be "de" or "en"
     * @throws Exception
     */
    public void switchLanguage(String language) throws Exception {
        String currentUrl = this.getDriver().getCurrentUrl();
        log.info("Current URL: " + currentUrl);
        URL url = new URL(currentUrl);
        if (url.getQuery() == null) {
            this.getDriver().get(currentUrl + "?hl=" + language);
        } else {
            this.getDriver().get(currentUrl + "&hl=" + language);
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
     * Returns a list of all placeholders of text boxes on the page. A placeholder is a text that is only shown if the user has
     * not written anything into the field yet.
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

    public String getPageTitle() throws Exception {
        return getDriver().getTitle();
    }

    /**
     * Breaks the session to a certain device through injecting Javascript that removes the session state in cryptobox
     *
     * @param deviceId Device id
     * @throws Exception
     */
    public void breakSession(String deviceId) throws Exception {
        String breakSession = "s = wire.app.repository.cryptography.cryptobox.store.sessions;\n" +
                "cs = s[Object.keys(s).filter((x) => x.endsWith(\"" + deviceId + "\"))[0]];\n" +
                "cs.session_states = {};";
        getDriver().executeScript(breakSession);
    }

    /**
     * Disables the Ad banner for the webapp to make sure tests are not affected by it.
     *
     * @throws Exception
     */
    public void disableAdBanner() throws Exception {
        Boolean wireLoaded;
        int retry = WIRE_LOADED_MAX_RETRY;
        do{
            wireLoaded = (Boolean) getDriver().executeScript("console.log('checking for app wire');\n"
                + "return typeof wire !== \"undefined\" && typeof wire.app !== \"undefined\" && typeof wire.app.repository !== \"undefined\"");
            retry--;
            Thread.sleep(WIRE_LOADED_WAIT_MS);
        }while(!wireLoaded && retry > 0);
        getDriver().executeScript("console.log('setting banner true');\n"
                + "wire.app.repository.user.properties.app_banner = true");
    }
}
