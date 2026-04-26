package org.example.library.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public void notifyReader(String readerName, String message) {
        System.out.println("Sending message to " + readerName + ": " + message);
    }
}
