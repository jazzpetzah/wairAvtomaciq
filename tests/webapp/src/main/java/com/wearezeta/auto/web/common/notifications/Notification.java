package com.wearezeta.auto.web.common.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

    private final String title;
    private final String body;
    private final String tag;
    private final String timestamp;
    private final String icon;
    private final String lang;
    private final String dir;
    private final String badge;
    private final String conversation_id;
    private final String message_id;
    private final String silent;
    private final String renotify;
    private final String requireInteraction;

    @JsonCreator
    public Notification(
            @JsonProperty("title") String title, 
            @JsonProperty("body") String body, 
            @JsonProperty("tag") String tag, 
            @JsonProperty("timestamp") String timestamp, 
            @JsonProperty("icon") String icon, 
            @JsonProperty("lang") String lang, 
            @JsonProperty("dir") String dir,
            @JsonProperty("badge") String badge, 
            @JsonProperty("conversation_id") String conversation_id, 
            @JsonProperty("message_id") String message_id, 
            @JsonProperty("silent") String silent, 
            @JsonProperty("renotify") String renotify, 
            @JsonProperty("requireInteraction") String requireInteraction) {
        this.title = title;
        this.body = body;
        this.tag = tag;
        this.timestamp = timestamp;
        this.icon = icon;
        this.lang = lang;
        this.dir = dir;
        this.badge = badge;
        this.conversation_id = conversation_id;
        this.message_id = message_id;
        this.silent = silent;
        this.renotify = renotify;
        this.requireInteraction = requireInteraction;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getTag() {
        return tag;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getIcon() {
        return icon;
    }

    public String getLang() {
        return lang;
    }

    public String getDir() {
        return dir;
    }

    public String getBadge() {
        return badge;
    }

    public String getConversation_id() {
        return conversation_id;
    }

    public String getMessage_id() {
        return message_id;
    }

    public String getSilent() {
        return silent;
    }

    public String getRenotify() {
        return renotify;
    }

    public String getRequireInteraction() {
        return requireInteraction;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.conversation_id);
        hash = 97 * hash + Objects.hashCode(this.message_id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Notification other = (Notification) obj;
        if (!Objects.equals(this.conversation_id, other.conversation_id)) {
            return false;
        }
        if (!Objects.equals(this.message_id, other.message_id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Notification{" + "title=" + title + ", body=" + body + ", tag=" + tag + ", timestamp=" + timestamp + ", icon=" + icon + ", lang=" + lang + ", dir=" + dir + ", badge=" + badge + ", conversation_id=" + conversation_id + ", message_id=" + message_id + ", silent=" + silent + ", renotify=" + renotify + ", requireInteraction=" + requireInteraction + '}';
    }

}
