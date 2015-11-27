package com.wearezeta.auto.android.pages.registration;

import java.util.concurrent.Future;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wearezeta.auto.android.pages.AndroidPage;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.pages.PeoplePickerPage;
import com.wearezeta.auto.common.driver.DriverUtils;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

/**
 * This page represents the email sign in screen, which you can reach from the
 * welcome screen. From here, you can choose to go to the phone signin page, go
 * back to the welcome screen, or log in with an existing email and password
 * 
 * @author deancook
 *
 */
public class EmailSignInPage extends AndroidPage {
	private static final Function<String, String> xpathLoginMessageByText = text -> String
			.format("//*[@id='message' and @value='%s']", text);

	private static final String xpathAlertOKButton = "//*[starts-with(@id, 'button') and @value='OK']";
	@FindBy(xpath = xpathAlertOKButton)
	private WebElement alertOKButton;

	@FindBy(id = PeoplePickerPage.idPeoplePickerClearbtn)
	private WebElement pickerClearBtn;

	public static final String idLoginInput = "get__sign_in__email";
	@FindBy(id = idLoginInput)
	private WebElement loginInput;

	public static final String idPasswordInput = "get__sign_in__password";
	@FindBy(id = idPasswordInput)
	private WebElement passwordInput;

	public static final String idLoginButton = "pcb__signin__email";
	@FindBy(id = idLoginButton)
	protected WebElement confirmSignInButton;

	public EmailSignInPage(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
	}

	public void setLogin(String login) throws Exception {
		loginInput.click();
		String cleanupString = "";
		for (int i = 0; i < 40; i++) {
			cleanupString += Keys.ARROW_RIGHT + "" + Keys.DELETE;
		}
		loginInput.sendKeys(cleanupString);
		loginInput.sendKeys(login);
	}

	public void setPassword(String password) throws Exception {
		passwordInput.click();
		passwordInput.sendKeys(password);
	}

	/**
	 * If the user already has a phone number attached to their account, this
	 * page will go directly to the start UI, or else it will return a page in
	 * which the user is asked to add a phone number
	 * 
	 * @param enableLoginWorkaround
	 *            set to try to accept any login alert automatically and retry
	 *            until timeout happens
	 * @param timeoutSeconds
	 *            sign in timeout
	 * 
	 * @return either a {@link AddPhoneNumberPage} or {@link ContactListPage}
	 * @throws Exception
	 */
	public void logIn(boolean enableLoginWorkaround, int timeoutSeconds)
			throws Exception {
		confirmSignInButton.click();
		waitForLogInToComplete(enableLoginWorkaround, timeoutSeconds);
	}

	public void waitForLogInToComplete(boolean enableLoginWorkaround,
			int timeoutSeconds) throws Exception {
		final long millisecondsStarted = System.currentTimeMillis();
		do {
			if (DriverUtils.waitUntilLocatorDissapears(this.getDriver(),
					By.id(idLoginButton), 5)) {
				// FIXME: Workaround for 403 error from the backend
				if (enableLoginWorkaround) {
					if (!DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
							By.xpath(xpathAlertOKButton), 1)) {
						return;
					}
				} else {
					return;
				}
			}
			if (enableLoginWorkaround
					&& DriverUtils.waitUntilLocatorIsDisplayed(getDriver(),
							By.xpath(xpathAlertOKButton), 1)) {
				acceptErrorMessage();
				confirmSignInButton.click();
			}
		} while (System.currentTimeMillis() - millisecondsStarted <= timeoutSeconds * 1000);
		throw new IllegalStateException(String.format(
				"Login screen is still visible after %s seconds timeout",
				timeoutSeconds));
	}

	public boolean waitForAddPhoneNumberAppear() throws Exception {
		return DriverUtils.waitUntilLocatorAppears(this.getDriver(),
				By.id(AddPhoneNumberPage.idNotNowButton), 2);
	}

	public void verifyErrorMessageText(String expectedMsg) throws Exception {
		final By locator = By.xpath(xpathLoginMessageByText.apply(expectedMsg));
		assert DriverUtils
				.waitUntilLocatorIsDisplayed(getDriver(), locator, 15) : String
				.format("Error message '%s' is not visible on the screen",
						expectedMsg);
	}

	public void acceptErrorMessage() {
		alertOKButton.click();
	}
}
