import java.util.*;

public class NotificationService {

    private int totalSent = 0;
    private final MessageRepository repository;

    public NotificationService(MessageRepository repository) {
        this.repository = repository;
    }

    public Message create(NotificationType type, String sender, String recipient, String text) {
        return switch (type) {
            case EMAIL -> new EmailMessage(sender, recipient, text);
            case SMS -> new SmsMessage(sender, recipient, text);
            case CHAT -> new ChatMessage(sender, recipient, text);
        };
    }

    public boolean send(Message message) {

        if (message == null || !message.isValid()) {
            return false;
        }

        NotificationType type;

        if (message instanceof EmailMessage) {
            type = NotificationType.EMAIL;
        } else if (message instanceof SmsMessage) {
            type = NotificationType.SMS;
        } else if (message instanceof ChatMessage) {
            type = NotificationType.CHAT;
        } else {
            return false;
        }

        System.out.println("SENDING...\n" + message.format() + "\n");

        repository.save(type, message);
        totalSent++;

        return true;
    }

    public int send(Message... messages) {
        return send(Arrays.asList(messages));
    }

    public int send(Collection<Message> messages) {
        int success = 0;
        for (Message m : messages) {
            if (send(m)) {
                success++;
            }
        }
        return success;
    }

    public List<Message> getSentMessages(NotificationType type) {
        return repository.findByType(type);
    }

    public int getTotalSent() {
        return totalSent;
    }

    public int getSentCount(NotificationType type) {
        return repository.findByType(type).size();
    }

    public List<Message> findByRecipient(String recipient) {
        return repository.findAll()
                .stream()
                .filter(m -> m.recipient.equals(recipient))
                .toList();
    }
    
    public List<Message> findByTextFragment(String fragment) {
        return repository.findAll()
                .stream()
                .filter(m -> m.text != null && m.text.contains(fragment))
                .toList();
    }

    public MessageRepository getRepository() {
        return repository;
    }
}

enum NotificationType {
    EMAIL, SMS, CHAT
}

abstract class Message {
    protected String sender;
    protected String recipient;
    protected String text;

    Message(String sender, String recipient, String text) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
    }

    abstract boolean isValid();
    abstract String format();
}

class EmailMessage extends Message {
    EmailMessage(String sender, String recipient, String text) {
        super(sender, recipient, text);
    }

    @Override
    boolean isValid() {
        return sender != null && recipient != null && text != null;
    }

    @Override
    String format() {
        return "Email: " + text;
    }
}

class SmsMessage extends Message {
    SmsMessage(String sender, String recipient, String text) {
        super(sender, recipient, text);
    }

    @Override
    boolean isValid() {
        return sender != null && recipient != null && text != null && text.length() <= 160;
    }

    @Override
    String format() {
        return "SMS: " + text;
    }
}

class ChatMessage extends Message {
    ChatMessage(String sender, String recipient, String text) {
        super(sender, recipient, text);
    }

    @Override
    boolean isValid() {
        return sender != null && recipient != null && text != null;
    }

    @Override
    String format() {
        return "Chat: " + text;
    }
}

interface MessageRepository {
    void save(NotificationType type, Message message);
    List<Message> findByType(NotificationType type);
    List<Message> findAll();
}
