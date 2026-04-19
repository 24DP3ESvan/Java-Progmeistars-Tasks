package org.example.library.service;

public class NotificationService {

    public void notifyReader(String readerName, String message) {
        System.out.println("Sending message to " + readerName + ": " + message);
    }
}
