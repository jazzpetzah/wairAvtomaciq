package com.wearezeta.auto.osx.pages.external;

import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;
import java.util.LinkedList;
import java.util.concurrent.Callable;

public class GoogleLoginPage extends WebPage {

	private final WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@FindBy(id = "Email")
	private WebElement emailField;

	@FindBy(id = "Passwd")
	private WebElement passwordField;

	@FindBy(id = "next")
	private WebElement nextButton;

	@FindBy(id = "signIn")
	private WebElement signInButton;

	@FindBy(id = "submit_approve_access")
	private WebElement approveAccessButton;

	public GoogleLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setEmail(String email) throws Exception {
		executeOnLatestWindow(() -> {
			DriverUtils.waitUntilLocatorAppears(getDriver(), By.id("Email"));
			emailField.clear();
			emailField.sendKeys(email);
			return true;
		});
	}

	public void setPassword(String password) throws Exception {
		executeOnLatestWindow(() -> {
			// this wait is needed when the NEXT button thing happens
			DriverUtils.waitUntilLocatorAppears(getDriver(), By.id("Password"));
			passwordField.clear();
			passwordField.sendKeys(password);
			return true;
		});
	}

	public boolean hasNextButton() throws Exception {
		return executeOnLatestWindow(() -> {
			return getDriver().findElements(By.id("next")).size() > 0;
		});
	}

	public void clickNext() throws Exception {
		executeOnLatestWindow(() -> {
			nextButton.click();
			return true;
		});
	}

	public boolean hasApproveButton() throws Exception {
		return executeOnLatestWindow(() -> {
			return getDriver().findElements(By.id("submit_approve_access"))
					.size() > 0;
		});
	}

	public void clickApprove() throws Exception {
		executeOnLatestWindow(() -> {
			assert DriverUtils.waitUntilElementClickable(getDriver(),
					approveAccessButton) : "Can not click Approve button";
			approveAccessButton.click();
			return true;
		});
	}

	public void clickSignIn() throws Exception {
		executeOnLatestWindow(() -> {
			signInButton.click();
			return true;
		});
	}

	private <T> T executeOnLatestWindow(Callable<T> function) throws Exception {
		LinkedList<String> windowHandles = new LinkedList<>(getDriver()
				.getWindowHandles());
		// switch to latest window
		getDriver().switchTo().window(windowHandles.getLast());
		T result = function.call();
		// switch to oldest window
		getDriver().switchTo().window(windowHandles.getFirst());
		return result;
	}

}
