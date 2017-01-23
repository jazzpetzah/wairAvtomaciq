package com.wearezeta.auto.web.pages.popovers;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Future;

import com.wearezeta.auto.common.driver.ZetaWebAppDriver;
import com.wearezeta.auto.web.locators.PopoverLocators;

public class SingleUserPopoverContainer extends PeoplePopoverContainer {

    private SingleUserInfoPopoverPage singleUserPopoverPage;
    private ConnectedParticipantInfoPopoverPage connectedParticipantInfoPopoverPage;
    private BlockUserConfirmationPopoverPage blockUserConfirmationPopoverPage;
    private PendingParticipantInfoPopoverPage pendingParticipantInfoPopoverPage;
    private BlockedParticipantInfoPopoverPage blockedParticipantInfoPopoverPage;

    public SingleUserPopoverContainer(Future<ZetaWebAppDriver> lazyDriver)
            throws Exception {
        super(lazyDriver);
        this.singleUserPopoverPage = new SingleUserInfoPopoverPage(lazyDriver,
                this);
        this.blockUserConfirmationPopoverPage = new BlockUserConfirmationPopoverPage(
                lazyDriver, this);
        this.pendingParticipantInfoPopoverPage = new PendingParticipantInfoPopoverPage(
                lazyDriver, this);
        this.connectedParticipantInfoPopoverPage = new ConnectedParticipantInfoPopoverPage(
                lazyDriver, this);
        this.blockedParticipantInfoPopoverPage = new BlockedParticipantInfoPopoverPage(
                lazyDriver, this);
    }

    @Override
    protected String getXpathLocator() {
        return PopoverLocators.SingleUserPopover.xpathRootLocator;
    }

    private AbstractUserInfoPopoverPage getCurrentUserInfoPage()
            throws Exception {
        if (this.singleUserPopoverPage.isCurrent()) {
            return this.singleUserPopoverPage;
        } else if (this.pendingParticipantInfoPopoverPage.isCurrent()) {
            return this.pendingParticipantInfoPopoverPage;
        } else if (this.blockedParticipantInfoPopoverPage.isCurrent()) {
            return this.blockedParticipantInfoPopoverPage;
        } else {
            throw new RuntimeException(
                    "The current popover page is neither connected user info nor pending-connected user info.");
        }
    }

    public void clickAddPeopleButton() throws Exception {
        this.singleUserPopoverPage.clickAddPeopleButton();
    }

    public void selectUserFromSearchResult(String user) throws Exception {
        this.addPeoplePopoverPage.selectUserFromSearchResult(user);
    }

    public String getUserName() throws Exception {
        return getCurrentUserInfoPage().getUserName();
    }

    public boolean isAddButtonVisible() throws Exception {
        return this.singleUserPopoverPage.isAddButtonVisible();
    }

    public boolean isBlockButtonVisible() {
        return this.singleUserPopoverPage.isBlockButtonVisible();
    }

    public boolean isUnblockButtonVisible() {
        return this.singleUserPopoverPage.isUnblockButtonVisible();
    }


    public boolean isRemoveButtonVisible() throws Exception {
        return getCurrentUserInfoPage().isRemoveButtonVisible();
    }

    public boolean isOpenConvButtonVisible() {
        return this.connectedParticipantInfoPopoverPage
                .isOpenConvButtonVisible();
    }

    public void clickOpenConvButton() {
        this.connectedParticipantInfoPopoverPage.clickOpenConversationButton();
    }

    public boolean isAvatarVisible() throws Exception {
        return getCurrentUserInfoPage().isAvatarVisible();
    }

    public void clickBlockButton() {
        this.singleUserPopoverPage.clickBlockButton();
    }

    public void clickUnblockButton() throws Exception {
        this.singleUserPopoverPage.clickUnblockButton();
    }

    public void switchToDevicesTab() throws Exception {
        this.singleUserPopoverPage.switchToDevicesTab();
    }

    public void switchToDetailsTab() {
        this.singleUserPopoverPage.switchToDetailsTab();
    }

    public String getDevicesText() {
        return this.singleUserPopoverPage.getDevicesText();
    }

    public boolean isUserVerified() throws Exception {
        return this.singleUserPopoverPage.isUserVerified();
    }

    public List<String> getDeviceIds() {
        return this.singleUserPopoverPage.getDeviceIds();
    }

    public List<String> getVerifiedDeviceIds() throws Exception {
        return this.singleUserPopoverPage.getVerifiedDeviceIds();
    }

    public boolean waitForDevices() throws Exception {
        return this.singleUserPopoverPage.waitForDevices();
    }

    public void clickDevice(String deviceId) throws Exception {
        this.singleUserPopoverPage.clickDevice(deviceId);
    }

    public void clickFirstDevice() throws Exception {
        this.singleUserPopoverPage.clickFirstDevice();
    }

    public void clickConfirmButton() {
        this.blockUserConfirmationPopoverPage.clickConfirmButton();
    }

    public boolean isPendingButtonVisible() {
        return this.pendingParticipantInfoPopoverPage.isPendingButtonVisible();
    }

    public void clickPendingButton() {
        this.pendingParticipantInfoPopoverPage.clickPendingButton();
    }

    public String getPendingButtonCaption() {
        return this.pendingParticipantInfoPopoverPage.getPendingButtonCaption();
    }

    public String getOpenConvButtonCaption() {
        return this.connectedParticipantInfoPopoverPage
                .getOpenConvButtonCaption();
    }

    public boolean isPendingTextBoxVisible() {
        return this.pendingParticipantInfoPopoverPage
                .isPendingTextBoxDisplayed();
    }

    public boolean isUserMailInvisible() throws Exception {
        return getCurrentUserInfoPage().isMailInvisible();
    }

    public boolean isUserMailVisible() throws Exception {
        return getCurrentUserInfoPage().isMailVisible();
    }

    public String getUserMail() throws Exception {
        return getCurrentUserInfoPage().getMailText();
    }

    public String getPendingButtonToolTip() {
        return this.pendingParticipantInfoPopoverPage.getPendingButtonToolTip();
    }

    public String getOpenConvButtonToolTip() {
        return this.connectedParticipantInfoPopoverPage
                .getOpenConvButtonToolTip();
    }

    public String getMailHref() throws Exception {
        return getCurrentUserInfoPage().getMailHref();
    }

    public BufferedImage getAvatar() throws IllegalStateException, Exception {
        return getCurrentUserInfoPage().getAvatar();
    }

    public String getUniqueUsername() throws Exception {
        return getCurrentUserInfoPage().getUniqueUsername();
    }
}
