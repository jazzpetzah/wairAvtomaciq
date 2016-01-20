package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class SingleUserPopover extends AbstractPopoverContainer {
	public final static String idRootLocator = "fl__participant_dialog__root";

	private SingleConnectedUserDetalsPage singleConnectedUserDetalsPage;
	private SearchPage searchPage;

	public SingleUserPopover(Future<ZetaAndroidDriver> lazyDriver)
			throws Exception {
		super(lazyDriver);
		this.singleConnectedUserDetalsPage = new SingleConnectedUserDetalsPage(
				lazyDriver, this);
		this.searchPage = new SearchPage(lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public void tapOptionsButton() throws Exception {
		this.singleConnectedUserDetalsPage.tapOptionsButton();
	}

	public void selectMenuItem(String itemName) throws Exception {
		this.singleConnectedUserDetalsPage.selectMenuItem(itemName);
	}

	public boolean isMenuItemVisible(String itemName) throws Exception {
		return this.singleConnectedUserDetalsPage.isMenuItemVisible(itemName);
	}

	public boolean waitUntilUserNameVisible(String expectedName)
			throws Exception {
		return this.singleConnectedUserDetalsPage
				.waitUntilUserNameVisible(expectedName);
	}

	public boolean waitUntilUserEmailVisible(String expectedEmail)
			throws Exception {
		return this.singleConnectedUserDetalsPage
				.waitUntilUserEmailVisible(expectedEmail);
	}

	public void tapAddPeopleButton() throws Exception {
		this.singleConnectedUserDetalsPage.tapAddPeopleButton();
	}

	public void enterSearchText(String text) throws Exception {
		this.searchPage.enterSearchText(text);
	}

	public void tapAvatarFromSearchResults(String name) throws Exception {
		this.searchPage.tapAvatarFromSearchResults(name);
	}

	public void tapAddToConversationButton() throws Exception {
		this.searchPage.tapAddToConversationButton();
	}

	public void tapCloseButton() throws Exception {
		this.singleConnectedUserDetalsPage.tapCloseButton();
	}

}
