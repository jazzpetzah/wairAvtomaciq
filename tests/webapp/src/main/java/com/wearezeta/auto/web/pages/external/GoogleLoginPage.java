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
import com.wearezeta.auto.web.pages.WebPage;

public class GoogleLoginPage extends WebPage {

	// TODO move to Locators
	private static final String EMAIL_ID = "Email";
	private static final String PASSWORD_ID = "Passwd";
	private static final String NEXT_BUTTON_ID = "next";
	private static final String SIGN_IN_BUTTON_ID = "signIn";
	private static final String SUBMIT_APPROVE_ACCESS_BUTTON_ID = "submit_approve_access";

	@FindBy(id = EMAIL_ID)
	private WebElement emailField;

	@FindBy(id = PASSWORD_ID)
	private WebElement passwordField;

	@FindBy(id = NEXT_BUTTON_ID)
	private WebElement nextButton;

	@FindBy(id = SIGN_IN_BUTTON_ID)
	private WebElement signInButton;

	@FindBy(id = SUBMIT_APPROVE_ACCESS_BUTTON_ID)
	private WebElement approveAccessButton;

	public GoogleLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setEmail(String email) throws Exception {
		DriverUtils.waitUntilLocatorAppears(getDriver(), By.id(EMAIL_ID));
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void setPassword(String password) throws Exception {
		// this wait is needed when the NEXT button thing happens
		DriverUtils.waitUntilLocatorAppears(getDriver(), By.id(PASSWORD_ID));
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public boolean hasNextButton() throws Exception {
		return this.getDriver().findElements(By.id(NEXT_BUTTON_ID)).size() > 0;
	}

	public void clickNext() throws Exception {
		nextButton.click();
	}

	public boolean hasApproveButton() throws Exception {
		return getDriver().findElements(By.id(SUBMIT_APPROVE_ACCESS_BUTTON_ID))
				.size() > 0;
	}

	public void clickApprove() throws Exception {
		assert DriverUtils.waitUntilElementClickable(getDriver(),
				approveAccessButton) : "Can not click Approve button";
		approveAccessButton.click();
	}

	public void clickSignIn() throws Exception {
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
	}

}
