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

	public void tapConfirmLeaveButton() {
		this.participantsPage.tapConfirmLeaveButton();
	}

	public void tapOptionsButton() {
		this.participantsPage.tapOptionsButton();
	}

	public void tapRemoveButton() {
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

	public void setConnectionMessage(String text) throws Exception {
		this.outgoingConnectionPage.setMessage(text);
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
}
