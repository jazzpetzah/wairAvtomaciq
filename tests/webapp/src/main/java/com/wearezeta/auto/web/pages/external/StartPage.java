package com.wearezeta.auto.web.pages.external;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.common.WebAppConstants;
import com.wearezeta.auto.web.pages.WebPage;

public class StartPage extends WebPage {
	
	// FIXME: Works for staging backend only
	private static final String SITE_ROOT = WebAppConstants.STAGING_SITE_ROOT;
	
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
		
	public List<WebElement> getAllElements() throws Exception{
		List<WebElement> list = getDriver().findElements(By.cssSelector("a"));	//By.tagName("a") also works
		return list;
	}
	
	public int getStatusCode(String href) throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();	
		HttpResponse response = client.execute(new HttpGet(href));
	    int statusCode = response.getStatusLine().getStatusCode();
		return statusCode;
	}

}
