package com.example.wechat;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a message between two users.
 */
public final class Message {
    private final String id;
    private final String sender;
    private final String recipient;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(String sender, String recipient, String content) {
        this(UUID.randomUUID().toString(), sender, recipient, content, LocalDateTime.now());
    }

    public Message(String id, String sender, String recipient, String content, LocalDateTime timestamp) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be blank");
        }
        this.id = Objects.requireNonNull(id, "id");
        this.sender = Objects.requireNonNull(sender, "sender");
        this.recipient = Objects.requireNonNull(recipient, "recipient");
        this.content = content.trim();
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
    }

    public String getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + sender + " -> " + recipient + ": " + content;
    }
}
