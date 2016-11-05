package com.wearezeta.auto.ios.steps;

import com.wearezeta.auto.ios.pages.details_overlay.group.GroupPendingParticipantOutgoingConnectionPage;

public class GroupParticipantOutgoingPendingConnectionPageSteps {
    private final IOSPagesCollection pagesCollection = IOSPagesCollection.getInstance();

    private GroupPendingParticipantOutgoingConnectionPage getPage() throws Exception {
        return pagesCollection.getPage(GroupPendingParticipantOutgoingConnectionPage.class);
    }
}
