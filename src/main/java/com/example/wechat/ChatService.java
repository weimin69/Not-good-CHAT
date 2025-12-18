package com.example.wechat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory chat service for registering users and sending messages.
 */
public class ChatService {
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final List<Message> messages = Collections.synchronizedList(new ArrayList<>());

    /**
     * Registers a new user with the provided username.
     *
     * @param username desired username, must be unique and non-blank
     * @return created User
     * @throws IllegalArgumentException when the username is invalid or already exists
     */
    public User registerUser(String username) {
        Objects.requireNonNull(username, "username");
        String trimmed = username.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (users.containsKey(trimmed)) {
            throw new IllegalArgumentException("User already exists: " + trimmed);
        }
        User user = new User(trimmed);
        users.put(user.getUsername(), user);
        return user;
    }

    /**
     * Sends a message from one registered user to another.
     */
    public Message sendMessage(String fromUsername, String toUsername, String content) {
        validateUserExists(fromUsername);
        validateUserExists(toUsername);
        if (fromUsername.equals(toUsername)) {
            throw new IllegalArgumentException("Cannot send message to yourself");
        }
        Message message = new Message(fromUsername, toUsername, content);
        messages.add(message);
        return message;
    }

    /**
     * Returns all messages exchanged between two users, sorted by timestamp.
     */
    public List<Message> getConversation(String userA, String userB) {
        validateUserExists(userA);
        validateUserExists(userB);
        return messages.stream()
                .filter(m -> isBetween(m, userA, userB))
                .sorted(Comparator.comparing(Message::getTimestamp))
                .collect(Collectors.toList());
    }

    /**
     * Returns all messages received by a user, sorted by newest first.
     */
    public List<Message> getInbox(String username) {
        validateUserExists(username);
        return messages.stream()
                .filter(m -> m.getRecipient().equals(username))
                .sorted(Comparator.comparing(Message::getTimestamp).reversed())
                .collect(Collectors.toList());
    }

    public List<User> listUsers() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getUsername))
                .collect(Collectors.toList());
    }

    private void validateUserExists(String username) {
        if (!users.containsKey(username)) {
            throw new IllegalArgumentException("Unknown user: " + username);
        }
    }

    private boolean isBetween(Message message, String userA, String userB) {
        return (message.getSender().equals(userA) && message.getRecipient().equals(userB))
                || (message.getSender().equals(userB) && message.getRecipient().equals(userA));
    }

    /**
     * Seeds some demo data to let users try the console application quickly.
     */
    public void seedDemoData() {
        if (!messages.isEmpty() || !users.isEmpty()) {
            return;
        }
        registerUser("alice");
        registerUser("bob");
        registerUser("charlie");

        sendMessage("alice", "bob", "你好 Bob，这是一条测试消息！");
        sendMessage("bob", "alice", "收到，Alice！");
        sendMessage("charlie", "alice", "我们一起喝咖啡吧？");
        messages.add(new Message("bob", "charlie", "欢迎加入群聊！",
                LocalDateTime.now().minusMinutes(5)));
    }
}
