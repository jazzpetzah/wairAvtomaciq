package com.wearezeta.auto.common.wire_actors.models;

import com.wearezeta.auto.common.misc.Timedelta;

public class MessageInfo {
    private String id;
    private MessageType type;
    private Timedelta timestamp;

    public enum MessageType {
        TEXT,
        TEXT_EMOJI_ONLY,
        ASSET,
        ANY_ASSET,
        VIDEO_ASSET,
        AUDIO_ASSET,
        KNOCK,
        MEMBER_JOIN,
        MEMBER_LEAVE,
        CONNECT_REQUEST,
        CONNECT_ACCEPTED,
        RENAME,
        MISSED_CALL,
        INCOMING_CALL,
        RICH_MEDIA,
        OTR_ERROR,
        OTR_IDENTITY_CHANGED,
        OTR_VERIFIED,
        OTR_UNVERIFIED,
        OTR_DEVICE_ADDED,
        STARTED_USING_DEVICE,
        HISTORY_LOST,
        LOCATION,
        UNKNOWN,
        RECALLED
    }

    public MessageInfo(String id, MessageType type, Timedelta timestamp) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public MessageType getType() {
        return type;
    }

    public Timedelta getTimestamp() {
        return timestamp;
    }
}

