package com.example.notifications;

class Message {
    protected String sender;
    protected String recipient;
    protected String text;

    public Message(String phoneNumber, String text) {
        this.recipient = phoneNumber;
        this.text = text;
        this.sender = "";
    }

    public Message(String sender, String phoneNumber, String text) {
        this.sender = sender;
        this.recipient = phoneNumber;
        this.text = text;
    }

    public boolean isValid() {
        return true;
    }

    public String format() {
        return "Message from " + sender + " to " + recipient + ": " + text;
    }
}

public class SmsMessage extends Message {

    public static final int MAX_LENGTH = 160;

    public SmsMessage(String phoneNumber, String text) {
        super(phoneNumber, text);
    }

    public SmsMessage(String sender, String phoneNumber, String text) {
        super(sender, phoneNumber, text);
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && recipient.startsWith("+")
                && text.length() <= MAX_LENGTH;
    }

    @Override
    public String format() {
        return "SMS from " + sender + " to " + recipient + ": " + text;
    }
}
