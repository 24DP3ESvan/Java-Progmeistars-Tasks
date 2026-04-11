package com.example.notifications;

public class Main {

    public static void main(String[] args) {

        MessageRepository repository = new MessageRepository() {
            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public String format() {
                return "";
            }
        };
        NotificationService service = new NotificationService(repository);

        MessageRepository m1 = service.create(NotificationType.EMAIL,
                "alice@example.com",
                "bob@example.com",
                "Hello Bob!");

        MessageRepository m2 = service.create(NotificationType.SMS,
                "+37120000000",
                "+37112345678",
                "Hi from SMS");

        service.send(m1, m2);

        System.out.println("Search by recipient:");
        service.findByRecipient("user2").forEach(System.out::println);

        System.out.println("\nSearch by text fragment:");
        service.findByTextFragment("Hello").forEach(System.out::println);
    }
}
