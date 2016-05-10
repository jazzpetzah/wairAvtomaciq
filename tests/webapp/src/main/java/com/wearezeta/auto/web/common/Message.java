package com.wearezeta.auto.web.common;

import com.wearezeta.auto.common.log.ZetaLogger;
import java.time.Instant;
import java.util.Objects;
import org.apache.log4j.Logger;

public class Message implements Comparable<Message>{
    
    private static final Logger log = ZetaLogger.getLog(Message.class.getSimpleName());
    
    private String message;
    private String senderId;
    private final Instant timestamp;

    public Message(String message, String sender, Instant timestamp) {
        this.message = message;
        this.senderId = sender;
        this.timestamp = timestamp;
    }
    
    public Message(String message, String sender) {
        this.message = message;
        this.senderId = sender;
        this.timestamp = Instant.now();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.message);
        hash = 11 * hash + Objects.hashCode(this.senderId);
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
        final Message other = (Message) obj;
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.senderId, other.senderId)) {
            return false;
        }
        return true;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(Message m) {
        return m.timestamp.compareTo(this.timestamp);
    }

    @Override
    public String toString() {
        return "Message{" + "message=" + message + ", senderId=" + senderId + ", timestamp=" + timestamp + '}';
    }
}
