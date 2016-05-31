package com.wearezeta.auto.web.pages.external;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.locators.ExternalLocators;
import com.wearezeta.auto.web.pages.WebPage;
import org.openqa.selenium.support.ui.Wait;

public class StartPage extends WebPage {
	
	// FIXME: Works for staging backend only
	private static final String SITE_ROOT = WebAppConstants.STAGING_SITE_ROOT;
	
	@FindBy(css = ExternalLocators.StartPage.cssGermanButton)
	private WebElement germanButton;
	
	@FindBy(css = ExternalLocators.StartPage.cssEnglishButton)
	private WebElement englishButton;
		
	public StartPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}
	
	@Override
	public void setUrl(String url) {
		// To make sure that we are redirected to staging site
		try {
			super.setUrl(transformSiteUrl(url));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected ZetaWebAppDriver getDriver() throws Exception {
		return (ZetaWebAppDriver) super.getDriver();
	}

	private static String transformSiteUrl(String url)
			throws URISyntaxException {
		final URI uri = new URI(url);
		return SITE_ROOT + uri.getPath();
	}

	public void switchToSupportPageTab() throws Exception {
		WebDriver driver = this.getDriver();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(
				DriverUtils.getDefaultLookupTimeoutSeconds(), TimeUnit.SECONDS)
				.pollingEvery(1, TimeUnit.SECONDS);
		try {
			wait.until(drv -> {
				return (drv.getWindowHandles().size() > 1);
			});
		} catch (TimeoutException e) {
			throw new TimeoutException("No Support page tab was found", e);
		}
		Set<String> handles = driver.getWindowHandles();
		handles.remove(driver.getWindowHandle());
		driver.switchTo().window(handles.iterator().next());
	}
		
	public List<WebElement> getAllLinkElements() throws Exception{
		List<WebElement> list = getDriver().findElements(By.cssSelector("a"));
		return list;
	}

	public List<WebElement> getAllImageElements() throws Exception{
		List<WebElement> list = getDriver().findElements(By.cssSelector("img"));
		return list;
	}
	
	public int getStatusCode(String href) throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();	
		HttpResponse response = client.execute(new HttpGet(href));
	    int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}

	public String getGermanValue() throws Exception {
		DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(ExternalLocators.StartPage.cssGermanValue));
		return germanButton.getAttribute("value");
	}

	public String getEnglishValue() throws Exception {
		DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(ExternalLocators.StartPage.cssEnglishValue));
		return englishButton.getAttribute("value");
	}

	public boolean isGermanTitleVisible() throws Exception {
		System.out.println("HEAD: " + getDriver().findElements(By.tagName("head")).get(0).toString());
		System.out.println("HEAD: " + getDriver().findElements(By.tagName("head")).get(0).getText());
		System.out.println("HEAD: " + getDriver().findElement(By.tagName("title")).getText());
		System.out.println("HEAD: " + getDriver().findElement(By.xpath("/html/head/title")));
		System.out.println("HEAD: " + getDriver().findElement(By.xpath("/html/head/title")).getTagName());
		System.out.println("HEAD: " + getDriver().findElement(By.xpath("/html/head/title")).getText());
		System.out.println("RESULT: " + DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath("/html/head/title")));
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.xpath("/html/head/title"));
	}

	public boolean isEnglishTitleVisible() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(ExternalLocators.StartPage.cssEnglishTitle));
	}

	public boolean isEnglish() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(ExternalLocators.StartPage.cssEnglishSite));
	}

	public boolean isGerman() throws Exception {
		return DriverUtils.waitUntilLocatorIsDisplayed(getDriver(), By.cssSelector(ExternalLocators.StartPage.cssGermanSite));
	}

	public void changeLanguageTo(String language) throws Exception {
		if (language.equals("german")) {
			WebElement select = getDriver().findElement(By.cssSelector(ExternalLocators.StartPage.cssEnglishButton));
			Select dropdown = new Select(select);
			dropdown.selectByVisibleText("DEUTSCH");
		}
		if (language.equals("english")) {
			WebElement select = getDriver().findElement(By.cssSelector(ExternalLocators.StartPage.cssGermanButton));
			Select dropdown = new Select(select);
			dropdown.selectByVisibleText("ENGLISH");
		}
	}
}
