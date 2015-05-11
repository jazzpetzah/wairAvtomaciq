package com.wearezeta.auto.web.pages.external;

import java.util.Set;
import java.util.concurrent.Future;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.pages.PagesCollection;
import com.wearezeta.auto.web.pages.PeoplePickerPage;
import com.wearezeta.auto.web.pages.WebPage;

public class GoogleLoginPage extends WebPage {

	@FindBy(id = "Email")
	private WebElement emailField;

	@FindBy(id = "Passwd")
	private WebElement passwordField;

	@FindBy(id = "signIn")
	private WebElement signInButton;

	public GoogleLoginPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setEmail(String email) {
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void setPassword(String password) {
		passwordField.clear();
		passwordField.sendKeys(password);
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
		PagesCollection.peoplePickerPage = new PeoplePickerPage(getLazyDriver());
	}
}
