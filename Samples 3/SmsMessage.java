package com.example.notifications;

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
