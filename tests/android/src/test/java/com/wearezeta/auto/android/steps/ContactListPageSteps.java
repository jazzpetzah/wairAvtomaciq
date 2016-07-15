package com.wearezeta.auto.android.steps;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.android.pages.ContactListPage;
import com.wearezeta.auto.common.backend.BackendAPIWrappers;
import com.wearezeta.auto.common.email.InvitationMessage;
import com.wearezeta.auto.common.email.MessagingUtils;
import com.wearezeta.auto.common.email.handlers.IMAPSMailbox;
import com.wearezeta.auto.common.usrmgmt.ClientUser;
import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class ContactListPageSteps {
    private final AndroidPagesCollection pagesCollection = AndroidPagesCollection.getInstance();

    private ContactListPage getInvitationsPage() throws Exception {
        return pagesCollection.getPage(ContactListPage.class);
    }

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();


}
