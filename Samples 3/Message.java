package com.example.notifications;

import java.util.*;

public class MessageRepository {

    private final Map<NotificationType, List<Message>> storage = new HashMap<>();

    public MessageRepository() {
        for (NotificationType type : NotificationType.values()) {
            storage.put(type, new ArrayList<>());
        }
    }

    public void save(NotificationType type, Message message) {
        storage.get(type).add(message);
    }

    public List<Message> findByType(NotificationType type) {
        return Collections.unmodifiableList(storage.get(type));
    }

    public List<Message> findAll() {
        List<Message> all = new ArrayList<>();
        for (List<Message> list : storage.values()) {
            all.addAll(list);
        }
        return all;
    }
}
