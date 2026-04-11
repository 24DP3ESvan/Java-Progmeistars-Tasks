package com.example.notifications;

import java.util.*;

public abstract class MessageRepository {

    private final Map<NotificationType, List<MessageRepository>> storage = new HashMap<>();

    public String sender;
    public String recipient;
    public String text;

    public MessageRepository() {
        for (NotificationType type : NotificationType.values()) {
            storage.put(type, new ArrayList<>());
        }
    }

    public MessageRepository(String sender, String recipient, String text) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }

    public MessageRepository(String phoneNumber, String text) {
        this.sender = phoneNumber;
        this.text = text;
    }

    public void save(NotificationType type, MessageRepository message) {
        storage.get(type).add(message);
    }

    public List<MessageRepository> findByType(NotificationType type) {
        return Collections.unmodifiableList(storage.get(type));
    }

    public List<MessageRepository> findAll() {
        List<MessageRepository> all = new ArrayList<>();
        for (List<MessageRepository> list : storage.values()) {
            all.addAll(list);
        }
        return all;
    }

    public boolean isValid() {
        return sender != null && recipient != null && text != null;
    }

    public abstract String format();
}