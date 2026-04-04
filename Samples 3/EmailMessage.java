package com.example.notifications;

public class EmailMessage extends MessageRepository {

    private String channel;

    public EmailMessage(String sender, String recipient, String text) {
        this(sender, recipient, "general", text);
    }

    public EmailMessage(String sender, String recipient, String channel, String text) {
        super(sender, recipient, text);
        this.channel = channel;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && channel != null && !channel.isBlank();
    }

    @Override
    public String format() {
        return "CHAT [" + channel + "]\n"
                + sender + " -> " + recipient + "\n"
                + text;
    }

    @Override
    public String toString() {
        return super.toString() + ", channel='" + channel + "'}";
    }
}
