package com.example.notifications;

public class MessageRepository {

    private final String sender;
    private final String recipient;
    private final String text;

    public MessageRepository(String sender, String recipient, String text) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }

    // Getters
    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getText() {
        return text;
    }
}
          
