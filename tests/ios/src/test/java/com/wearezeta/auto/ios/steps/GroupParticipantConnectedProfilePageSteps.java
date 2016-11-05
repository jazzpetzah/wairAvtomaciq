package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.common.usrmgmt.ClientUsersManager;
import com.wearezeta.auto.ios.pages.details_overlay.group.GroupConnectedParticipantProfilePage;

public class GroupParticipantConnectedProfilePageSteps {

    private final ClientUsersManager usrMgr = ClientUsersManager.getInstance();

    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupConnectedParticipantProfilePage getGroupInfoPage() throws Exception {
        return pagesCollection.getPage(GroupConnectedParticipantProfilePage.class);
    }
}
