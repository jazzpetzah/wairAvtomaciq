package com.wearezeta.auto.web.pages.external;

import java.util.Set;
import java.util.concurrent.Future;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.PeoplePickerPage;
import com.wearezeta.auto.web.pages.WebPage;

public class GoogleLoginPage extends WebPage {

	@FindBy(id = "Email")
	private WebElement emailField;

	@FindBy(id = "Passwd")
	private WebElement passwordField;

	@FindBy(id = "next")
	private WebElement nextButton;

	@FindBy(id = "signIn")
	private WebElement signInButton;

	public GoogleLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setEmail(String email) throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(), By.id("Email"));
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void setPassword(String password) throws Exception {
		// this wait is needed when the NEXT button thing happens
		DriverUtils.waitUntilLocatorAppears(getDriver(), By.id("Password"));
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public boolean hasNextButton() throws Exception {
		return this.getDriver().findElements(By.id("next")).size() > 0;
	}

	public void clickNext() {
		nextButton.click();
	}

	public PeoplePickerPage clickSignIn() throws Exception {
		final Set<String> handles = this.getDriver().getWindowHandles();
		signInButton.click();
		// wait for popup to close
		this.getWait().until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver input) {
				return input.getWindowHandles().size() < handles.size();
			}
		});
		// switch back to main window
		this.getDriver().switchTo()
				.window(this.getDriver().getWindowHandles().iterator().next());
		return new PeoplePickerPage(getLazyDriver());
	}

}
