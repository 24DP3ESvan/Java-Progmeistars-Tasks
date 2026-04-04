package com.example.notifications;

import java.util.*;

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
