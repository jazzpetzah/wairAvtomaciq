package com.wearezeta.auto.web.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class ConversationStates {
    private Map<String, SortedSet<Message>> conversations = new HashMap<>();

    public void addMessage(String conversation, Message message){
        SortedSet<Message> messagesForConversation = conversations.getOrDefault(conversation, new TreeSet<>());
        messagesForConversation.add(message);
        conversations.put(conversation, messagesForConversation);
    }
    
    public SortedSet<Message> getAllMessages(String conversation){
        SortedSet<Message> messages = conversations.getOrDefault(conversation, new TreeSet<>());
        return Collections.unmodifiableSortedSet(messages);
    }
    
}
