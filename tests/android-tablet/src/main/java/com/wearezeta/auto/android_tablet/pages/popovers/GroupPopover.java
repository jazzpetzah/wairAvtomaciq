package com.wearezeta.auto.android_tablet.pages.popovers;

import java.util.concurrent.Future;

import org.openqa.selenium.By;

import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

public class GroupPopover extends AbstractPopoverContainer {
	public final static String idRootLocator = "fl__participant_dialog__root";

	private ParticipantsPage participantsPage;
	private ConnectedParticipantPage connectedParticipantPage;
	private ParticipantOutgoingConnectionPage outgoingConnectionPage;

	public GroupPopover(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
		super(lazyDriver);
		this.participantsPage = new ParticipantsPage(lazyDriver, this);
		this.connectedParticipantPage = new ConnectedParticipantPage(
				lazyDriver, this);
		this.outgoingConnectionPage = new ParticipantOutgoingConnectionPage(
				lazyDriver, this);
	}

	@Override
	protected By getLocator() {
		return By.id(idRootLocator);
	}

	public void selectMenuItem(String itemName) throws Exception {
		this.participantsPage.selectMenuItem(itemName);
	}

	public void tapConfirmLeaveButton() throws Exception {
		this.participantsPage.tapConfirmLeaveButton();
	}

	public void tapOptionsButton() throws Exception {
		this.participantsPage.tapOptionsButton();
	}

	public void tapRemoveButton() throws Exception {
		this.connectedParticipantPage.tapRemoveButton();
	}

	public void tapConfirmRemovalButton() throws Exception {
		this.connectedParticipantPage.tapConfirmRemovalButton();
	}

	public boolean waitForParticipantAvatarVisible(String name)
			throws Exception {
		return this.participantsPage.waitForParticipantAvatarVisible(name);
	}

	public void tapParticipantAvatar(String name) throws Exception {
		this.participantsPage.tapParticipantAvatar(name);
	}

	public boolean waitForParticipantAvatarNotVisible(String name)
			throws Exception {
		return this.participantsPage.waitForParticipantAvatarNotVisible(name);
	}

	public void tapConnectButton() throws Exception {
		this.outgoingConnectionPage.tapConnectButton();
	}

	public boolean waitUntilPendingButtonIsVisible() throws Exception {
		return this.outgoingConnectionPage.waitUntilPendingButtonIsVisible();
	}

	public void tapCloseButton() throws Exception {
		this.participantsPage.tapCloseButton();
	}

	public void renameConversation(String newName) throws Exception {
		this.participantsPage.renameConversation(newName);
	}

	public void tapOpenConversationButton() throws Exception {
		this.participantsPage.tapOpenConversationButton();
	}

	public boolean waitUntilUserNameVisible(String expectedName)
			throws Exception {
		return this.connectedParticipantPage
				.waitUntilUserNameVisible(expectedName);
	}

	public boolean waitUntilUserEmailVisible(String expectedEmail)
			throws Exception {
		return this.connectedParticipantPage
				.waitUntilUserEmailVisible(expectedEmail);
	}

	public boolean waitUntilConversationNameVisible(String expectedName)
			throws Exception {
		return this.participantsPage
				.waitUntilConversationNameVisible(expectedName);
	}

	public boolean waitUntilSubheaderIsVisible(String expectedText)
			throws Exception {
		return this.participantsPage.waitUntilSubheaderIsVisible(expectedText);

	}

	public boolean waitUntilMenuItemIsVisible(String itemName) throws Exception {
		return participantsPage.isMenuItemVisible(itemName);
	}

	public boolean waitUntilMenuItemIsInvisible(String itemName)
			throws Exception {
		return participantsPage.isMenuItemInvisible(itemName);
	}
}
