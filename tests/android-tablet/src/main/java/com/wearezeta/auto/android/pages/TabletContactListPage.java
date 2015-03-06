package com.wearezeta.auto.android.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;
import com.wearezeta.auto.common.log.ZetaLogger;

public class TabletContactListPage extends ContactListPage {

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idYourName")
	private WebElement profileLink;

	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ContactListPage.CLASS_NAME, locatorKey = "idOpenStartUIButton")
	private WebElement peoplePickerButton;

	@SuppressWarnings("unused")
	private static final Logger log = ZetaLogger
			.getLog(TabletContactListPage.class.getSimpleName());

	public TabletContactListPage(ZetaAndroidDriver driver, WebDriverWait wait)
			throws Exception {
		super(driver, wait);
	}

	public void tapOnProfileLink() {
		getWait().until(ExpectedConditions.visibilityOf(profileLink));
		profileLink.click();
	}

	public TabletDialogPage initDialogPage() throws Exception {
		return new TabletDialogPage(getDriver(), getWait());
	}

	public boolean isPeoplePickerButtonVisible() throws NoSuchElementException {
		try {
			return peoplePickerButton.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public String getSelfName() {
		return profileLink.getText();
	}

}