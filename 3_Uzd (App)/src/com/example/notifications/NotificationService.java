package com.example.notifications;

import java.util.*;

public class NotificationService {

    private int totalSent = 0;
    private final MessageRepository repository;

    public NotificationService(MessageRepository repository) {
        this.repository = repository;
    }

    public MessageRepository create(NotificationType type, String sender, String recipient, String text) {
        return switch (type) {
            case EMAIL -> new EmailMessage(sender, recipient, text);
            case SMS -> new SmsMessage(sender, recipient, text);
        };
    }

    public boolean send(MessageRepository message) {

        if (message == null || !message.isValid()) {
            return false;
        }

        NotificationType type;

        if (message instanceof EmailMessage) {
            type = NotificationType.EMAIL;
        } else if (message instanceof SmsMessage) {
            type = NotificationType.SMS;
        } else {
            return false;
        }

        System.out.println("SENDING...\n" + message.format() + "\n");

        repository.save(type, message);
        totalSent++;

        return true;
    }

    public int send(MessageRepository... messages) {
        return send(Arrays.asList(messages));
    }

    public int send(Collection<MessageRepository> messages) {
        int success = 0;
        for (MessageRepository m : messages) {
            if (send(m)) {
                success++;
            }
        }
        return success;
    }

    public List<MessageRepository> getSentMessages(NotificationType type) {
        return repository.findByType(type);
    }

    public int getTotalSent() {
        return totalSent;
    }

    public int getSentCount(NotificationType type) {
        return repository.findByType(type).size();
    }

    public List<MessageRepository> findByRecipient(String recipient) {
        return repository.findAll()
                .stream()
                .filter(m -> recipient.equals(m.recipient))
                .toList();
    }

    public List<MessageRepository> findByTextFragment(String fragment) {
        return repository.findAll()
                .stream()
                .filter(m -> m.text != null && m.text.contains(fragment))
                .toList();
    }
}
