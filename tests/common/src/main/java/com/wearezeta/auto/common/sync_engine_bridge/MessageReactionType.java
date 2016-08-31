package com.wearezeta.auto.common.sync_engine_bridge;

import com.waz.model.Liking;

public enum MessageReactionType {
    LIKE(Liking.like()),
    UNLIKE(Liking.unlike());

    private final Liking.Action action;

    MessageReactionType(Liking.Action action) {
        this.action = action;
    }

    public Liking.Action getAction() {
        return action;
    }
}
