package com.wearezeta.auto.osx.pages.webapp;

import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.web.locators.WebAppLocators;
import com.wearezeta.auto.web.pages.WebPage;
import com.wearezeta.auto.web.pages.WebappPagesCollection;

public class RegistrationPage extends WebPage {

	private static final int TIMEOUT_FOR_FIRST_LOAD_OF_PAGE = 25;

	private final static WebappPagesCollection webappPagesCollection = WebappPagesCollection
			.getInstance();

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger.getLog(RegistrationPage.class
			.getSimpleName());

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssNameFiled)
	private WebElement nameField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssEmailFiled)
	private WebElement emailField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssPasswordFiled)
	private WebElement passwordField;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssCreateButton)
	private WebElement createAccount;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssVerificationEmail)
	private WebElement verificationEmail;

	@FindBy(how = How.CSS, using = WebAppLocators.RegistrationPage.cssPendingEmail)
	private WebElement pendingEmail;

	@FindBy(css = WebAppLocators.RegistrationPage.cssSwitchToSignInButton)
	private WebElement switchToSignInButton;

	// TODO move to locators
	@FindBy(css = ".icon-envelope")
	private WebElement verificationEnvelope;

	@FindBy(css = WebAppLocators.RegistrationPage.cssErrorMarkedEmailField)
	private WebElement redDotOnEmailField;

	@FindBy(css = WebAppLocators.RegistrationPage.cssErrorMessages)
	private List<WebElement> errorMessages;

	public RegistrationPage(Future<ZetaWebAppDriver> lazyDriver)
			throws Exception {
		super(lazyDriver, null);
	}

	public boolean waitForRegistrationPageToFullyLoad() throws Exception {
		return DriverUtils.waitUntilElementClickable(this.getDriver(),
				emailField, TIMEOUT_FOR_FIRST_LOAD_OF_PAGE);
	}

	public LoginPage switchToLoginPage() throws Exception {
		waitForRegistrationPageToFullyLoad();
		DriverUtils
				.waitUntilElementClickable(getDriver(), switchToSignInButton);
		switchToSignInButton.click();

		return webappPagesCollection.getPage(LoginPage.class);
	}

	public void enterName(String name) throws Exception {
		nameField.click();
		nameField.clear();
		nameField.sendKeys(name);
	}

	public void enterEmail(String email) throws Exception {
		emailField.click();
		emailField.clear();
		emailField.sendKeys(email);
	}

	public void enterPassword(String password) throws Exception {
		passwordField.click();
		passwordField.clear();
		passwordField.sendKeys(password);
	}

	public void submitRegistration() throws Exception {
		assert DriverUtils.waitUntilElementClickable(this.getDriver(),
				createAccount) : "'Create' button is not clickable after timeout";
		createAccount.click();
	}

	public boolean isEnvelopeShown() throws Exception {
		return DriverUtils.waitUntilElementClickable(getDriver(),
				verificationEnvelope);
	}

	public String getVerificationEmailAddress() throws Exception {
		return verificationEmail.getText();
	}

	public String getPendingEmailAddress() throws Exception {
		return pendingEmail.getText();
	}

	public List<String> getErrorMessages() {
		return errorMessages.stream()
				.map(errorMessage -> errorMessage.getText())
				.collect(Collectors.toList());
	}

	public boolean isEmailFieldMarkedAsError() throws Exception {
		return DriverUtils
				.waitUntilLocatorIsDisplayed(
						getDriver(),
						By.cssSelector(WebAppLocators.RegistrationPage.cssErrorMarkedEmailField));
	}

	public boolean isEmailFieldMarkedAsValid() throws Exception {
		return DriverUtils
				.waitUntilLocatorDissapears(
						getDriver(),
						By.cssSelector(WebAppLocators.RegistrationPage.cssErrorMarkedEmailField));
	}

	public LoginPage openSignInPage() throws Exception {
		getDriver()
				.get(CommonUtils
						.getWebAppApplicationPathFromConfig(RegistrationPage.class)
						+ "auth/#login");
		return new LoginPage(getLazyDriver());
	}

	public void refreshPage() throws Exception {
		getDriver().get(getDriver().getCurrentUrl());
	}
}
