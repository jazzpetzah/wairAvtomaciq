package com.wearezeta.auto.android_tablet.pages;


import com.wearezeta.auto.android.pages.AbstractPickUserPage;
import com.wearezeta.auto.common.driver.ZetaAndroidDriver;

import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.concurrent.Future;

public abstract class AbstractTabletPeoplePickerPage extends AndroidTabletPage {
    public AbstractTabletPeoplePickerPage(Future<ZetaAndroidDriver> lazyDriver) throws Exception {
        super(lazyDriver);
    }

    public void selectEmailOnAlert(String email) throws Exception {
        getPage(getAndroidPageClass()).selectEmailOnAlert(email);
    }

    public void tapPeopleSearch() throws Exception {
        getPage(getAndroidPageClass()).tapPeopleSearch();
    }

    public void typeTextInPeopleSearch(String text) throws Exception {
        getPage(getAndroidPageClass()).typeTextInPeopleSearch(text);
    }

    public boolean isPeopleSearchTextEmpty() throws Exception {
        return getPage(getAndroidPageClass()).isPeopleSearchTextEmpty();
    }

    public boolean waitUntilPageVisible() throws Exception {
        return getPage(getAndroidPageClass()).waitUntilPageVisible();
    }

    public boolean waitUntilPageInvisible() throws Exception {
        return getPage(getAndroidPageClass()).waitUntilPageInvisible();
    }

    public void typeBackspaceInSearchInput() throws Exception {
        getPage(getAndroidPageClass()).typeBackspaceInSearchInput();
    }

    public void tapCreateConversation() throws Exception {
        getPage(getAndroidPageClass()).tapCreateConversation();
    }

    public void tapClearButton() throws Exception {
        getPage(getAndroidPageClass()).tapClearButton();
    }

    public boolean waitUntilNameVisible(boolean isGroup, String name) throws Exception {
        return getPage(getAndroidPageClass()).waitUntilNameVisible(isGroup, name);
    }

    public boolean waitUntilNameInvisible(boolean isGroup, String name) throws Exception {
        return getPage(getAndroidPageClass()).waitUntilNameInvisible(isGroup, name);
    }

    public boolean waitUntilActionButtonIsVisible(String name) throws Exception {
        return getPage(getAndroidPageClass()).waitUntilActionButtonIsVisible(name);
    }

    public boolean waitUntilActionButtonIsInvisible(String name) throws Exception {
        return getPage(getAndroidPageClass()).waitUntilActionButtonIsInvisible(name);
    }

    public void tapActionButton(String name) throws Exception {
        getPage(getAndroidPageClass()).tapActionButton(name);
    }

    public Optional<BufferedImage> getUserAvatarScreenshot(String name) throws Exception {
        return getPage(getAndroidPageClass()).getUserAvatarScreenshot(name);
    }

    public void tapOnUserAvatar(String name) throws Exception {
        getPage(getAndroidPageClass()).tapOnUserAvatar(name);
    }

    public void tapInviteButtonFor(String name) throws Exception {
        getPage(getAndroidPageClass()).tapInviteButtonFor(name);
    }

    public boolean isErrorVisible() throws Exception {
        return getPage(getAndroidPageClass()).isErrorVisible();
    }

    public boolean isErrorInvisible() throws Exception {
        return getPage(getAndroidPageClass()).isErrorInvisible();
    }

    protected abstract Class getAndroidPageClass();

    private <T extends AbstractPickUserPage> T getPage(Class<T> cls) throws Exception {
        return this.getAndroidPageInstance(cls);
    }
}
