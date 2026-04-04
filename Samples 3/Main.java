package com.example.notifications;

import java.util.*;

enum NotificationType {
    EMAIL, SMS, CHAT;
}

class Message {
    private final NotificationType type;
    private final String from;
    private final String to;
    private final String text;

    public Message(NotificationType type, String from, String to, String text) {
        this.type = type;
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public NotificationType getType() {
        return type;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Message{type=" + type + ", from='" + from + "', to='" + to + "', text='" + text + "'}";
    }
}

class MessageRepository {
    private final List<Message> messages = new ArrayList<>();

    public void save(Message message) {
        messages.add(message);
    }

    public List<Message> findByRecipient(String recipient) {
        return messages.stream().filter(m -> m.getTo().equals(recipient)).toList();
    }

    public List<Message> findByTextFragment(String fragment) {
        return messages.stream().filter(m -> m.getText().contains(fragment)).toList();
    }
}

class NotificationService {
    private final MessageRepository repository;

    public NotificationService(MessageRepository repository) {
        this.repository = repository;
    }

    public Message create(NotificationType type, String from, String to, String text) {
        Message message = new Message(type, from, to, text);
        repository.save(message);
        return message;
    }

    public void send(Message... messages) {
        for (Message m : messages) {
            System.out.println("Sending " + m);
        }
    }

    public List<Message> findByRecipient(String recipient) {
        return repository.findByRecipient(recipient);
    }

    public List<Message> findByTextFragment(String fragment) {
        return repository.findByTextFragment(fragment);
    }
}

public class Main {

    public static void main(String[] args) {

        MessageRepository repository = new MessageRepository();
        NotificationService service = new NotificationService(repository);

        Message m1 = service.create(NotificationType.EMAIL,
                "alice@example.com",
                "bob@example.com",
                "Hello Bob!");

        Message m2 = service.create(NotificationType.SMS,
                "+37120000000",
                "+37112345678",
                "Hi from SMS");

        Message chat = service.create(NotificationType.CHAT,
                "user1",
                "user2",
                "Hello in chat!");

        service.send(m1, m2, chat);

        System.out.println("Search by recipient:");
        service.findByRecipient("user2").forEach(System.out::println);

        System.out.println("\nSearch by text fragment:");
        service.findByTextFragment("Hello").forEach(System.out::println);
    }
}
